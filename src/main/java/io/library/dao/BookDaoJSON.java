package io.library.dao;

import io.library.datasource.DataSourceJSON;
import io.library.model.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookDaoJSON implements IBookDao {

    private Book makeBook(JSONObject book) {
        String name = book.getString("book_name");
        String id = book.getString("id");
        String authorName = book.getString("book_author");
        String genre = book.getString("genre");
        int quantity = book.getInt("quantity");
        return new Book(id, name, authorName, quantity, genre);
    }

    @Override
    public List<Book> getBookByAuthor(String author) {
        JSONArray books = DataSourceJSON.getInstance().readBooks();
        List<Book> bookList = new ArrayList<>();
        for(var book : books) {
            JSONObject object = (JSONObject) book;
            if(object.getString("book_author").equals(author)) {
                bookList.add(makeBook(object));
            }
        }
        return bookList;
    }

    @Override
    public Book getBookByName(String bookName) {
        JSONArray books = DataSourceJSON.getInstance().readBooks();
        Book resultBook = null;
        for(var book : books) {
            JSONObject object = (JSONObject) book;
            if(object.getString("book_name").equals(bookName)) {
                resultBook = makeBook(object);
                break;
            }
        }
        return resultBook;
    }

    @Override
    public Book getBookById(String bookId) {
        JSONArray books = DataSourceJSON.getInstance().readBooks();
        Book resultBook = null;
        for(var book : books) {
            JSONObject object = (JSONObject) book;
            if(object.getString("id").equals(bookId)) {
                resultBook = makeBook(object);
                break;
            }
        }
        return resultBook;
    }

    @Override
    public void increaseQuantityOfBook(String bookId, int quantity) {
        JSONArray books = DataSourceJSON.getInstance().readBooks();
        for(var book : books) {
            JSONObject object = (JSONObject) book;
            if(object.getString("id").equals(bookId)) {
                int prevQuantity = object.getInt("quantity");
                object.put("quantity", prevQuantity + quantity);
                break;
            }
        }
        DataSourceJSON.getInstance().writeBooks(books);
    }

    @Override
    public void decreaseQuantityOfBook(String bookId, int quantity) {
        JSONArray books = DataSourceJSON.getInstance().readBooks();
        for(var book : books) {
            JSONObject object = (JSONObject) book;
            if(object.getString("id").equals(bookId)) {
                int prevQuantity = object.getInt("quantity");
                object.put("quantity", prevQuantity - quantity);
                break;
            }
        }
        DataSourceJSON.getInstance().writeBooks(books);
    }

    @Override
    public void addBook(Book book) {
        JSONObject newBook = new JSONObject();
        newBook.put("id", book.getId());
        newBook.put("book_name", book.getBookName());
        newBook.put("book_author", book.getAuthor());
        newBook.put("quantity", book.getQuantity());
        newBook.put("genre", book.getGenre());
        DataSourceJSON.getInstance().writeBook(newBook);
    }

    @Override
    public List<Book> getBookByGenre(String genre) {
        JSONArray books = DataSourceJSON.getInstance().readBooks();
        List<Book> bookList = new ArrayList<>();
        for(var book : books) {
            JSONObject object = (JSONObject) book;
            if(object.getString("genre").equals(genre)) {
                bookList.add(makeBook(object));
            }
        }
        return bookList;
    }
}
