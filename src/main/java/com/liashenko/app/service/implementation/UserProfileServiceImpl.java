package com.liashenko.app.service.implementation;

import com.liashenko.app.persistance.dao.DaoFactory;
import com.liashenko.app.persistance.dao.GenericJDBCDao;
import com.liashenko.app.persistance.dao.PasswordDao;
import com.liashenko.app.persistance.dao.UserDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.Password;
import com.liashenko.app.persistance.domain.User;
import com.liashenko.app.service.UserProfileService;
import com.liashenko.app.service.data_source.DbConnectService;
import com.liashenko.app.service.dto.UserDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.utils.AppProperties;
import com.liashenko.app.utils.PasswordProcessor;
import com.liashenko.app.utils.exceptions.PasswordProcessorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.liashenko.app.controller.utils.Asserts.assertIsNull;

public class UserProfileServiceImpl implements UserProfileService {
    private static final Logger classLogger = LogManager.getLogger(UserProfileServiceImpl.class);

    private static final DbConnectService dbConnectService = DbConnectService.getInstance();
    private static final DaoFactory daoFactory = MySQLDaoFactory.getInstance();

    private ResourceBundle localeQueries;

    public UserProfileServiceImpl(ResourceBundle localeQueries) {
        this.localeQueries = localeQueries;
    }

    @Override
    public void createProfile(UserDto userDto) {
        if (assertIsNull(userDto)) throw new ServiceException("userDto is null");
        Connection conn = dbConnectService.getConnection();
        try {
            conn.setAutoCommit(false);
            Optional<GenericJDBCDao> passwordDaoOpt = daoFactory.getDao(conn, Password.class, localeQueries);
            PasswordDao passwordDao = (PasswordDao) passwordDaoOpt
                    .orElseThrow(() -> new ServiceException("PasswordDao is null"));

            byte[] salt = PasswordProcessor.generateSalt(userDto.getPassword().length);
            byte[] encryptedPass = PasswordProcessor.getEncryptedPass(userDto.getPassword(), salt);

            Password password = new Password(encryptedPass, salt, AppProperties.getPassAlgIterationValue(), AppProperties.getPassGenerationAlg());
            password = passwordDao.persist(password)
                    .orElseThrow(() -> new ServiceException("Couldn't persist password"));

            User newUser = getUserEntity(userDto, password);

            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));
            userDao.create(newUser);
            conn.commit();
        } catch (PasswordProcessorException | ServiceException | SQLException | DAOException e) {
            classLogger.error(e);
            DbConnectService.rollback(conn);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
    }

    private User getUserEntity(UserDto userDto, Password pass) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setLanguage(userDto.getLanguage());
        user.setBanned(userDto.getBanned());
        user.setRoleId(userDto.getRoleId());
        user.setPasswordId(pass.getId());
        return user;
    }

    @Override
    public void changeLanguage(Long userId, String newLanguage) {
        Connection conn = dbConnectService.getConnection();
        try {
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));
            User newUser = userDao.getByPK(userId).orElseThrow(() -> new ServiceException("User doesn't exist"));
            newUser.setLanguage(newLanguage);
            userDao.update(newUser);
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        Connection conn = dbConnectService.getConnection();
        try {
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));
            return userDao.isEmailExists(email);
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
    }

    @Override
    public Optional<UserDto> getUserById(Long userId) {
        Connection conn = dbConnectService.getConnection();
        UserDto userDto = null;
        try {
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));
            Optional<User> userOpt = userDao.getByPK(userId);
            if (userOpt.isPresent()){
               userDto = UserDto.builder()
                        .email(userOpt.get().getEmail())
                        .firstName(userOpt.get().getFirstName())
                        .lastName(userOpt.get().getLastName())
                        .build();
            }
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
        return Optional.ofNullable(userDto);
    }

    @Override
    public void banUserProfile(Long userId) {
        Connection conn = dbConnectService.getConnection();
        try {
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));
            User user = userDao.getByPK(userId).orElseThrow(() -> new ServiceException("User doesn't exist"));
            user.setBanned(Boolean.TRUE);
            userDao.update(user);
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
    }

    @Override
    public void updateProfile(UserDto userDto) {
        if (assertIsNull(userDto)) throw new ServiceException("userDto is null");
        Connection conn = dbConnectService.getConnection();
        try {
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));
            User user = userDao.getByPK(userDto.getId()).orElseThrow(() -> new ServiceException("User doesn't exist"));
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            userDao.update(user);
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }

    }

    @Override
    public boolean isOtherUsersWithEmailExist(Long userId, String email) {
        Connection conn = dbConnectService.getConnection();
        try {
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));
            return userDao.isOtherUsersWithEmailExist(userId, email);
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
    }
}
