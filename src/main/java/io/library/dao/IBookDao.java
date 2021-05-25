package io.library.dao;

import io.library.model.Book;

import java.sql.SQLException;
import java.util.List;

public interface IBookDao {
    List<Book> getBookByAuthor(String author) throws SQLException;
    Book getBookByName(String bookName) throws SQLException;
    Book getBookById(String bookId) throws SQLException;
    void increaseQuantityOfBook(String bookId, int quantity) throws SQLException;
    void decreaseQuantityOfBook(String bookId, int quantity) throws SQLException;
    void addBook(Book book) throws SQLException;
    List<Book> getBookByGenre(String genre) throws SQLException;
}
