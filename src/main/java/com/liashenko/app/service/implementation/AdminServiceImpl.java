package com.liashenko.app.service.implementation;

import com.liashenko.app.persistance.dao.*;
import com.liashenko.app.persistance.dao.mysql.MySQLDaoFactory;
import com.liashenko.app.persistance.domain.Role;
import com.liashenko.app.persistance.domain.User;
import com.liashenko.app.service.storage_connection.DBConnectService;
import com.liashenko.app.service.AdminService;
import com.liashenko.app.service.exceptions.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.liashenko.app.service.storage_connection.DBConnectService.close;


public class AdminServiceImpl implements AdminService {
    private static final Logger classLogger = LogManager.getLogger(AdminServiceImpl.class);

    private DaoFactory daoFactory;
    private DBConnectService DBConnectService;
    private ResourceBundle localeQueries;

    public AdminServiceImpl(ResourceBundle localeQueries){
        this.localeQueries = localeQueries;
        this.DBConnectService = new DBConnectService();
        this.daoFactory = new MySQLDaoFactory();
    }

    public Optional<List<User>> showUsers(int rowsPerPage, int offset){
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        final List<User> userList;
        try {
            Optional<GenericJDBCDao>  userDaoOpt = daoFactory.getDao(connection, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(ServiceException::new);
            Optional<List<User>> userListOpt = userDao.getPages(rowsPerPage, offset);
            if (!userListOpt.isPresent()) return Optional.empty();

            userList = new ArrayList<>();
            userListOpt.get().forEach(user -> userList.add(new User(user.getId(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getRoleId(), user.getBanned())));
        } catch (ServiceException | DAOException e) {
            classLogger.error("Transaction wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            close(connection);
        }
        return Optional.of(userList);
    }

    public Optional<List<User>> showUsers(){
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        final List<User> userList;
        try {
            Optional<GenericJDBCDao>  userDaoOpt = daoFactory.getDao(connection, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(ServiceException::new);
            Optional<List<User>> userListOpt = userDao.getAll();
            if (!userListOpt.isPresent()) return Optional.empty();

            userList = new ArrayList<>();
            userListOpt.get().forEach(user -> userList.add(new User(user.getId(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), user.getRoleId(), user.getBanned())));
        } catch (ServiceException | DAOException e) {
            classLogger.error("Transaction wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            close(connection);
        }
        return Optional.of(userList);
    }

    @Override
    public Optional<List<Role>> showRoles() throws ServiceException {
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        Optional<List<Role>> rolesListOpt;
        try {
            Optional<GenericJDBCDao>  rolesDaoOpt = daoFactory.getDao(connection, Role.class, localeQueries);
            RoleDao roleDao = (RoleDao) rolesDaoOpt.orElseThrow(ServiceException::new);
            rolesListOpt = roleDao.getAll();
        } catch (ServiceException | DAOException e) {
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            close(connection);
        }
        return rolesListOpt;
    }

    @Override
    public void updateUserInfo(User user) {
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
            Optional<GenericJDBCDao>  userDaoOpt = daoFactory.getDao(connection, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(ServiceException::new);
            User newUser = userDao.getByPK(user.getId()).orElseThrow(() -> new ServiceException("User doesn't exist"));
            newUser.setId(user.getId());
            newUser.setBanned(user.getBanned());
            newUser.setRoleId(user.getRoleId());
            userDao.update(newUser);
        } catch (ServiceException | DAOException e){
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            close(connection);
        }
    }


    @Override
    public Integer getUsersCount() {
        Optional<Connection> connectionOpt = DBConnectService.getConnection();
        Connection connection = connectionOpt.orElseThrow(() -> new ServiceException("Operation wasn't successful"));
        try {
            Optional<GenericJDBCDao>  userDaoOpt = daoFactory.getDao(connection, User.class, localeQueries);
            UserDao userDao = (UserDao) userDaoOpt.orElseThrow(ServiceException::new);
            return userDao.getCount();
        } catch (ServiceException | DAOException e){
            classLogger.error("Operation wasn't successful", e);
            throw new ServiceException("Operation wasn't successful");
        } finally {
            close(connection);
        }
    }
}
