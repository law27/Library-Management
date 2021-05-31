package io.library.service;

import io.library.auth.LoggedInUser;
import io.library.dao.IBorrowBookDao;
import io.library.datasource.GlobalDataSource;
import io.library.model.BorrowedBook;
import io.library.model.User;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
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

    private BorrowService(User user, IBorrowBookDao borrowBookDao, InputStream stream) {
        sc = new Scanner(stream);
        this.borrowBookDao = borrowBookDao;
        this.user = user;
    }

    public static BorrowService getInstance() {
        if (borrowService == null) {
            borrowService = new BorrowService();
        }
        return borrowService;
    }

    // For Mocking purpose
    public static BorrowService getInstance(User user, IBorrowBookDao borrowBookDao, InputStream stream) {
        if (borrowService == null) {
            borrowService = new BorrowService(user, borrowBookDao, stream);
        } else {
            borrowService.user = user;
            borrowService.borrowBookDao = borrowBookDao;
            sc = new Scanner(stream);
        }
        return borrowService;
    }

    private boolean returnTheBook(String bookId) {
        boolean isSuccess = true;
        try {
            borrowBookDao.returnABook(bookId, user.getUserName());
        } catch (SQLException | IllegalStateException exception) {
            isSuccess = false;
            System.out.println(exception.getMessage());
        }
        return isSuccess;
    }

    private boolean borrowABook(String bookId, String userName) {
        boolean isSuccess = true;
        try {
            int bookQuantity = BookService.getInstance().getQuantityOfBook(bookId);
            if(bookQuantity == 0) {
                System.out.println("No stock is left. Please come again later");
                isSuccess = false;
            }
            else {
                borrowBookDao.borrowABook(bookId, userName);
            }
        } catch (SQLException | NoSuchElementException exception) {
            isSuccess = false;
            System.out.println(exception.getMessage());
        }
        return isSuccess;
    }

    private int getNumberOfBorrowedBookOfUser(String userName) {
        int count = 0;
        try {
            count = borrowBookDao.numberOfBookBorrowed(userName);
        } catch (SQLException exception) {
            System.out.println("SQL Exception");
            System.out.println(exception.getMessage());
        }
        return count;
    }

    private List<BorrowedBook> getAllBorrowedBook(String userName) {
        List<BorrowedBook> borrowedBookList = null;
        try {
            borrowedBookList = borrowBookDao.getAllBorrowedBook(userName);
        } catch (SQLException exception) {
            System.out.println("SQL Exception");
            System.out.println(exception.getMessage());
        }
        return borrowedBookList;
    }


    public void borrowABook() {
        System.out.print("Enter book id: ");
        String bookId = sc.nextLine();
        System.out.println();
        int numberOfBookBorrowed = getNumberOfBorrowedBookOfUser(user.getUserName());
        if (numberOfBookBorrowed >= 2) {
            System.out.println("You've exceeded the amount of book you can borrow. Return a book before borrowing another book");
            return;
        }
        if (borrowABook(bookId, user.getUserName())) {
            System.out.println("Book borrowed successfully");
        } else {
            System.out.println("Some error occurred. Please try Later");
        }
    }

    public void listBorrowedBooks(List<BorrowedBook> books) {
        if (books.isEmpty()) {
            System.out.println("Haven't borrowed any books");
        } else {
            System.out.println(HEADINGS);
            int i = 1;
            for (BorrowedBook book : books) {
                System.out.println(i++ + ". " + book);
            }
        }
    }

    public void listBorrowedBooks() {
        var books = getAllBorrowedBook(user.getUserName());
        listBorrowedBooks(books);
        System.out.println();
    }


    public void returnABorrowedBook() {
        var books = getAllBorrowedBook(user.getUserName());
        boolean satisfied = false;
        boolean isSuccess;
        while (!satisfied) {
            listBorrowedBooks(books);
            if (!books.isEmpty()) {
                System.out.print("Select a book to return ");
                int option = sc.nextInt();
                System.out.println();
                if (option <= 0 || option > books.size()) {
                    System.out.println("Invalid option try Again");
                } else {
                    isSuccess =  returnTheBook(books.get(option - 1).getBook().getId());
                    if(isSuccess) {
                        System.out.println("Returned Successfully");
                    }
                    satisfied = true;
                }
            } else {
                satisfied = true;
            }
        }
    }
}