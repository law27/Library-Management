package io.library.datasource;

import io.library.dao.*;
import io.library.service.LoggingService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSourceJSON implements IDataSource {
    private static final Logger logger = LoggingService.getLogger(DataSourceJSON.class);
    private volatile static DataSourceJSON dataSourceJSON = null;
    private final IUserDao userDao;
    private final IBookDao bookDao;
    private final IBorrowBookDao borrowBookDao;
    private File books;
    private File users;
    private File borrow;
    private JSONArray userJsonArray = null;
    private JSONArray borrowJsonArray = null;
    private JSONArray bookJsonArray = null;

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
            logger.log(Level.INFO, "Files loaded");
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
        try(InputStream inputStream = new FileInputStream(file)) {
            result = new String(inputStream.readAllBytes());

            logger.log(Level.INFO, "Reading from " + file.getName() );

        }
        catch (IOException exception) {

            logger.log(Level.SEVERE, exception.toString(), exception);

        }
        return result;
    }

    private synchronized void writeToFile(JSONArray array, File file) {
        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter) ) {
            writer.write(array.toString(4));

            logger.log(Level.INFO, "Writing to " + file.getName());

        }
        catch (Exception exception) {

            logger.log(Level.SEVERE, exception.toString(), exception);

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
        return borrowJsonArray;
    }

    public synchronized void writeBook(JSONObject book) {
        JSONArray booksArray = readBooks();
        booksArray.put(book);
        writeBooks(booksArray);
    }

    public synchronized void writeUser(JSONObject userObject) {
        JSONArray usersArray = readUsers();
        usersArray.put(userObject);
        writeUsers(usersArray);
    }

    public synchronized void writeBorrow(JSONObject borrowObject) {
        JSONArray borrowsArray = readBorrow();
        borrowsArray.put(borrowObject);
        writeBorrows(borrowsArray);
    }

    public synchronized void writeBooks(JSONArray newBooks) {
        bookJsonArray = newBooks;
        writeToFile(bookJsonArray, books);
    }

    public synchronized void writeBorrows(JSONArray newBorrowedBooks) {
        borrowJsonArray = newBorrowedBooks;
        writeToFile(borrowJsonArray, borrow);
    }

    public synchronized void writeUsers(JSONArray newUsers) {
        userJsonArray = newUsers;
        writeToFile(userJsonArray, users);
    }
}
