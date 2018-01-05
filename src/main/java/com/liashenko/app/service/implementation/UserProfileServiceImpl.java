package com.liashenko.app.service.implementation;

import com.liashenko.app.persistance.dao.*;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.Password;
import com.liashenko.app.persistance.domain.User;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.UserProfileService;
import com.liashenko.app.service.dto.AdminViewDto;
import com.liashenko.app.service.storage_connection.DBConnectService;
import com.liashenko.app.utils.AppProperties;
import com.liashenko.app.utils.PasswordProcessor;
import com.liashenko.app.utils.exceptions.PasswordProcessorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserProfileServiceImpl implements UserProfileService {
    private static final Logger classLogger = LogManager.getLogger(UserProfileServiceImpl.class);

    private DaoFactory daoFactory;
    private DBConnectService DBConnectService;
    private ResourceBundle localeQueries;


    public UserProfileServiceImpl(ResourceBundle localeQueries){
        this.localeQueries = localeQueries;
        this.DBConnectService = new DBConnectService();
        this.daoFactory = new MySQLDaoFactory();
    }

    @Override
    public void createProfile(AdminViewDto adminViewDto) {
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
            connection.setAutoCommit(false);
            Optional<GenericJDBCDao>  userDaoOpt = daoFactory.getDao(connection, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(ServiceException::new);

            Optional<GenericJDBCDao>  passwordDaoOpt = daoFactory.getDao(connection, Password.class, localeQueries);
            byte[] salt = PasswordProcessor.generateSalt(adminViewDto.getPassword().length);
            byte [] encryptedPass = PasswordProcessor.getEncryptedPass(adminViewDto.getPassword(), salt);

            PasswordDao passwordDao = (PasswordDao) passwordDaoOpt.orElseThrow(ServiceException::new);
            Password password = new Password(encryptedPass, salt, AppProperties.getPassAlgIterationValue(), AppProperties.getPassGenerationAlg());
            password = passwordDao.persist(password).orElseThrow(() -> new ServiceException("Operation wasn't successful"));

            User newUser = new User();
            newUser.setFirstName(adminViewDto.getFirstName());
            newUser.setLastName(adminViewDto.getLastName());
            newUser.setEmail(adminViewDto.getEmail());
            newUser.setLanguage(adminViewDto.getLanguage());
            newUser.setBanned(adminViewDto.getBanned());
            newUser.setRoleId(adminViewDto.getRoleId());
            newUser.setPasswordId(password.getId());

            userDao.create(newUser);
            connection.commit();
        } catch (PasswordProcessorException | ServiceException | SQLException | DAOException e){
            classLogger.error("Operation wasn't successful", e);
            DBConnectService.rollback(connection);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            DBConnectService.close(connection);
        }
    }

    @Override
    public void changeLanguage(Long userId, String newLanguage) {
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
            Optional<GenericJDBCDao>  userDaoOpt = daoFactory.getDao(connection, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(ServiceException::new);
            User newUser = userDao.getByPK(userId).orElseThrow(() -> new ServiceException("User doesn't exist"));
            newUser.setLanguage(newLanguage);
            userDao.update(newUser);
        } catch (ServiceException | DAOException e){
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            DBConnectService.close(connection);
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(connection, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(ServiceException::new);
            return userDao.isEmailExists(email);
        } catch (ServiceException | DAOException e){
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            DBConnectService.close(connection);
        }
    }
}
