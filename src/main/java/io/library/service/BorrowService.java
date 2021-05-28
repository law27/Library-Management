package io.library.service;

import io.library.auth.LoggedInUser;
import io.library.dao.IBorrowBookDao;
import io.library.datasource.GlobalDataSource;
import io.library.model.BorrowedBook;
import io.library.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BorrowService {
    private static Scanner sc = null;

    private User user;
    private IBorrowBookDao borrowBookDao;

    private static BorrowService borrowService;

    private final String HEADINGS = "Name\t\t\tBookName\t\t\tBorrowedDate\t\t\tReturnDate\n" +
                                    "====\t\t\t========\t\t\t============\t\t\t==========";

    private BorrowService() {
        sc = Utility.getScanner();
        user = LoggedInUser.getLoggedInUser();
        borrowBookDao = GlobalDataSource.getDataSource().getBorrowBookDao();
    }

    public static BorrowService getInstance() {
        if(borrowService == null) {
            borrowService = new BorrowService();
        }
        return borrowService;
    }

    // For Mocking purpose
    public static BorrowService getInstance(User user, IBorrowBookDao borrowBookDao) {
        if(borrowService == null) {
            borrowService = new BorrowService();
        }
        borrowService.user = user;
        borrowService.borrowBookDao = borrowBookDao;
        return borrowService;
    }

    public void borrowABook() {
        System.out.print("Enter book id: ");
        String bookId = sc.nextLine();
        System.out.println();

        try {
            if(borrowBookDao.numberOfBookBorrowed(user.getUserName()) >= 2) {
                System.out.println("You've exceeded the amount of book you can borrow. Return a book before borrowing another book");
                return;
            }
            borrowBookDao.borrowABook(bookId, user.getUserName());
        }
        catch (SQLException exception) {
            System.out.println("Some SQL Error");
            exception.printStackTrace();
        }
    }

    public void listBorrowedBooks(List<BorrowedBook> books) {
        if (books.isEmpty()) {
            System.out.println("Haven't borrowed any books");
        } else {
            System.out.println(HEADINGS);
            int i = 1;
            for (BorrowedBook book : books) {
                System.out.println(i++ + ". "+ book);
            }
        }
    }

    public void listBorrowedBooks()  {
        try {
            var books = borrowBookDao.getAllBorrowedBook(user.getUserName());
            listBorrowedBooks(books);
            System.out.println();
        }
        catch (SQLException exception) {
            System.out.println("SQL error");
            exception.printStackTrace();
        }
    }


    public void returnABorrowedBook() {
        try {
            var books = borrowBookDao.getAllBorrowedBook(user.getUserName());
            boolean satisfied = false;
            while (!satisfied) {
                listBorrowedBooks(books);
                if(!books.isEmpty()) {
                    System.out.print("Select a book to return ");
                    int option = sc.nextInt();
                    System.out.println();
                    if (option <= 0 || option > books.size()) {
                        System.out.println("Invalid option try Again");
                    } else {
                        borrowBookDao.returnABook(books.get(option - 1).getBook().getId(), user.getUserName());
                        satisfied = true;
                    }
                }
                else {
                    satisfied = true;
                }
            }
        }
        catch (SQLException exception) {
            System.out.println("SQL error");
            exception.printStackTrace();
        }
    }
}
