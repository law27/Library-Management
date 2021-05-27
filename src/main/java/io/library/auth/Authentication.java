package io.library.auth;

import io.library.datasource.GlobalDataSource;
import io.library.menu.UserMenu;
import io.library.model.AccessLevel;
import io.library.model.User;
import io.library.service.Utility;

import java.sql.SQLException;
import java.util.Scanner;

public class Authentication {

    private Authentication() {

    }

    public static void signUp() {
        Scanner sc = Utility.getScanner();
        boolean completed = false;
        while (!completed) {
            System.out.print("Enter userName: ");
            String userName = sc.nextLine();
            System.out.println();
            System.out.print("Enter password ");
            String password = sc.nextLine();
            System.out.println();
            System.out.print("Enter Mobile Number ");
            String mobileNumber = sc.nextLine();
            System.out.println();
            System.out.print("Enter Age ");
            int age = sc.nextInt();
            sc.nextLine();
            System.out.println();
            try {
                if (!GlobalDataSource.getDataSource().getUserDao().checkUserNameAvailability(userName)) {
                    System.out.println("Username already taken. Try different one");
                } else if (!(password.length() > 6)) {
                    System.out.println("Password length should consist more than 6 characters");
                } else {
                    User user = new User(userName, password, mobileNumber, age, AccessLevel.USER, new UserMenu());
                    GlobalDataSource.getDataSource().getUserDao().addUser(user);
                    completed = true;
                }

            } catch (SQLException exception) {
                System.out.println("Error in SQL");
                exception.printStackTrace();
            }
        }
    }

    public static ILogin getLoginInstance(AccessLevel role) {
        ILogin login = null;
        if(role == AccessLevel.USER) {
            login = UserLogin.getInstance();
        }
        else if(role == AccessLevel.ADMIN) {
            login = AdminLogin.getInstance();
        }
        return login;
    }
}
