package com.liashenko.app.service.implementation;

import com.liashenko.app.persistance.dao.DaoFactory;
import com.liashenko.app.persistance.dao.GenericJDBCDao;
import com.liashenko.app.persistance.dao.RoleDao;
import com.liashenko.app.persistance.dao.UserDao;
import com.liashenko.app.persistance.dao.exceptions.DAOException;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.Role;
import com.liashenko.app.persistance.domain.User;
import com.liashenko.app.service.AdminService;
import com.liashenko.app.service.data_source.DbConnectService;
import com.liashenko.app.service.exceptions.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class AdminServiceImpl implements AdminService {
    private static final Logger classLogger = LogManager.getLogger(AdminServiceImpl.class);

    private static final DbConnectService dbConnectService = DbConnectService.getInstance();
    private static final DaoFactory daoFactory = MySQLDaoFactory.getInstance();

    private ResourceBundle localeQueries;

    public AdminServiceImpl(ResourceBundle localeQueries) {
        this.localeQueries = localeQueries;
    }

    public Optional<List<User>> showUsers(int rowsPerPage, int offset) {
        List<User> userList = new ArrayList<>();
        Connection conn = dbConnectService.getConnection();
        try {
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));
            Optional<List<User>> userListOpt = userDao.getPages(rowsPerPage, offset);
            userListOpt.ifPresent(users -> users.forEach(user
                    -> userList.add(new User(user.getId(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getRoleId(), user.getBanned()))));
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
        return Optional.of(userList);
    }

    @Override
    public Optional<List<Role>> showRoles() throws ServiceException {
        Connection conn = dbConnectService.getConnection();
        Optional<List<Role>> rolesListOpt;
        try {
            Optional<GenericJDBCDao> rolesDaoOpt = daoFactory.getDao(conn, Role.class, localeQueries);
            RoleDao roleDao = (RoleDao) rolesDaoOpt.orElseThrow(ServiceException::new);
            rolesListOpt = roleDao.getAll();
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
        return rolesListOpt;
    }

    @Override
    public void updateUserInfo(User user) {
        Connection conn = dbConnectService.getConnection();
        try {
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));
            User newUser = userDao.getByPK(user.getId()).orElseThrow(() -> new ServiceException("User doesn't exist"));
            newUser.setId(user.getId());
            newUser.setBanned(user.getBanned());
            newUser.setRoleId(user.getRoleId());
            userDao.update(newUser);
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
    }

    @Override
    public Integer getUsersCount() {
        Connection conn = dbConnectService.getConnection();
        try {
            Optional<GenericJDBCDao> userDaoOpt = daoFactory.getDao(conn, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(() -> new ServiceException("UserDao is null"));
            return userDao.getCount();
        } catch (ServiceException | DAOException e) {
            classLogger.error(e);
            throw new ServiceException(e);
        } finally {
            DbConnectService.close(conn);
        }
    }
}
