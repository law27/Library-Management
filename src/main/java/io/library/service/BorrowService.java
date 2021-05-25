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
    Scanner sc;
    User user;
    IBorrowBookDao borrowBookDao;

    public BorrowService() {
        sc = new Scanner(System.in);
        user = LoggedInUser.getLoggedInUser();
        borrowBookDao = GlobalDataSource.getDataSource().getBorrowBookDao();
    }

    public BorrowService(User user, IBorrowBookDao bookDao) {
        sc = new Scanner(System.in);
        this.user = user;
        this.borrowBookDao = bookDao;
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

    private static void printHeadings() {
        System.out.println("Name\t\t\tBookName\t\t\tBorrowedDate\t\t\tReturnDate");
        System.out.println("====\t\t\t========\t\t\t============\t\t\t==========");
    }

    public static void listBorrowedBooks(List<BorrowedBook> books) {
        if(books.isEmpty()) {
            System.out.println("Haven't borrowed any books");
        }
        else {
            printHeadings();
            for (BorrowedBook book : books) {
                System.out.printf("%s\t\t%s\t\t\t\t%s\t\t\t%s",
                        book.getUserName(),
                        book.getBook().getBookName(),
                        book.getBorrowedDate(),
                        book.getReturnDate()
                );
                System.out.println();
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
        }
        catch (SQLException exception) {
            System.out.println("SQL error");
            exception.printStackTrace();
        }
    }
}
