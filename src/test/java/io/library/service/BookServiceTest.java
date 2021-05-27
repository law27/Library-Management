package io.library.service;

import io.library.dao.BookDao;
import io.library.model.Book;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


class BookServiceTest {

    @Test
    public void checkAddBook() throws SQLException {
        String input = "javascript\nlawrance\n10\ncomputer\n";
        BookDao bookDao = Mockito.mock(BookDao.class);
        BookService bookService = BookService.getInstance(new ByteArrayInputStream(input.getBytes()), bookDao);
        bookService.addBook();
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao).addBook(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.getValue();
        Condition<Book> bookCondition = new Condition<>( (Book book) -> (
                        book.getBookName().equals("javascript") &&
                        book.getAuthor().equals("lawrance") &&
                        book.getQuantity() == 10 &&
                        book.getGenre().equals("computer")
        ), "Check whether the book is same");
        assertThat(capturedBook).has(bookCondition);
    }

    @Test
    public void checkSearchByAuthor() throws SQLException {
        String input = "1\nlawrance\n5\n";
        BookDao bookDao = Mockito.mock(BookDao.class);
        BookService bookService = BookService.getInstance(new ByteArrayInputStream(input.getBytes()), bookDao);
        bookService.searchOptions();
        ArgumentCaptor<String> bookArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(bookDao).getBookByAuthor(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue()).isEqualTo("lawrance");
    }

    @Test
    public void checkSearchByBookName() throws SQLException {
        String input = "2\njava\n5\n";
        BookDao bookDao = Mockito.mock(BookDao.class);
        BookService bookService = BookService.getInstance(new ByteArrayInputStream(input.getBytes()), bookDao);
        bookService.searchOptions();
        ArgumentCaptor<String> bookArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(bookDao).getBookByName(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue()).isEqualTo("java");
    }

    @Test
    public void checkSearchByBookId() throws SQLException {
        String bookId = UUID.randomUUID().toString();
        String input = "3\n" + bookId  + "\n5\n";
        BookDao bookDao = Mockito.mock(BookDao.class);
        BookService bookService = BookService.getInstance(new ByteArrayInputStream(input.getBytes()), bookDao);
        bookService.searchOptions();
        ArgumentCaptor<String> bookArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(bookDao).getBookById(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue()).isEqualTo(bookId);
    }

    @Test
    public void checkSearchByGenre() throws SQLException {
        String input = "4\ncomputer\n5\n";
        BookDao bookDao = Mockito.mock(BookDao.class);
        BookService bookService = BookService.getInstance(new ByteArrayInputStream(input.getBytes()), bookDao);
        bookService.searchOptions();
        ArgumentCaptor<String> bookArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(bookDao).getBookByGenre(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue()).isEqualTo("computer");
    }
}





