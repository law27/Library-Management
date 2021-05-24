package io.lawrance.dao;

import io.lawrance.datasource.DataSourceJSON;
import io.lawrance.datasource.GlobalDataSource;
import io.lawrance.model.Book;
import io.lawrance.model.BorrowedBook;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowBookDaoJSON implements IBorrowBookDao {
    @Override
    public List<BorrowedBook> getAllBorrowedBook(String userName) throws SQLException {
        IBookDao bookDao = GlobalDataSource.getDataSource().getBookDao();
        IUserDao userDao = GlobalDataSource.getDataSource().getUserDao();
        JSONArray borrowedBooks = DataSourceJSON.getInstance().readBorrow();
        int userId = userDao.getUserId(userName);
        List<BorrowedBook> borrowedBookList = new ArrayList<>();

        for(var borrowedBook : borrowedBooks) {

            JSONObject object = (JSONObject) borrowedBook;

            if(object.getInt("user_id") == userId) {
                Book book = bookDao.getBookById(object.getString("book_id"));
                String returnDate = object.getString("return_date");
                String borrowedDate = object.getString("borrowed_date");
                borrowedBookList.add(new BorrowedBook(book, borrowedDate,returnDate, userName));
            }

        }
        return borrowedBookList;
    }

    @Override
    public int numberOfBookBorrowed(String userName) {
        JSONArray borrowedBooks = DataSourceJSON.getInstance().readBorrow();
        return borrowedBooks.length();
    }

    @Override
    public void borrowABook(String bookId, String userName) throws SQLException {
        IBookDao bookDao = GlobalDataSource.getDataSource().getBookDao();
        IUserDao userDao = GlobalDataSource.getDataSource().getUserDao();
        Book toBeBorrowed =  bookDao.getBookById(bookId);
        int userId = userDao.getUserId(userName);
        if(toBeBorrowed == null) {
            System.out.println("Book doesn't exist");
        }
        else {
            DateTime today = new DateTime();
            DateTime returnDate = new DateTime().plusDays(7);
            JSONObject object = new JSONObject();
            object.put("book_id", bookId);
            object.put("user_id", userId);
            object.put("borrowed_date", today);
            object.put("return_date", returnDate);
            DataSourceJSON.getInstance().writeBorrow(object);
        }
        bookDao.decreaseQuantityOfBook(bookId, 1);
    }

    @Override
    public void returnABook(String bookId, String userName) throws SQLException {
        IBookDao bookDao = GlobalDataSource.getDataSource().getBookDao();
        int userId = GlobalDataSource.getDataSource().getUserDao().getUserId(userName);
        int index = -1;
        JSONArray borrowedBooks = DataSourceJSON.getInstance().readBorrow();
        for(int i = 0; i < borrowedBooks.length(); i++) {
            JSONObject object = (JSONObject) borrowedBooks.get(i);
            if(object.getInt("user_id") == userId &&
                    object.getString("book_id").equals(bookId)) {
                index = i;
                break;
            }
        }
        if(index == -1) {
            System.out.println("You've not borrowed this book");
            return;
        }
        else {
            borrowedBooks.remove(index);
            DataSourceJSON.getInstance().writeBorrows(borrowedBooks);
        }
        bookDao.increaseQuantityOfBook(bookId, 1);
    }
}
