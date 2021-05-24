package io.lawrance.dao;

import io.lawrance.datasource.DataSourceDatabase;
import io.lawrance.model.Book;
import io.lawrance.service.Utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDao implements IBookDao {

    public List<Book> getBookByAuthor(String author) throws SQLException {
        String sql = String.format("SELECT * FROM books WHERE book_author=%s", Utility.getFormattedString(author));
        List<Book> books = new ArrayList<>();
        ResultSet resultSet = DataSourceDatabase.sqlExecutionerForSelect(sql);
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("book_name");
            String authorName = resultSet.getString("book_author");
            int quantity = resultSet.getInt("quantity");
            String genre = resultSet.getString("genre");
            Book book = new Book(id, name, authorName, quantity, genre);
            books.add(book);
        }
        return books;
    }

    public Book getBookByName(String bookName) throws SQLException {
        String sql = String.format("SELECT * FROM books WHERE book_name=%s", Utility.getFormattedString(bookName));
        ResultSet resultSet = DataSourceDatabase.sqlExecutionerForSelect(sql);
        Book book = null;
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("book_name");
            String author = resultSet.getString("book_author");
            int quantity = resultSet.getInt("quantity");
            String genre = resultSet.getString("genre");
            book = new Book(id, name, author, quantity, genre);
            break;
        }
        return book;
    }

    public Book getBookById(String bookId) throws SQLException {
        String sql = String.format("SELECT * FROM books WHERE id=%s", Utility.getFormattedString(bookId));
        ResultSet resultSet = DataSourceDatabase.sqlExecutionerForSelect(sql);
        Book book = null;
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("book_name");
            String author = resultSet.getString("book_author");
            int quantity = resultSet.getInt("quantity");
            String genre = resultSet.getString("genre");
            book = new Book(id, name, author, quantity, genre);
            break;
        }
        return book;
    }

    public void increaseQuantityOfBook(String bookId, int quantity) throws SQLException {
        String sql = String.format("UPDATE books SET quantity=" +
                        "(SELECT quantity FROM (SELECT quantity FROM books WHERE id=%s) as T)+%d WHERE id=%s",
                Utility.getFormattedString(bookId),
                quantity, Utility.getFormattedString(bookId));
        System.out.println(sql);
        DataSourceDatabase.sqlExecutionerForDML(sql);
    }

    public void decreaseQuantityOfBook(String bookId, int quantity) throws SQLException {
        String sql = String.format("UPDATE books SET quantity=" +
                        "(SELECT quantity FROM (SELECT quantity FROM books WHERE id=%s) as T)-%d WHERE id=%s",
                        Utility.getFormattedString(bookId),
                        quantity, Utility.getFormattedString(bookId));
        System.out.println(sql);
        DataSourceDatabase.sqlExecutionerForDML(sql);
    }

    public void addBook(Book book) throws SQLException {
        String sql = String.format("INSERT INTO books(id, book_name, book_author, quantity, genre) VALUES(%s, %s, %s, %d, %s)",
                Utility.getFormattedString(book.getId()),
                Utility.getFormattedString(book.getBookName()),
                Utility.getFormattedString(book.getAuthor()),
                book.getQuantity(),
                Utility.getFormattedString(book.getGenre())
        );
        System.out.println(sql);
        DataSourceDatabase.sqlExecutionerForDML(sql);
        System.out.println("Successfully Added");
    }

    public List<Book> getBookByGenre(String genre) throws SQLException {
        String sql = String.format("SELECT * FROM books WHERE genre=%s", Utility.getFormattedString(genre));
        List<Book> books = new ArrayList<>();
        ResultSet resultSet = DataSourceDatabase.sqlExecutionerForSelect(sql);
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("book_name");
            String authorName = resultSet.getString("book_author");
            int quantity = resultSet.getInt("quantity");
            Book book = new Book(id, name, authorName, quantity, genre);
            books.add(book);
        }
        return books;
    }
}
