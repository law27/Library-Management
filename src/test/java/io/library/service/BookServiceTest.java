package io.library.service;

import io.library.dao.BookDao;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    BookDao bookDao;
    BookService bookService;

    public BookServiceTest() {
        bookService = new BookService(bookDao);
    }


}