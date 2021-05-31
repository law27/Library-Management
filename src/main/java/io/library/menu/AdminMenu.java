package io.library.menu;

import io.library.service.BookService;
import io.library.service.UserService;
import io.library.service.Utility;

import java.util.Scanner;

public class AdminMenu implements IMenu {

    @Override
    public void printOptions() {
        System.out.println("1. Search Books");
        System.out.println("2. Search User");
        System.out.println("3. Add a new book");
        System.out.println("4. Logout");
    }

    @Override
    public void show() {
        Scanner sc = Utility.getScanner();
        boolean satisfied = false;

        while (!satisfied) {
            printOptions();
            System.out.print("Enter your option:  ");
            int userInput = sc.nextInt();
            sc.nextLine();
            System.out.println();
            switch (userInput) {
                case 1:
                    BookService.getInstance().searchOptions();
                    break;
                case 2:
                    UserService.getInstance().searchOptions();
                    break;
                case 3:
                    BookService.getInstance().addBook();
                    break;
                case 4:
                    satisfied = true;
                    break;
                default:
                    System.out.println("Invalid Option");
                    break;
            }
        }
    }
}
