package io.library.dao;

import io.library.datasource.DataSourceDatabase;
import io.library.datasource.GlobalDataSource;
import io.library.menu.UserMenu;
import io.library.model.AccessLevel;
import io.library.model.Book;
import io.library.model.BorrowedBook;
import io.library.model.User;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class BorrowBookDaoTest {

    private final BookDao bookDao;
    private final BorrowBookDao borrowBookDao;
    private final UserDao userDao;

    public BorrowBookDaoTest() {
        this.bookDao = new BookDao();
        this.borrowBookDao = new BorrowBookDao();
        this.userDao = new UserDao();
    }

    @BeforeAll
    static void createConnection() {
        String fileName = "application.properties";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = classLoader.getResourceAsStream(fileName)) {
            properties.load(resourceStream);
            DataSourceDatabase.getInstance().createConnection(properties);
            GlobalDataSource.setDataSource(DataSourceDatabase.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
                String userDelete = "DELETE FROM users";
                String bookDelete = "DELETE FROM books";
                DataSourceDatabase.sqlExecutionerForDML(userDelete);
                DataSourceDatabase.sqlExecutionerForDML(bookDelete);
                DataSourceDatabase.commitToDatabase();
        */
    }

    @AfterAll
    static void closeDataBaseConnection() throws SQLException {
        String userDelete = "DELETE FROM users";
        String bookDelete = "DELETE FROM books";
        DataSourceDatabase.sqlExecutionerForDML(userDelete);
        DataSourceDatabase.sqlExecutionerForDML(bookDelete);
        DataSourceDatabase.commitToDatabase();
        DataSourceDatabase.closeDataBaseConnection();
    }

    @AfterEach
    void tearDown() throws SQLException {
        String userDelete = "DELETE FROM users";
        String bookDelete = "DELETE FROM books";
        DataSourceDatabase.sqlExecutionerForDML(userDelete);
        DataSourceDatabase.sqlExecutionerForDML(bookDelete);
        DataSourceDatabase.commitToDatabase();
    }

    @Test
    void checkBorrowABook() throws SQLException {
        User testUser = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        Book testBook = new Book("js", "lawrance", 10, "computer");
        userDao.addUser(testUser);
        bookDao.addBook(testBook);

        borrowBookDao.borrowABook(testBook.getId(), testUser.getUserName());

        List<BorrowedBook> borrowedBooks = borrowBookDao.getAllBorrowedBook(testUser.getUserName());
        Condition<BorrowedBook> condition = new Condition<>( (BorrowedBook borrowedBook) -> (
                borrowedBook.getBook().getId().equals(testBook.getId()) &&
                borrowedBook.getUserName().equals(testUser.getUserName())
                ), "Check whether borrowed book is correct as we expect");

        assertThat(borrowedBooks.get(0)).has(condition);
        borrowBookDao.returnABook(testBook.getId(), testUser.getUserName());
    }

    @Test
    void checkNumberOfBorrowedBooks() throws SQLException {
        User testUser = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        Book testBook = new Book("js", "lawrance", 10, "computer");
        userDao.addUser(testUser);
        bookDao.addBook(testBook);

        borrowBookDao.borrowABook(testBook.getId(), testUser.getUserName());

        int noOfBooks = borrowBookDao.numberOfBookBorrowed(testUser.getUserName());

        assertThat(noOfBooks).isEqualTo(1);
        borrowBookDao.returnABook(testBook.getId(), testUser.getUserName());
    }

    @Test
    void checkReturnBook() throws SQLException {
        User testUser = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        Book testBook = new Book("js", "lawrance", 10, "computer");
        userDao.addUser(testUser);
        bookDao.addBook(testBook);

        borrowBookDao.borrowABook(testBook.getId(), testUser.getUserName());

        int beforeReturn = borrowBookDao.numberOfBookBorrowed(testUser.getUserName());
        borrowBookDao.returnABook(testBook.getId(), testUser.getUserName());

        int afterReturn = borrowBookDao.numberOfBookBorrowed(testUser.getUserName());
        assertThat(afterReturn).isLessThan(beforeReturn);
    }

    @Test
    void checkBorrowBookDecreasesBookCount() throws SQLException {
        User testUser = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        Book testBook = new Book("js", "lawrance", 10, "computer");
        userDao.addUser(testUser);
        bookDao.addBook(testBook);

        int quantityBeforeBorrow = bookDao.getBookById(testBook.getId()).getQuantity();
        borrowBookDao.borrowABook(testBook.getId(), testUser.getUserName());

        int quantityAfterBorrow = bookDao.getBookById(testBook.getId()).getQuantity();
        assertThat(quantityBeforeBorrow).isEqualTo(quantityAfterBorrow + 1);

        borrowBookDao.returnABook(testBook.getId(), testUser.getUserName());
    }

    @Test
    void checkReturnBookIncreasesBookCount() throws SQLException {
        User testUser = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        Book testBook = new Book("js", "lawrance", 10, "computer");
        userDao.addUser(testUser);
        bookDao.addBook(testBook);

        borrowBookDao.borrowABook(testBook.getId(), testUser.getUserName());
        int quantityBeforeReturn = bookDao.getBookById(testBook.getId()).getQuantity();

        borrowBookDao.returnABook(testBook.getId(), testUser.getUserName());
        int quantityAfterReturn = bookDao.getBookById(testBook.getId()).getQuantity();

        assertThat(quantityBeforeReturn).isEqualTo(quantityAfterReturn - 1);
    }

}