package io.library.service;


import io.library.dao.IBorrowBookDao;
import io.library.dao.IUserDao;
import io.library.menu.UserMenu;
import io.library.model.AccessLevel;
import io.library.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private final static IUserDao userDao = Mockito.mock(IUserDao.class);
    private final static IBorrowBookDao borrowDao = Mockito.mock(IBorrowBookDao.class);
    private final UserService userService;

    UserServiceTest() {
        userService = UserService.getInstance(userDao, borrowDao);
    }

    @Test
    void getUserByUserNameReturnNullWhenNoDataFound() throws SQLException {
        // Given
        String userName = "rubesh";

        // When
        when(userDao.getUser(userName)).thenReturn(null);
        User result = userService.getUserByUserName(userName);

        // expect
        assertThat(result).isNull();
    }

    @Test
    void getUserByUserNameReturnsNullOnEmptyArgument() {
        // Given
        String userName = "";

        // When
        User result = userService.getUserByUserName(userName);

        // expect
        assertThat(result).isNull();
    }

    @Test
    void getUserByUsernameReturnsNullOnException() throws SQLException {
        // Given
        String userName = "lawrance";

        // When
        when(userDao.getUser(userName)).thenThrow(new SQLException("Test Exception"));
        User result = userService.getUserByUserName(userName);

        // expect
        assertThat(result).isNull();
    }

    @Test
    void getUserByUserNameReturnsUserObjOnDataFound() throws SQLException {
        // Given
        String userName = "karthick";
        User user = new User(userName, "lawrance", "74643564754", 21, AccessLevel.USER, new UserMenu());

        // When
        when(userDao.getUser(userName)).thenReturn(user);
        User result = userService.getUserByUserName(userName);

        // expect
        assertThat(result).isEqualToComparingFieldByField(user);
    }
}