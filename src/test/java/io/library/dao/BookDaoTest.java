package io.library.dao;

import io.library.datasource.DataSourceDatabase;
import io.library.datasource.GlobalDataSource;
import io.library.model.Book;
import io.library.service.Utility;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class BookDaoTest {
    Book book;
    BookDao bookDao;

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
    }

    @AfterAll
    static void closeDataBaseConnection() throws SQLException {
        DataSourceDatabase.closeDataBaseConnection();
    }

    @BeforeEach
    void setUp() throws SQLException {
        bookDao = new BookDao();
        book = new Book(UUID.randomUUID().toString(), "js", "lawrance", 10, "computer");
        String sql = String.format("INSERT INTO books(id, book_name, book_author, quantity, genre) VALUES(%s, %s, %s, %d, %s)",
                Utility.getFormattedString(book.getId()),
                Utility.getFormattedString(book.getBookName()),
                Utility.getFormattedString(book.getAuthor()),
                book.getQuantity(),
                Utility.getFormattedString(book.getGenre())
        );
        DataSourceDatabase.sqlExecutionerForDML(sql);
    }

    @AfterEach
    void tearDown() throws SQLException {
        String sql = "DELETE FROM books";
        DataSourceDatabase.sqlExecutionerForDML(sql);
    }

    @Test
    void checkWhetherBookIsBeingAdded() throws SQLException {
        Book testBook = new Book("python", "lawrance", 10, "computer");
        bookDao.addBook(testBook);
        Book expected = bookDao.getBookById(testBook.getId());
        assertThat(expected).isEqualToComparingFieldByField(testBook);
    }


    @Test
    void checkGetBookByGenre() throws SQLException {
        List<Book> books = bookDao.getBookByGenre("computer");
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0)).isEqualToComparingFieldByField(book);
    }

    @Test
    void checkGetBookByAuthor() throws SQLException {
        List<Book> books = bookDao.getBookByAuthor("lawrance");
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0)).isEqualToComparingFieldByField(book);
    }

    @Test
    void checkGetBookByName() throws SQLException {
        Book expected = bookDao.getBookByName("js");
        assertThat(expected).isEqualToComparingFieldByField(book);
    }

    @Test
    void returnsNullIfNoSuchBookOnSearchByName() throws SQLException {
        Book expected = bookDao.getBookByName("chrome");
        assertThat(expected).isNull();
    }

    @Test
    void checkIncreaseQuantityOfBook() throws SQLException {
        int currentQuantity = book.getQuantity();
        bookDao.increaseQuantityOfBook(book.getId(), 2);
        int expectedQuantity = currentQuantity + 2;
        Book testBook = bookDao.getBookById(book.getId());
        assertThat(expectedQuantity).isEqualTo(testBook.getQuantity());
    }

    @Test
    void checkDecreaseQuantityOfBook() throws SQLException {
        int currentQuantity = book.getQuantity();
        bookDao.decreaseQuantityOfBook(book.getId(), 2);
        int expectedQuantity = currentQuantity - 2;
        Book testBook = bookDao.getBookById(book.getId());
        assertThat(expectedQuantity).isEqualTo(testBook.getQuantity());
    }

    @Test
    void checkIncreaseQuantityOfBookOfNonExistingBook() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> bookDao.increaseQuantityOfBook(UUID.randomUUID().toString(), 10))
                .withMessage("There is no such Book");
    }

    @Test
    void checkDecreaseQuantityOfBookOfNonExistingBook() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> bookDao.decreaseQuantityOfBook(UUID.randomUUID().toString(), 10))
                .withMessage("There is no such Book");
    }
}