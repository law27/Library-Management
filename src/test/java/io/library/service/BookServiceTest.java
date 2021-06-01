package io.library.service;

import io.library.dao.IBookDao;
import io.library.model.Book;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class BookServiceTest {

    private static final IBookDao bookDao = Mockito.mock(IBookDao.class);

    private final BookService bookService;

    BookServiceTest() {
        bookService = BookService.getInstance(bookDao);
    }

    @Test
    void addNewBookWithOneEmptyStringReturnsFalse() {
        //Given
        String bookName = "";
        String bookAuthor = "lawrance";
        int quantity = 10;
        String genre = "action";

        //When
        boolean result = bookService.addNewBook(bookName, bookAuthor, quantity, genre);

        //Expected
        assertThat(result).isFalse();
    }

    @Test
    void addNewBookWithNegativeQuantityReturnsFalse() {
        //Given
        String bookName = "python";
        String bookAuthor = "lawrance";
        int quantity = -10;
        String genre = "action";

        //When
        boolean result = bookService.addNewBook(bookName, bookAuthor, quantity, genre);

        //Expected
        assertThat(result).isFalse();
    }

    @Test
    void addNewBookWithProperValuesShouldReturnTrue() {
        //Given
        String bookName = "python";
        String bookAuthor = "lawrance";
        int quantity = 10;
        String genre = "action";

        //When
        boolean result = bookService.addNewBook(bookName, bookAuthor, quantity, genre);

        //Expected
        assertThat(result).isTrue();
    }

    @Test
    void addNewBookWithProperValuesShouldCallBookDaoAddBook() throws SQLException {
        //Given
        String bookName = "python";
        String bookAuthor = "lawrance";
        int quantity = 10;
        String genre = "action";

        //When
        bookService.addNewBook(bookName, bookAuthor, quantity, genre);
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);

        //Expected
        verify(bookDao).addBook(captor.capture());
    }

    @Test
    void getBooksByGenreWithEmptyArgumentReturnNull() {
        //Given
        String genre = "";


        //When
        List<Book> result = bookService.getBooksByGenre(genre);

        //Expected
        assertThat(result).isNull();
    }

    @Test
    void getBooksByGenreWithCorrectArgumentReturnList() throws SQLException {
        //Given
        String genre = "action";
        List<Book> books = new ArrayList<>();
        books.add(new Book("python", "lawrance", 10, "action"));

        //When
        when(bookDao.getBookByGenre(genre)).thenReturn(books);
        var resultBooks = bookService.getBooksByGenre(genre);

        //Expected
        assertThat(books).hasSameElementsAs(resultBooks);
    }

    @Test
    void getBooksByGenreShouldReturnEmptyListIfNoMatchingDataFound() throws SQLException {
        //Given
        String genre = "action";
        List<Book> books = new ArrayList<>();

        //When
        when(bookDao.getBookByGenre(genre)).thenReturn(books);
        var resultBooks = bookService.getBooksByGenre(genre);

        //Expected
        assertThat(resultBooks).isEmpty();
    }

    @Test
    void getBooksByIdWithEmptyArgumentReturnNull() {
        //Given
        String bookId = "";


        //When
        Book result = bookService.getBookById(bookId);

        //Expected
        assertThat(result).isNull();
    }

    @Test
    void getBooksByIdShouldReturnNullIfNoMatchingBookFound() throws SQLException {
        //Given
        String bookId = UUID.randomUUID().toString();

        //When
        when(bookDao.getBookById(bookId)).thenReturn(null);
        var resultBook = bookService.getBookById(bookId);

        //Expected
        assertThat(resultBook).isNull();
    }

    @Test
    void getBookByIdWithCorrectArgumentCallsProperDaoMethod() throws SQLException {
        //Given
        String bookId = UUID.randomUUID().toString();

        //When
        bookService.getBookById(bookId);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        //Expected
        verify(bookDao).getBookById(captor.capture());
    }

    @Test
    void getBooksByIdShouldReturnBookIfMatchingBookFound() throws SQLException {
        //Given
        String bookId = UUID.randomUUID().toString();
        Book book = new Book(bookId, "python", "lawrance", 10, "computer");


        //When
        when(bookDao.getBookById(bookId)).thenReturn(book);
        var resultBook = bookService.getBookById(bookId);

        //Expected
        assertThat(resultBook).isEqualToComparingFieldByField(book);
    }

    // Here
    @Test
    void getBooksByNameWithEmptyArgumentReturnNull() {
        //Given
        String bookName = "";


        //When
        Book result = bookService.getBookByName(bookName);

        //Expected
        assertThat(result).isNull();
    }

    @Test
    void getBooksByNameShouldReturnNullIfNoMatchingBookFound() throws SQLException {
        //Given
        String bookName = "python";

        //When
        when(bookDao.getBookByName(bookName)).thenReturn(null);
        var resultBook = bookService.getBookById(bookName);

        //Expected
        assertThat(resultBook).isNull();
    }

    @Test
    void getBookByNameWithCorrectArgumentCallsProperDaoMethod() throws SQLException {
        //Given
        String bookName = "python";

        //When
        bookService.getBookByName(bookName);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        //Expected
        verify(bookDao).getBookByName(captor.capture());
    }

    @Test
    void getBooksByNameShouldReturnBookIfMatchingBookFound() throws SQLException {
        //Given
        String bookName = "python";
        Book book = new Book(UUID.randomUUID().toString(), bookName, "lawrance", 10, "computer");


        //When
        when(bookDao.getBookByName(bookName)).thenReturn(book);
        var resultBook = bookService.getBookByName(bookName);

        //Expected
        assertThat(resultBook).isEqualToComparingFieldByField(book);
    }

    @Test
    void getBooksByAuthorWithEmptyArgumentReturnNull() {
        //Given
        String author = "";


        //When
        List<Book> result = bookService.getBookByAuthor(author);

        //Expected
        assertThat(result).isNull();
    }

    @Test
    void getBooksByAuthorWithCorrectArgumentReturnList() throws SQLException {
        //Given
        String author = "lawrance";
        List<Book> books = new ArrayList<>();
        books.add(new Book("python", "lawrance", 10, "action"));

        //When
        when(bookDao.getBookByAuthor(author)).thenReturn(books);
        var resultBooks = bookService.getBookByAuthor(author);

        //Expected
        assertThat(books).hasSameElementsAs(resultBooks);
    }

    @Test
    void getBooksByAuthorShouldReturnEmptyListIfNoMatchingDataFound() throws SQLException {
        //Given
        String author = "lawrance";
        List<Book> books = new ArrayList<>();

        //When
        when(bookDao.getBookByAuthor(author)).thenReturn(books);
        var resultBooks = bookService.getBookByAuthor(author);

        //Expected
        assertThat(resultBooks).isEmpty();
    }
}

/*@Test
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
    }*/




