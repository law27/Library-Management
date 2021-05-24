package io.lawrance.menu;

import io.lawrance.service.BookService;
import io.lawrance.service.UserService;

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
        Scanner sc = new Scanner(System.in);
        boolean satisfied = false;

        while (!satisfied) {
            printOptions();
            System.out.print("Enter your option:  ");
            int userInput = sc.nextInt();
            System.out.println();
            switch (userInput) {
                case 1:
                    new BookService().searchOptions();
                    break;
                case 2:
                    new UserService().searchOptions();
                    break;
                case 3:
                    new BookService().addBook();
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
