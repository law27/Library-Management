package io.library.service;

import io.library.dao.IBorrowBookDao;
import io.library.dao.IUserDao;
import io.library.datasource.GlobalDataSource;
import io.library.model.User;

import java.sql.SQLException;
import java.util.Scanner;

public class UserService {
    private static Scanner sc = null;

    private IUserDao userDao;
    private IBorrowBookDao borrowBookDao;
    private final String HEADINGS = "Username\t\tMobile Number\t\t\tAge\n" +
                                    "====\t\t\t========\t\t\t============";

    private static UserService userService;

    private UserService() {
        sc = Utility.getScanner();
        userDao = GlobalDataSource.getDataSource().getUserDao();
        borrowBookDao = GlobalDataSource.getDataSource().getBorrowBookDao();
    }

    public static UserService getInstance() {
        if(userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    // For mocking purpose
    protected static UserService getInstance(IUserDao userDao, IBorrowBookDao borrowBookDao) {
        if(userService == null) {
            userService = new UserService();
        }
        userService.userDao = userDao;
        userService.borrowBookDao = borrowBookDao;
        return userService;
    }

    private void printUser(User user) {
        if(user == null) {
            System.out.println("User doesn't exist");
        }
        else {
            System.out.println(HEADINGS);
            System.out.println(user);
            System.out.println();
        }
    }

    private void searchByName() {
        System.out.println("Enter user name");
        String userName = sc.nextLine();
        try {
            var user = userDao.getUser(userName);
            printUser(user);
        }
        catch (SQLException exception) {
            System.out.println("SQL Error");
            exception.printStackTrace();
        }
    }

    private void getAllBorrowTransactions() {
        System.out.println("Enter user name");
        String userName = sc.nextLine();
        try {
            var books = borrowBookDao.getAllBorrowedBook(userName);
            BorrowService.getInstance().listBorrowedBooks(books);
            System.out.println();
        }
        catch (SQLException exception) {
            System.out.println("SQL Error");
            exception.printStackTrace();
        }
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
            sc.nextLine();
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
