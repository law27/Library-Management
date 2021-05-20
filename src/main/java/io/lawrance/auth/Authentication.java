package io.lawrance.auth;

import io.lawrance.dao.UserDao;
import io.lawrance.menu.UserMenu;
import io.lawrance.model.AccessLevel;
import io.lawrance.model.User;

import java.util.Scanner;

public class Authentication {

    private Authentication() {

    }

    public static void signUp() {
        Scanner sc = new Scanner(System.in);
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
            System.out.println();
            if(!UserDao.checkUserNameAvailability(userName)) {
                System.out.println("Username already taken. Try different one");
            }
            else if(!(password.length() > 6)) {
                System.out.println("Password length should consist more than 6 characters");
            }
            else {
                User user = new User(userName, password, mobileNumber, age, AccessLevel.USER, new UserMenu());
                UserDao.addUser(user);
                completed = true;
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
