package io.library.auth;

import io.library.datasource.GlobalDataSource;
import io.library.model.User;
import io.library.service.Utility;

import java.sql.SQLException;
import java.util.Scanner;

public class UserLogin implements ILogin {

    private static UserLogin userLogin = null;

    private UserLogin() {

    }

    public synchronized static UserLogin getInstance() {
        if (userLogin == null) {
            userLogin = new UserLogin();
        }
        return userLogin;
    }

    @Override
    public boolean loginMechanism() {
        Scanner sc = Utility.getScanner();
        boolean satisfied = false;
        while (!satisfied) {
            System.out.print("Enter userName: ");
            String userName = sc.nextLine();
            System.out.println();
            System.out.print("Enter password: ");
            String passWord = sc.nextLine();
            System.out.println();
            try {
                User user = GlobalDataSource.getDataSource().getUserDao().getUser(userName);
                if(user != null && user.getPassword().equals(passWord)) {
                    LoggedInUser.setLoggedInUser(user);
                    satisfied = true;
                }
                else {
                    System.out.println("Wrong username or password");
                }
            }
            catch (SQLException exception) {
                System.out.println("SQL Error");
                exception.printStackTrace();
            }
        }
        return true;
    }
}
