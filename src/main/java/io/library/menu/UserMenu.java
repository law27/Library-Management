package io.library.menu;

import io.library.service.BookService;
import io.library.service.BorrowService;
import io.library.service.Utility;

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
        Scanner sc = Utility.getScanner();
        boolean satisfied = false;

        while (!satisfied) {
            printOptions();
            System.out.print("Enter your option:   ");
            int userInput;
            try {
                userInput = sc.nextInt();
            }
            catch (Exception exception) {
                sc.nextLine();
                System.out.println("Enter a valid Number\n");
                continue;
            }
            sc.nextLine();
            System.out.println();
            switch (userInput) {
                case 1:
                    BookService.getInstance().searchOptions();
                    break;
                case 2:
                    BorrowService.getInstance().borrowABook();
                    break;
                case 3:
                    BorrowService.getInstance().listBorrowedBooks();
                    break;
                case 4:
                    BorrowService.getInstance().returnABorrowedBook();
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
