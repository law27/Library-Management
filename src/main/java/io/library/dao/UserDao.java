package io.library.dao;

import io.library.datasource.DataSourceDatabase;
import io.library.menu.AdminMenu;
import io.library.menu.IMenu;
import io.library.menu.UserMenu;
import io.library.model.AccessLevel;
import io.library.model.User;
import io.library.service.Utility;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao implements IUserDao {

    public boolean checkUserNameAvailability(String userName) throws SQLException {
        String sql = String.format("SELECT COUNT(*) FROM users WHERE name=%s", Utility.getFormattedString(userName));
        ResultSet resultSet = DataSourceDatabase.sqlExecutionerForSelect(sql);
        int count = 0;
        while (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        return count == 0;
    }

    public void addUser(User user) throws SQLException {
        String sql = String.format("INSERT INTO users(name, password, access_level, mobile_number, age) " +
                        "VALUES (%s, %s, %d, %s, %d)",
                Utility.getFormattedString(user.getUserName()),
                Utility.getFormattedString(user.getPassword()),
                user.getAccessLevel().ordinal(),
                Utility.getFormattedString(user.getMobileNumber()),
                user.getAge());
        DataSourceDatabase.sqlExecutionerForDML(sql);
        System.out.println("User added");
    }

    public int getUserId(String userName) throws SQLException {
        String sql = String.format("SELECT id FROM users WHERE name=%s", Utility.getFormattedString(userName));
        ResultSet resultSet = DataSourceDatabase.sqlExecutionerForSelect(sql);
        int id = -1;
        while (resultSet.next()) {
            id = resultSet.getInt("id");
        }
        return id;
    }

    public User getUser(String userName) throws SQLException {
        String sql = String.format("SELECT * FROM users WHERE name=%s", Utility.getFormattedString(userName));
        ResultSet resultSet = DataSourceDatabase.sqlExecutionerForSelect(sql);
        User user = null;
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String password = resultSet.getString("password");
            AccessLevel accessLevel = AccessLevel.values()[resultSet.getInt("access_level")];
            String mobileNumber = resultSet.getString("mobile_number");
            int age = resultSet.getInt("age");
            IMenu menu;
            if(accessLevel == AccessLevel.ADMIN) {
                menu = new AdminMenu();
            }
            else {
                menu = new UserMenu();
            }
            user = new User(name, password, mobileNumber, age, accessLevel, menu);
            break;
        }
        return user;
    }

}
