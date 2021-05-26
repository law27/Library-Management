package io.library.datasource;

import io.library.dao.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class DataSourceJSON implements IDataSource {
    private volatile static DataSourceJSON dataSourceJSON = null;
    private final IUserDao userDao;
    private final IBookDao bookDao;
    private final IBorrowBookDao borrowBookDao;
    private File books;
    private File users;
    private File borrow;
    private JSONArray bookJsonArray = null;
    private JSONArray userJsonArray = null;
    private JSONArray borrowJsonArray = null;

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

    public void createConnection(File books, File users, File borrow) throws FileNotFoundException {
        if(books.exists() && users.exists() && borrow.exists()) {
            this.books = books;
            this.users = users;
            this.borrow = borrow;
        }
        else {
            throw new FileNotFoundException("Required files not found");
        }
    }

    public synchronized static DataSourceJSON getInstance() {
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
        if(bookJsonArray == null) {
            String jsonString = readFromFile(books);
            bookJsonArray = new JSONArray(jsonString);
        }
        return bookJsonArray;
    }

    public JSONArray readUsers() {
        if(userJsonArray == null) {
            String jsonString = readFromFile(users);
            userJsonArray = new JSONArray(jsonString);
        }
        return userJsonArray;
    }

    public JSONArray readBorrow() {
        if(borrowJsonArray == null) {
            String jsonString = readFromFile(borrow);
            borrowJsonArray = new JSONArray(jsonString);
        }
        return userJsonArray;
    }

    public synchronized void writeBook(JSONObject book) {
        JSONArray booksArray = readBooks();
        booksArray.put(book);
    }

    public synchronized void writeUser(JSONObject userObject) {
        JSONArray usersArray = readUsers();
        usersArray.put(userObject);
    }

    public synchronized void writeBorrow(JSONObject borrowObject) {
        JSONArray borrowsArray = readBorrow();
        borrowsArray.put(borrowObject);
    }

    public void writeBooks(JSONArray newBooks) {
        writeToFile(newBooks, books);
    }

    public void writeBorrows(JSONArray borrowedBooks) {
        writeToFile(borrowedBooks, borrow);
    }

    public void writeUsers(JSONArray newUsers) {
        writeToFile(newUsers, users);
    }
}
