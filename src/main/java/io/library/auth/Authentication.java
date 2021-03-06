package io.library.auth;

import io.library.datasource.GlobalDataSource;
import io.library.menu.UserMenu;
import io.library.model.AccessLevel;
import io.library.model.User;
import io.library.service.CustomLevel;
import io.library.service.LoggingService;
import io.library.service.Utility;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Authentication {

    private final static Logger logger = LoggingService.getLogger(Authentication.class);

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
            int age;
            try {
                age = sc.nextInt();
            }
            catch (Exception exception) {
                sc.nextLine();

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

                System.out.println("Age should be of number");
                return;
            }
            sc.nextLine();
            System.out.println();
            try {
                if (userName.equals("") || mobileNumber.equals("") || mobileNumber.startsWith("-")) {
                    System.out.println("Fields can't be empty");
                } else if (age < 0 || age > 110) {
                    System.out.println("Enter a valid age");
                } else if (!GlobalDataSource.getDataSource().getUserDao().checkUserNameAvailability(userName)) {
                    System.out.println("Username already taken. Try different one");
                } else if (!(password.length() > 6)) {
                    System.out.println("Password length should consist more than 6 characters");
                } else {
                    User user = new User(userName, password, mobileNumber, age, AccessLevel.USER, new UserMenu());
                    GlobalDataSource.getDataSource().getUserDao().addUser(user);
                    logger.log(Level.INFO, "New user added " + userName );
                    completed = true;
                }
            } catch (SQLException exception) {

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

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
