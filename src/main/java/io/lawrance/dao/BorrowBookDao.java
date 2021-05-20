package io.lawrance.dao;


import io.lawrance.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BorrowBookDao {

    public static List<Book> getAllBorrowedBook(String userName) {
        return new ArrayList<>();
    }

    public static int numberOfBookBorrowed(String userName) {
        return getAllBorrowedBook(userName).size();
    }

    public static void borrowABook(String bookId, String userName) {
        BookDao.decreaseQuantityOfBook(bookId, 1);
    }

    public static void returnABook(String bookId, String userName) {
        BookDao.increaseQuantityOfBook(bookId, 1);
    }
}
