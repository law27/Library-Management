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

    public void createConnection(Properties properties) throws SQLException, ClassNotFoundException {
            String jdbcDriver = properties.getProperty("jdbc-driver");
            String dbURL = properties.getProperty("db-url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(dbURL, username, password);
            connection.setAutoCommit(false);
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

    public void commit() throws SQLException {
        connection.commit();
    }

    public static ResultSet sqlExecutionerForSelect(String sql) throws SQLException {
        Statement statement = DataSourceDatabase.getInstance().getExecutionStatement();
        return statement.executeQuery(sql);
    }

    public static void sqlExecutionerForDML(String sql) throws SQLException {
        Statement statement = DataSourceDatabase.getInstance().getExecutionStatement();
        statement.execute(sql);
    }

    private void closeConnection() throws SQLException {
        connection.close();
    }

    private void rollback() throws SQLException {
        connection.rollback();
    }

    public static void closeDataBaseConnection() throws SQLException {
        if(connector != null) {
            DataSourceDatabase.getInstance().closeConnection();
        }
        System.out.println("Connection closed");
    }

    public static void commitToDatabase() throws SQLException {
        if(connector != null) {
            connector.commit();
        }
        else {
            System.out.println("Database connection is not resolved");
        }
    }

    public static void rollbackUncommittedStatement() throws SQLException {
        if(connector != null) {
            connector.rollback();
        }
        else {
            System.out.println("Database connection is not resolved");
        }
    }

}
