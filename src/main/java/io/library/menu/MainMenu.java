package io.library.menu;

import io.library.auth.ILogin;
import io.library.auth.Authentication;
import io.library.auth.LoggedInUser;
import io.library.model.AccessLevel;
import io.library.model.User;
import io.library.service.Utility;

import java.util.Scanner;

public class MainMenu implements IMenu {

    public void printOptions() {
        System.out.println("1. New User");
        System.out.println("2. User Login");
        System.out.println("3. Admin Login");
        System.out.println("4. Exit");
    }

    @Override
    public void show() {
        Scanner sc = Utility.getScanner();
        boolean satisfied = false;
        ILogin login;

        while (!satisfied) {
            printOptions();
            System.out.print("Enter your option:   ");
            int userInput;
            try {
                userInput = sc.nextInt();
            }
            catch (Exception exception) {
                sc.nextLine();
                System.out.println("Enter a valid Number");
                System.out.println();
                continue;
            }
            System.out.println();
            sc.nextLine();
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
                case 4:
                    return;
                default:
                    System.out.println("Invalid Option");
                    break;
            }
        }
        User loggedInUser = LoggedInUser.getLoggedInUser();
        loggedInUser.getMenu().show();
    }
}
