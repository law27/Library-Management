package io.library.service;



import io.library.dao.IBookDao;
import io.library.dao.IBorrowBookDao;
import io.library.menu.UserMenu;
import io.library.model.AccessLevel;
import io.library.model.Book;
import io.library.model.BorrowedBook;
import io.library.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class BorrowServiceTest {

    private final static IBorrowBookDao borrowBookDao = Mockito.mock(IBorrowBookDao.class);
    private final static IBookDao bookDao = Mockito.mock(IBookDao.class);
    private final static User user = new User("lawrance", "lawrance", "74643564754", 21, AccessLevel.USER, new UserMenu());

    private final BorrowService borrowService;

    BorrowServiceTest() {
        BookService bookService = BookService.getInstance(bookDao);
        borrowService = BorrowService.getInstance(user, borrowBookDao, bookService);
    }

    @Test
    void borrowABookShouldReturnFalseOnEmptyBookId() {
        // Given
        String userName = user.getUserName();
        String bookId = "";

        //when
        boolean result = borrowService.borrowABook(bookId, userName);

        //expected
        assertThat(result).isFalse();
    }

    @Test
    void borrowABookShouldReturnFalseOnEmptyStock() throws SQLException {
        // Given
        String userName = user.getUserName();
        String bookId = UUID.randomUUID().toString();
        Book book = new Book(bookId, "python", "lawrance", 0, "computer");

        //When
        when(bookDao.getBookById(bookId)).thenReturn(book);
        boolean result = borrowService.borrowABook(bookId, userName);

        //expected
        assertThat(result).isFalse();
    }

    @Test
    void borrowABookShouldReturnFalseOnException() throws SQLException {
        // Given
        String userName = user.getUserName();
        String bookId = UUID.randomUUID().toString();
        Book book = new Book(bookId, "python", "lawrance", 0, "computer");

        //When
        when(bookDao.getBookById(bookId)).thenThrow(new SQLException("Test Exception"));
        boolean result = borrowService.borrowABook(bookId, userName);

        //expected
        assertThat(result).isFalse();
    }

    @Test
    void borrowABookShouldReturnTrueOnSuccessfulBorrow() throws SQLException {
        // Given
        String userName = user.getUserName();
        String bookId = UUID.randomUUID().toString();
        Book book = new Book(bookId, "python", "lawrance", 1, "computer");

        //When
        when(bookDao.getBookById(bookId)).thenReturn(book);
        boolean result = borrowService.borrowABook(bookId, userName);

        //expected
        assertThat(result).isTrue();
    }

    @Test
    void testNumberOfBorrowedBookOfUser() throws SQLException {
        //Given
        String userName = user.getUserName();

        //When
        when(borrowBookDao.numberOfBookBorrowed(userName)).thenReturn(10);
        int result = borrowService.getNumberOfBorrowedBookOfUser(userName);

        //expected
        assertThat(result).isEqualTo(10);
    }

    @Test
    void getAllBorrowedBookShouldReturnNullOnException() throws SQLException {
        // Given
        String userName = user.getUserName();

        //When
        when(borrowBookDao.getAllBorrowedBook(userName)).thenThrow(new SQLException("Test Error"));
        List<BorrowedBook> borrowedBookList = borrowService.getAllBorrowedBook(userName);

        //expected
        assertThat(borrowedBookList).isNull();
    }

    @Test
    void getAllBorrowedBookShouldReturnListOnDataFound() throws SQLException {
        // Given
        String userName = "karthick";
        Book book = new Book(UUID.randomUUID().toString(), "python", "lawrance", 10, "computer");
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        borrowedBooks.add(new BorrowedBook(book, "", "", userName));

        //When
        when(borrowBookDao.getAllBorrowedBook(userName)).thenReturn(borrowedBooks);
        var result = borrowService.getAllBorrowedBook(userName);

        //expected
        assertThat(result).hasSameElementsAs(borrowedBooks);
    }

    @Test
    void getAllBorrowedBookShouldReturnEmptyListOnNoDataFound() throws SQLException {
        // Given
        String userName = user.getUserName();
        List<BorrowedBook> borrowedBooks = new ArrayList<>();

        //When
        when(borrowBookDao.getAllBorrowedBook(userName)).thenReturn(borrowedBooks);
        var result = borrowService.getAllBorrowedBook(userName);

        //expected
        assertThat(result).isEmpty();
    }

    @Test
    void returnTheBookShouldReturnFalseOnEmptyBookId() {
        // Given
        String bookId = "";

        // When
        boolean result = borrowService.returnTheBook(bookId);

        //expected
        assertThat(result).isFalse();
    }

    @Test
    void returnTheBookShouldReturnFalseOnException() throws SQLException {
        // Given
        String bookId = UUID.randomUUID().toString();

        // When
        Mockito.doThrow(new SQLException("Test error")).when(borrowBookDao).returnABook(bookId, "lawrance");
        boolean result = borrowService.returnTheBook(bookId);

        //expected
        assertThat(result).isFalse();
    }

    @Test
    void returnTheBookShouldReturnTrueOnSuccessfulReturn() {
        // Given
        String bookId = UUID.randomUUID().toString();

        //When
        boolean result = borrowService.returnTheBook(bookId);

        //expected
        assertThat(result).isTrue();
    }
}