package io.library.service;


import io.library.dao.BookDao;
import io.library.dao.BorrowBookDao;
import io.library.dao.IBookDao;
import io.library.model.User;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import static org.mockito.Mockito.verify;

class BorrowServiceTest {

    public void checkBorrowBook() {
        String bookId = UUID.randomUUID().toString();
        String input = bookId + "\n";
        InputStream stream = new ByteArrayInputStream(input.getBytes());

        BorrowBookDao borrowBookDao = Mockito.mock(BorrowBookDao.class);
        User testUser = Mockito.mock(User.class);
        IBookDao bookDao = Mockito.mock(BookDao.class);

        BorrowService bookService = BorrowService.getInstance(testUser, borrowBookDao, stream);
        bookService.borrowABook();
        verify(borrowBookDao);
    }

}