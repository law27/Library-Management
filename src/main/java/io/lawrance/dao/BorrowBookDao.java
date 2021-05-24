package io.lawrance.dao;


import io.lawrance.datasource.DataSourceDatabase;
import io.lawrance.datasource.GlobalDataSource;
import io.lawrance.model.Book;
import io.lawrance.model.BorrowedBook;
import io.lawrance.service.Utility;
import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class BorrowBookDao implements IBorrowBookDao {

    public List<BorrowedBook> getAllBorrowedBook(String userName) throws SQLException {
        int userId = GlobalDataSource.getDataSource().getUserDao().getUserId(userName);
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String sql = String.format("SELECT users.name,\n" +
                "       books.*,\n" +
                "       borrowed_books.borrowed_date,\n" +
                "       borrowed_books.return_date\n" +
                "       FROM borrowed_books INNER JOIN books on borrowed_books.book_id = books.id INNER JOIN users on borrowed_books.user_id = users.id\n" +
                "       WHERE user_id=%d", userId);
        ResultSet resultSet = DataSourceDatabase.sqlExecutionerForSelect(sql);
        while (resultSet.next()) {
            String bookId = resultSet.getString("books.id");
            String bookName = resultSet.getString("books.book_name");
            String bookAuthor = resultSet.getString("books.book_author");
            String borrowedDate = resultSet.getString("borrowed_books.borrowed_date");
            String returnDate = resultSet.getString("borrowed_books.return_date");
            String name = resultSet.getString("users.name");
            String genre = resultSet.getString("books.genre");
            int quantity = resultSet.getInt("books.quantity");
            Book book = new Book(bookId, bookName, bookAuthor, quantity, genre);
            BorrowedBook bookDao = new BorrowedBook(book, borrowedDate, returnDate, name);
            borrowedBooks.add(bookDao);
        }
        return borrowedBooks;
    }

    public int numberOfBookBorrowed(String userName) throws SQLException {
        return getAllBorrowedBook(userName).size();
    }

    public void borrowABook(String bookId, String userName) {
        try {
            DateTime today = new DateTime();
            DateTime returnDate = new DateTime().plusDays(7);
            System.out.println(returnDate);
            int userId = GlobalDataSource.getDataSource().getUserDao().getUserId(userName);
            String sql = String.format("INSERT INTO borrowed_books(book_id, user_id, borrowed_date, return_date) " +
                            "VALUES(%s, %d, %s, %s)",
                            Utility.getFormattedString(bookId),
                            userId,
                            Utility.getFormattedString(today.toString()),
                            Utility.getFormattedString(returnDate.toString()));
            System.out.println(sql);
            DataSourceDatabase.sqlExecutionerForDML(sql);
            GlobalDataSource.getDataSource().getBookDao().decreaseQuantityOfBook(bookId, 1);
        }
        catch (SQLIntegrityConstraintViolationException exception) {
            System.out.println("Book doesn't exist");
        }
        catch (SQLException exception) {
            System.out.println("SQL Error");
            exception.printStackTrace();
        }
    }

    public void returnABook(String bookId, String userName) throws SQLException {
            int userId = GlobalDataSource.getDataSource().getUserDao().getUserId(userName);
            String sql = String.format("DELETE FROM borrowed_books WHERE user_id=%d AND book_id=%s",userId, Utility.getFormattedString(bookId));
            System.out.println(sql);
            DataSourceDatabase.sqlExecutionerForDML(sql);
            GlobalDataSource.getDataSource().getBookDao().increaseQuantityOfBook(bookId, 1);
    }
}
