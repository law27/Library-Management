package io.library.service;

import io.library.dao.IBorrowBookDao;
import io.library.dao.IUserDao;
import io.library.datasource.GlobalDataSource;
import io.library.model.BorrowedBook;
import io.library.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class UserService {
    private static Scanner sc = null;
    private final static Logger logger = LoggingService.getLogger(UserService.class);

    private final IUserDao userDao;
    private final IBorrowBookDao borrowBookDao;
    private final String HEADINGS = "Username\t\tMobile Number\t\t\tAge\n" +
                                    "====\t\t\t========\t\t\t============";

    private static UserService userService;

    private UserService() {
        sc = Utility.getScanner();
        userDao = GlobalDataSource.getDataSource().getUserDao();
        borrowBookDao = GlobalDataSource.getDataSource().getBorrowBookDao();
    }

    private UserService(IUserDao userDao, IBorrowBookDao borrowBookDao) {
        this.userDao = userDao;
        this.borrowBookDao = borrowBookDao;
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
            userService = new UserService(userDao, borrowBookDao);
        }
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

    protected User getUserByUserName(String userName) {
        User user = null;
        if(!userName.equals("")) {
            try {
                user = userDao.getUser(userName);
            } catch (SQLException exception) {

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

            }
        }

        return user;
    }

    private void searchByName() {
        System.out.println("Enter user name");
        String userName = sc.nextLine();
        var user = getUserByUserName(userName);
        printUser(user);
    }

    private List<BorrowedBook> getAllBorrowedBooksOfUser(String userName) {
        List<BorrowedBook> borrowedBookList = null;
        try {
            borrowedBookList = borrowBookDao.getAllBorrowedBook(userName);
        }
        catch (SQLException exception) {

            logger.log(CustomLevel.ERROR, exception.toString(), exception);

        }
        return borrowedBookList;
    }

    private void getAllBorrowTransactions() {
        System.out.println("Enter user name");
        String userName = sc.nextLine();
        var books = getAllBorrowedBooksOfUser(userName);
        BorrowService.getInstance().listBorrowedBooks(books);
        System.out.println();
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
            int userInput;
            try {
                userInput = sc.nextInt();
            }
            catch (Exception exception) {
                sc.nextLine();

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

                System.out.println("Option should be number");
                continue;
            }
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
