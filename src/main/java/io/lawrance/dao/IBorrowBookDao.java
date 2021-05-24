package io.lawrance.dao;

import io.lawrance.model.BorrowedBook;

import java.sql.SQLException;
import java.util.List;

public interface IBorrowBookDao {
    List<BorrowedBook> getAllBorrowedBook(String userName) throws SQLException;
    int numberOfBookBorrowed(String userName) throws SQLException;
    void borrowABook(String bookId, String userName) throws SQLException;
    void returnABook(String bookId, String userName) throws SQLException;
}
