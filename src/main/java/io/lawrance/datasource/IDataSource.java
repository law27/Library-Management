package io.lawrance.datasource;

import io.lawrance.dao.IBookDao;
import io.lawrance.dao.IBorrowBookDao;
import io.lawrance.dao.IUserDao;

public interface IDataSource {
    IUserDao getUserDao();
    IBookDao getBookDao();
    IBorrowBookDao getBorrowBookDao();
}
