package io.lawrance.service;

import io.lawrance.dao.BorrowBookDao;
import io.lawrance.dao.UserDao;

import java.util.Scanner;

public class UserService {
    Scanner sc;
    public UserService() {
        sc = new Scanner(System.in);
    }


    private void searchByName() {
        System.out.println("Enter user name");
        String userName = sc.nextLine();
        var user = UserDao.getUser(userName);
    }

    private void getAllBorrowTransactions() {
        System.out.println("Enter user name");
        String userName = sc.nextLine();
        var books = BorrowBookDao.getAllBorrowedBook(userName);
    }

    public void printOptions() {
        System.out.println("1. Search by User Name");
        System.out.println("2. See Users borrow Transactions");
        System.out.println("3. Main Menu");
    }

    public void searchOptions() {
        boolean satisfied = false;

        while (!satisfied) {
            printOptions();
            System.out.print("Enter your option:   ");
            int userInput = sc.nextInt();
            System.out.println();
            switch (userInput) {
                case 1:
                    this.searchByName();
                    satisfied = true;
                    break;
                case 2:
                    this.getAllBorrowTransactions();
                    satisfied = true;
                    break;
                case 3:
                    satisfied = true;
                    break;
                default:
                    System.out.println("Enter a valid option");
                    break;
            }
        }
    }
}
