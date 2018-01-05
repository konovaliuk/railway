package com.liashenko.app.service.implementation;

import com.liashenko.app.persistance.dao.*;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.Password;
import com.liashenko.app.persistance.domain.User;
import com.liashenko.app.service.AuthorizationService;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.dto.PrinciplesDto;
import com.liashenko.app.service.dto.UserSessionProfileDto;
import com.liashenko.app.service.storage_connection.DBConnectService;
import com.liashenko.app.utils.PasswordProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AuthorizationServiceImpl implements AuthorizationService {
    private static final Logger classLogger = LogManager.getLogger(AuthorizationServiceImpl.class);

    private DaoFactory daoFactory;
    private DBConnectService DBConnectService;
    private ResourceBundle localeQueries;

    public AuthorizationServiceImpl(ResourceBundle localeQueries){
        this.localeQueries = localeQueries;
        this.DBConnectService = new DBConnectService();
        this.daoFactory = new MySQLDaoFactory();
    }


//check this method
    @Override
    public Optional<UserSessionProfileDto> logIn(PrinciplesDto principlesDto) {
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
//            connection.setReadOnly(true);
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(connection, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(ServiceException::new);
            User user = userDao.getUserByEmail(principlesDto.getEmail()).orElseThrow(() -> new ServiceException("Operation wasn't successful"));
            if (user.getBanned()) return Optional.empty();

                    Optional<GenericJDBCDao>  passwordDaoOpt = daoFactory.getDao(connection, Password.class, localeQueries);
            PasswordDao passwordDao = (PasswordDao) passwordDaoOpt.orElseThrow(ServiceException::new);
            Password password = passwordDao.getByPK(user.getPasswordId()).orElseThrow(() -> new ServiceException("Operation wasn't successful"));

            if (PasswordProcessor.checkPassword(principlesDto.getPassword(), password.getPassword(), password.getIterations(), password.getSalt(), password.getAlgorithm())){
                return Optional.of(new UserSessionProfileDto(user.getId(), user.getRoleId(), user.getLanguage()));
            }

        } catch (ServiceException | DAOException e){
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            try {
                System.out.println(">>>AuthorizationServiceImpl.logIn 1readOnly : " + connection.isReadOnly());
                com.liashenko.app.service.storage_connection.DBConnectService.close(connection);
                System.out.println("AuthorizationServiceImpl.logIn : closed?");
            } catch (SQLException e){
                System.out.println(e);
            }
        }

        return Optional.empty();
    }
}
