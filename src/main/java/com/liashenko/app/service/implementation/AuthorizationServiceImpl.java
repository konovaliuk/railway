package com.liashenko.app.service.implementation;

import com.liashenko.app.persistance.dao.DaoFactory;
import com.liashenko.app.persistance.dao.GenericJDBCDao;
import com.liashenko.app.persistance.dao.PasswordDao;
import com.liashenko.app.persistance.dao.UserDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.Password;
import com.liashenko.app.persistance.domain.User;
import com.liashenko.app.service.AuthorizationService;
import com.liashenko.app.service.data_source.DbConnectService;
import com.liashenko.app.service.dto.PrinciplesDto;
import com.liashenko.app.service.dto.UserSessionProfileDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.utils.PasswordProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;

public class AuthorizationServiceImpl implements AuthorizationService {
    private static final Logger classLogger = LogManager.getLogger(AuthorizationServiceImpl.class);

    private static final DbConnectService dbConnectService = DbConnectService.getInstance();
    private static final DaoFactory daoFactory = MySQLDaoFactory.getInstance();

    private ResourceBundle localeQueries;

    public AuthorizationServiceImpl(ResourceBundle localeQueries) {
        this.localeQueries = localeQueries;
    }

    @Override
    public Optional<UserSessionProfileDto> logIn(PrinciplesDto principlesDto) {
        Connection conn = dbConnectService.getConnection();
        try {
//            conn.setReadOnly(true);
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));

            User user = userDao.getUserByEmail(principlesDto.getEmail()).orElseThrow(() -> new ServiceException("User is null"));
            if (user.getBanned()) return Optional.empty();

            Optional<GenericJDBCDao> passwordDaoOpt = daoFactory.getDao(conn, Password.class, localeQueries);
            PasswordDao passwordDao = (PasswordDao) passwordDaoOpt
                    .orElseThrow(() -> new ServiceException("PasswordDao is null"));

            Password password = passwordDao.getByPK(user.getPasswordId())
                    .orElseThrow(() -> new ServiceException("Password is null"));

            if (PasswordProcessor.checkPassword(principlesDto.getPassword(), password.getPassword(),
                    password.getIterations(), password.getSalt(), password.getAlgorithm())) {
                return Optional.of(new UserSessionProfileDto(user.getId(), user.getRoleId(), user.getLanguage()));
            }
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }

        return Optional.empty();
    }
}
