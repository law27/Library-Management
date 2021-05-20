package io.lawrance.model;

import java.util.UUID;

public class Book {
    private final String id;
    private String bookName;
    private String author;
    private int quantity;

    public Book(String bookName, String author, int quantity) {
        this.id = UUID.randomUUID().toString();
        this.bookName = bookName;
        this.author = author;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
