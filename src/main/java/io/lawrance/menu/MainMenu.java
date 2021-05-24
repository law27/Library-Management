package io.lawrance.menu;

import io.lawrance.auth.ILogin;
import io.lawrance.auth.Authentication;
import io.lawrance.auth.LoggedInUser;
import io.lawrance.model.AccessLevel;
import io.lawrance.model.User;

import java.util.Scanner;

public class MainMenu implements IMenu {

    public void printOptions() {
        System.out.println("1. New User");
        System.out.println("2. User Login");
        System.out.println("3. Admin Login");
    }

    @Override
    public void show() {
        Scanner sc = new Scanner(System.in);
        boolean satisfied = false;
        ILogin login;

        while (!satisfied) {
            printOptions();
            System.out.print("Enter your option:   ");
            int userInput = sc.nextInt();
            System.out.println();
            switch (userInput) {
                case 1:
                    Authentication.signUp();
                    break;
                case 2:
                    login = Authentication.getLoginInstance(AccessLevel.USER);
                    satisfied = login.loginMechanism();
                    break;
                case 3:
                    login = Authentication.getLoginInstance(AccessLevel.ADMIN);
                    satisfied = login.loginMechanism();
                    break;
                default:
                    System.out.println("Invalid Option");
                    break;
            }
        }
        User loggedInUser = LoggedInUser.getLoggedInUser();
        loggedInUser.getMenu().show();
    }
}
