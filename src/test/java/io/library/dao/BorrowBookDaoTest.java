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

    private static BookDao bookDao;
    private static BorrowBookDao borrowBookDao;
    private static User user;
    private static Book book;

    @BeforeAll
    static void createConnection() throws SQLException {
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
        String userDelete = "DELETE FROM users";
        String bookDelete = "DELETE FROM books";
        DataSourceDatabase.sqlExecutionerForDML(userDelete);
        DataSourceDatabase.sqlExecutionerForDML(bookDelete);
        UserDao userDao = new UserDao();
        bookDao = new BookDao();
        borrowBookDao = new BorrowBookDao();
        user = new User("test", "test123", "6369399387", 21, AccessLevel.USER, new UserMenu());
        book = new Book("js", "lawrance", 10, "computer");
        userDao.addUser(user);
        bookDao.addBook(book);
    }

    @AfterAll
    static void closeDataBaseConnection() throws SQLException {
        String userDelete = "DELETE FROM users";
        String bookDelete = "DELETE FROM books";
        DataSourceDatabase.sqlExecutionerForDML(userDelete);
        DataSourceDatabase.sqlExecutionerForDML(bookDelete);
        DataSourceDatabase.closeDataBaseConnection();
    }

    @AfterEach
    void tearDown() throws SQLException {
        try {
            borrowBookDao.returnABook(book.getId(), user.getUserName());
        }
        catch (IllegalStateException exception) {
            System.out.println("No Need to return");
        }
        String borrowedDelete = "DELETE FROM borrowed_books";
        DataSourceDatabase.sqlExecutionerForDML(borrowedDelete);
    }

    @Test
    void checkBorrowABook() throws SQLException {
        borrowBookDao.borrowABook(book.getId(), user.getUserName());
        List<BorrowedBook> borrowedBooks = borrowBookDao.getAllBorrowedBook(user.getUserName());
        Condition<BorrowedBook> condition = new Condition<>( (BorrowedBook borrowedBook) -> (
                borrowedBook.getBook().getId().equals(book.getId()) &&
                borrowedBook.getUserName().equals(user.getUserName())
                ), "Check whether borrowed book is correct as we expect");
        assertThat(borrowedBooks.get(0)).has(condition);
    }

    @Test
    void checkNumberOfBorrowedBooks() throws SQLException {
        borrowBookDao.borrowABook(book.getId(), user.getUserName());
        int noOfBooks = borrowBookDao.numberOfBookBorrowed(user.getUserName());
        assertThat(noOfBooks).isEqualTo(1);
    }

    @Test
    void checkReturnBook() throws SQLException {
        borrowBookDao.borrowABook(book.getId(), user.getUserName());
        int beforeReturn = borrowBookDao.numberOfBookBorrowed(user.getUserName());
        borrowBookDao.returnABook(book.getId(), user.getUserName());
        int afterReturn = borrowBookDao.numberOfBookBorrowed(user.getUserName());
        assertThat(afterReturn).isLessThan(beforeReturn);
    }

    @Test
    void checkBorrowBookDecreasesBookCount() throws SQLException {
        int quantityBeforeBorrow = bookDao.getBookById(book.getId()).getQuantity();
        borrowBookDao.borrowABook(book.getId(), user.getUserName());
        int quantityAfterBorrow = bookDao.getBookById(book.getId()).getQuantity();
        assertThat(quantityBeforeBorrow).isEqualTo(quantityAfterBorrow + 1);
    }

    @Test
    void checkReturnBookIncreasesBookCount() throws SQLException {
        borrowBookDao.borrowABook(book.getId(), user.getUserName());
        int quantityBeforeReturn = bookDao.getBookById(book.getId()).getQuantity();
        borrowBookDao.returnABook(book.getId(), user.getUserName());
        int quantityAfterReturn = bookDao.getBookById(book.getId()).getQuantity();
        assertThat(quantityBeforeReturn).isEqualTo(quantityAfterReturn - 1);
    }



}