package io.lawrance.menu;

import io.lawrance.service.BookService;
import io.lawrance.service.BorrowService;

import java.util.Scanner;

public class UserMenu implements IMenu {

    @Override
    public void printOptions() {
        System.out.println("1. Search Books");
        System.out.println("2. Borrow a Book");
        System.out.println("3. List Borrowed Book");
        System.out.println("4. Return a Book");
        System.out.println("5. Logout");
    }

    @Override
    public void show() {
        Scanner sc = new Scanner(System.in);
        boolean satisfied = false;

        while (!satisfied) {
            printOptions();
            System.out.print("Enter your option:   ");
            int userInput = sc.nextInt();
            System.out.println();
            switch (userInput) {
                case 1:
                    new BookService().searchOptions();
                    break;
                case 2:
                    new BorrowService().borrowABook();
                    break;
                case 3:
                    new BorrowService().listBorrowedBooks();
                    break;
                case 4:
                    new BorrowService().returnABorrowedBook();
                    break;
                case 5:
                    satisfied = true;
                    break;
                default:
                    System.out.println("Invalid Option");
                    break;
            }
        }
    }
}
