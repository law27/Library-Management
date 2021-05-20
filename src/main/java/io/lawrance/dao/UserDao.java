package io.lawrance.dao;

import io.lawrance.menu.UserMenu;
import io.lawrance.model.AccessLevel;
import io.lawrance.model.User;

public class UserDao {

    public static boolean checkUserNameAvailability(String userName) {
        return true;
    }

    public static void addUser(User user) {
        System.out.println("User Added");
    }

    public static User getUser(String userName) {
        return new User("lawrance", "1234567", AccessLevel.USER, new UserMenu());
    }

}
