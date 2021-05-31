package io.library.model;

public class BorrowedBook {
    Book book;
    String userName;
    String borrowedDate;
    String returnDate;

    public BorrowedBook(Book book, String borrowedDate, String returnDate, String userName) {
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.returnDate = returnDate;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return String.format("%s\t\t%s\t\t\t\t%s\t\t\t%s",
                userName,
                book.getBookName(),
                borrowedDate,
                returnDate
        );
    }
}
