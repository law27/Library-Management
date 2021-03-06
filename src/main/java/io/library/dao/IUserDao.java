package io.library.dao;

import io.library.model.User;

import java.sql.SQLException;

public interface IUserDao {
    boolean checkUserNameAvailability(String userName) throws SQLException;
    void addUser(User user) throws SQLException;
    int getUserId(String userName) throws SQLException;
    User getUser(String userName) throws SQLException;
}
