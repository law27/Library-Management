package io.library.dao;

import io.library.model.BorrowedBook;

import java.sql.SQLException;
import java.util.List;

public interface IBorrowBookDao {
    List<BorrowedBook> getAllBorrowedBook(String userName) throws SQLException;
    int numberOfBookBorrowed(String userName) throws SQLException;
    void borrowABook(String bookId, String userName, String borrowDate, String returnDate) throws SQLException;
    void returnABook(String bookId, String userName) throws SQLException;
}
