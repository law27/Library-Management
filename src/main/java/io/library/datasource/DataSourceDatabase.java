package io.library.datasource;

import io.library.dao.*;

import java.sql.*;
import java.util.Properties;

public class DataSourceDatabase implements IDataSource {
    private static DataSourceDatabase connector = null;
    private Connection connection;
    private final IUserDao userDao;
    private final IBookDao bookDao;
    private final IBorrowBookDao borrowBookDao;

    private DataSourceDatabase() {
        this.userDao = new UserDao();
        this.bookDao = new BookDao();
        this.borrowBookDao = new BorrowBookDao();
    }

    public static DataSourceDatabase getInstance() {
        if(connector == null) {
            connector = new DataSourceDatabase();
        }
        return connector;
    }

    @Override
    public IUserDao getUserDao() {
        return userDao;
    }

    @Override
    public IBookDao getBookDao() {
        return bookDao;
    }

    @Override
    public IBorrowBookDao getBorrowBookDao() {
        return borrowBookDao;
    }

    public void createConnection(Properties properties) {
        try {
            String jdbcDriver = properties.getProperty("jdbc-driver");
            String dbURL = properties.getProperty("db-url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(dbURL, username, password);
        } catch (ClassNotFoundException | SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Statement getExecutionStatement() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        return statement;
    }

    public static ResultSet sqlExecutionerForSelect(String sql) throws SQLException {
        Statement statement = DataSourceDatabase.getInstance().getExecutionStatement();
        return statement.executeQuery(sql);
    }

    public static void sqlExecutionerForDML(String sql) throws SQLException {
        Statement statement = DataSourceDatabase.getInstance().getExecutionStatement();
        statement.execute(sql);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public static void closeDataBaseConnection() throws SQLException {
        DataSourceDatabase.getInstance().closeConnection();
    }

}
