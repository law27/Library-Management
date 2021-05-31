package io.library.dao;

import io.library.datasource.DataSourceDatabase;
import io.library.datasource.GlobalDataSource;
import io.library.menu.UserMenu;
import io.library.model.AccessLevel;
import io.library.model.User;
import io.library.service.Utility;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UserDaoTest {
    UserDao userDao;

    UserDaoTest() {
        userDao = new UserDao();
    }
    @BeforeAll
    static void createConnection() throws SQLException {
        String fileName = "application.properties";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = classLoader.getResourceAsStream(fileName)) {
            properties.load(resourceStream);
            DataSourceDatabase.getInstance().createConnection(properties);
            GlobalDataSource.setDataSource(DataSourceDatabase.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String userDelete = "DELETE FROM users";
        DataSourceDatabase.sqlExecutionerForDML(userDelete);
        DataSourceDatabase.commitToDatabase();
    }

    @AfterAll
    static void closeDataBaseConnection() throws SQLException {
        String sql = "DELETE FROM users";
        DataSourceDatabase.sqlExecutionerForDML(sql);
        DataSourceDatabase.commitToDatabase();
        DataSourceDatabase.closeDataBaseConnection();
    }

    @AfterEach
    void tearDown() throws SQLException {
        String sql = "DELETE FROM users";
        DataSourceDatabase.sqlExecutionerForDML(sql);
    }

    @Test
    public void checkWhetherUserIsBeingAdded() throws SQLException {
        User testUser = new User("testing", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        userDao.addUser(testUser);
        User expectedUser = userDao.getUser("testing");
        Condition<User> expected = new Condition<>((User paramUser) -> (
                expectedUser.getUserName().equals(paramUser.getUserName()) &&
                        expectedUser.getPassword().equals(paramUser.getPassword()) &&
                        expectedUser.getAccessLevel() == paramUser.getAccessLevel() &&
                        expectedUser.getAge() == paramUser.getAge() &&
                        expectedUser.getMobileNumber().equals(paramUser.getMobileNumber())
        ), "Checking user field by field");
        assertThat(expectedUser).has(expected);
    }

    @Test
    public void checkWhetherSameUserNameThrowsException() throws SQLException {
        User testUser = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        userDao.addUser(testUser);
        DataSourceDatabase.commitToDatabase();
        User userWithSameName = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        assertThatExceptionOfType(SQLException.class).isThrownBy(() -> userDao.addUser(userWithSameName));
    }

    @Test
    public void checkWhetherUserNameAvailabilityWorks() throws SQLException {
        User testUser = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        userDao.addUser(testUser);
        DataSourceDatabase.commitToDatabase();
        assertThat(userDao.checkUserNameAvailability("test")).isFalse();
        assertThat(userDao.checkUserNameAvailability("lawrance")).isTrue();
    }

    @Test
    public void checkWhetherGetUserIdReturnsId() throws SQLException {
        User testUser = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        userDao.addUser(testUser);
        DataSourceDatabase.commitToDatabase();
        int expected = userDao.getUserId("test");
        assertThat(expected).isPositive();
        expected = userDao.getUserId("user");
        assertThat(expected).isNegative();
    }

    @Test
    public void checkGetUser() throws SQLException {
        User testUser = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        userDao.addUser(testUser);
        DataSourceDatabase.commitToDatabase();
        User expectedUser = userDao.getUser("test");
        Condition<User> expected = new Condition<>((User paramUser) -> (
                expectedUser.getUserName().equals(paramUser.getUserName()) &&
                expectedUser.getPassword().equals(paramUser.getPassword()) &&
                expectedUser.getAccessLevel() == paramUser.getAccessLevel() &&
                expectedUser.getAge() == paramUser.getAge() &&
                expectedUser.getMobileNumber().equals(paramUser.getMobileNumber())
        ), "Checking user field by field");
        assertThat(expectedUser).has(expected);
    }

}



