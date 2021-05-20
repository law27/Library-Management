package io.lawrance.service;

import io.lawrance.auth.LoggedInUser;
import io.lawrance.dao.BorrowBookDao;
import io.lawrance.model.Book;
import io.lawrance.model.User;

import java.util.List;
import java.util.Scanner;

public class BorrowService {
    Scanner sc;
    User user;

    public BorrowService() {
        sc = new Scanner(System.in);
        user = LoggedInUser.getLoggedInUser();
    }

    public void borrowABook() {
        System.out.print("Enter book id: ");
        String bookId = sc.nextLine();
        System.out.println();

        if(BorrowBookDao.numberOfBookBorrowed(user.getUserName()) >= 2) {
            System.out.println("You've exceeded the amount of book you can borrow. Return a book before borrowing another book");
            return;
        }
        BorrowBookDao.borrowABook(bookId, user.getUserName());
    }

    public void listBorrowedBooks(List<Book> books) {

    }

    public void listBorrowedBooks() {
        var books = BorrowBookDao.getAllBorrowedBook(user.getUserName());
        listBorrowedBooks(books);
    }


    public void returnABorrowedBook() {
        var books = BorrowBookDao.getAllBorrowedBook(user.getUserName());
        listBorrowedBooks(books);
        boolean satisfied = false;
        while (!satisfied) {
            System.out.print("Select a book to return ");
            int option = sc.nextInt();
            if(option <= 0 || option >= books.size()) {
                System.out.println("Invalid option try Again");
            }
            else {
                BorrowBookDao.returnABook(books.get(option - 1).getId(), user.getUserName());
                satisfied = true;
            }
        }
    }

}
