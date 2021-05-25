package io.library.datasource;

import io.library.dao.IBookDao;
import io.library.dao.IBorrowBookDao;
import io.library.dao.IUserDao;

public interface IDataSource {
    IUserDao getUserDao();
    IBookDao getBookDao();
    IBorrowBookDao getBorrowBookDao();
}
