package io.lawrance.datasource;

import io.lawrance.dao.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class DataSourceJSON implements IDataSource {
    private static DataSourceJSON dataSourceJSON = null;
    private final IUserDao userDao;
    private final IBookDao bookDao;
    private final IBorrowBookDao borrowBookDao;
    private File books;
    private File users;
    private File borrow;

    private DataSourceJSON() {
        this.userDao = new UserDaoJSON();
        this.bookDao = new BookDaoJSON();
        this.borrowBookDao = new BorrowBookDaoJSON();
    }

    @Override
    public IUserDao getUserDao() {
        return userDao;
    }

    @Override
    public IBookDao getBookDao() {
        return bookDao;
    }

    @Override
    public IBorrowBookDao getBorrowBookDao() {
        return borrowBookDao;
    }

    public void createConnection(File books, File users, File borrow) {
        this.books = books;
        this.users = users;
        this.borrow = borrow;
    }

    public static DataSourceJSON getInstance() {
        if(dataSourceJSON == null) {
            dataSourceJSON = new DataSourceJSON();
        }
        return dataSourceJSON;
    }

    private synchronized String readFromFile(File file) {
        String result = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            result = new String(inputStream.readAllBytes());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    private synchronized void writeToFile(JSONArray array, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(array.toString(4));
            writer.flush();
            fileWriter.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public JSONArray readBooks() {
        String jsonString = readFromFile(books);
        return new JSONArray(jsonString);
    }

    public JSONArray readUsers() {
        String jsonString = readFromFile(users);
        return new JSONArray(jsonString);
    }

    public JSONArray readBorrow() {
        String jsonString = readFromFile(borrow);
        return new JSONArray(jsonString);
    }


    public void writeBook(JSONObject book) {
        JSONArray booksArray = readBooks();
        booksArray.put(book);
        writeToFile(booksArray, books);
    }

    public void writeUser(JSONObject userObject) {
        JSONArray usersArray = readUsers();
        usersArray.put(userObject);
        writeToFile(usersArray, users);
    }

    public void writeBorrow(JSONObject borrowObject) {
        JSONArray borrowsArray = readBorrow();
        borrowsArray.put(borrowObject);
        writeToFile(borrowsArray, borrow);
    }

    public void writeBooks(JSONArray newBooks) {
        writeToFile(newBooks, books);
    }

    public void writeBorrows(JSONArray borrowedBooks) {
        writeToFile(borrowedBooks, borrow);
    }
}
