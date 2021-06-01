package io.library.datasource;

import io.library.auth.LoggedInUser;
import io.library.dao.*;
import io.library.service.LoggingService;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSourceDatabase implements IDataSource {
    private static final Logger logger = LoggingService.getLogger(DataSourceDatabase.class);
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

            logger.log(Level.INFO ,"Database connection established");
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

        logger.log(Level.INFO, "Committing to Database");
    }

    public static ResultSet sqlExecutionerForSelect(String sql) throws SQLException {
        Statement statement = DataSourceDatabase.getInstance().getExecutionStatement();
        ResultSet resultSet =  statement.executeQuery(sql);

        logger.log(Level.INFO, "executed sql statement: " + sql);

        return resultSet;
    }

    public static void sqlExecutionerForDML(String sql) throws SQLException {
        Statement statement = DataSourceDatabase.getInstance().getExecutionStatement();
        statement.execute(sql);

        logger.log(Level.INFO, "executed sql statement: " + sql);
    }

    private void closeConnection() throws SQLException {
        connection.close();

        logger.info("Database connection closed");
    }

    private void rollback() throws SQLException {
        connection.rollback();

        logger.log(Level.WARNING, "Database rollback executed ");

    }

    public static void closeDataBaseConnection() throws SQLException {
        if(connector != null) {
            DataSourceDatabase.getInstance().closeConnection();
        }
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
