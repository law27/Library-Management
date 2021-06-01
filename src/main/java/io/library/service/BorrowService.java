package io.library.service;

import io.library.auth.LoggedInUser;
import io.library.dao.IBorrowBookDao;
import io.library.datasource.GlobalDataSource;
import io.library.model.BorrowedBook;
import io.library.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BorrowService {
    private final static Logger logger = LoggingService.getLogger(BorrowService.class);
    private static Scanner sc = null;

    private final User user;
    private final IBorrowBookDao borrowBookDao;
    private final BookService bookService;

    private static BorrowService borrowService;

    private final String HEADINGS = "Name\t\t\tBookName\t\t\tBorrowedDate\t\t\tReturnDate\n" +
            "====\t\t\t========\t\t\t============\t\t\t==========";

    private BorrowService() {
        sc = Utility.getScanner();
        user = LoggedInUser.getLoggedInUser();
        borrowBookDao = GlobalDataSource.getDataSource().getBorrowBookDao();
        bookService = BookService.getInstance();
    }

    private BorrowService(User user, IBorrowBookDao borrowBookDao, BookService bookService) {
        this.borrowBookDao = borrowBookDao;
        this.user = user;
        this.bookService = bookService;
    }

    public static BorrowService getInstance() {
        if (borrowService == null) {
            borrowService = new BorrowService();
        }
        return borrowService;
    }

    // For Mocking purpose
    public static BorrowService getInstance(User user, IBorrowBookDao borrowBookDao, BookService bookService) {
        if (borrowService == null) {
            borrowService = new BorrowService(user, borrowBookDao, bookService);
        }
        return borrowService;
    }

    protected boolean returnTheBook(String bookId) {
        boolean isSuccess = false;
        if(!bookId.equals("")) {
            try {
                borrowBookDao.returnABook(bookId, user.getUserName());

                logger.log(Level.INFO, "Book returned: " + bookId + " by: " + user.getUserName());

                isSuccess = true;
            } catch (Exception exception) {

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

            }

        }

        return isSuccess;
    }

    protected boolean borrowABook(String bookId, String userName) {
        boolean isSuccess = false;
        if (!bookId.equals("")) {
            try {
                int bookQuantity = bookService.getQuantityOfBook(bookId);
                if (bookQuantity == 0) {
                    System.out.println("No stock is left. Please come again later");
                    return false;
                } else {
                    borrowBookDao.borrowABook(bookId, userName);
                }

                logger.log(Level.INFO, "Book borrowed: " + bookId + " by: " + userName);

                isSuccess = true;
            } catch (Exception exception) {

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

            }
        }
        return isSuccess;
    }

    protected int getNumberOfBorrowedBookOfUser(String userName) {
        int count = 0;
        try {
            count = borrowBookDao.numberOfBookBorrowed(userName);
        } catch (SQLException exception) {

            logger.log(CustomLevel.ERROR, exception.toString(), exception);

        }
        return count;
    }

    protected List<BorrowedBook> getAllBorrowedBook(String userName) {
        List<BorrowedBook> borrowedBookList = null;
        try {
            borrowedBookList = borrowBookDao.getAllBorrowedBook(userName);
        } catch (SQLException exception) {

            logger.log(CustomLevel.ERROR, exception.toString(), exception);

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
        if(borrowABook(bookId, user.getUserName())) {
            System.out.println("Borrowed Successfully");
        }
        else {
            System.out.println("Borrow failed");
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
        if(books == null) {
            System.out.println("Some error occurred");
        }
        else {
            listBorrowedBooks(books);
        }
        System.out.println();
    }


    public void returnABorrowedBook() {
        var books = getAllBorrowedBook(user.getUserName());
        if (books == null) {
            System.out.println("Some error occurred");
        } else {
            boolean satisfied = false;
            while (!satisfied) {
                listBorrowedBooks(books);
                if (!books.isEmpty()) {
                    System.out.print("Select a book to return ");
                    int option;
                    try {
                        option = sc.nextInt();
                    } catch (Exception exception) {
                        sc.nextLine();

                        logger.log(CustomLevel.ERROR, exception.toString(), exception);

                        System.out.println("Option should be a number");
                        continue;
                    }
                    System.out.println();
                    if (option <= 0 || option > books.size()) {
                        System.out.println("Invalid option try Again");
                    } else {
                        if(returnTheBook(books.get(option - 1).getBook().getId())) {
                            System.out.println("Returned Successfully");
                        }
                        else {
                            System.out.println("Return failed. Try Later");
                        }
                        satisfied = true;
                    }
                } else {
                    satisfied = true;
                }
            }

        }

    }
}