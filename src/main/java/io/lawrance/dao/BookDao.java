package io.lawrance.dao;

import io.lawrance.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookDao {

    public static List<Book> getBookByAuthor(String author) {
        return new ArrayList<>();
    }

    public static Book getBookByName(String name) {
        return null;
    }

    public static Book getBookById(String id) {
        return null;
    }

    public static void increaseQuantityOfBook(String bookId, int quantity) {

    }

    public static void decreaseQuantityOfBook(String bookId, int quantity) {

    }

    public static void addBook(Book book) {

    }
}
