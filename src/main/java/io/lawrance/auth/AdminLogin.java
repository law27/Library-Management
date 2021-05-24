package io.lawrance.auth;

import io.lawrance.datasource.GlobalDataSource;
import io.lawrance.model.AccessLevel;
import io.lawrance.model.User;

import java.sql.SQLException;
import java.util.Scanner;

public class AdminLogin implements ILogin {

    private static AdminLogin adminLogin = null;

    private AdminLogin() {

    }

    public static AdminLogin getInstance() {
        if(adminLogin == null) {
            adminLogin = new AdminLogin();
        }
        return adminLogin;
    }

    @Override
    public boolean loginMechanism() {
        Scanner sc = new Scanner(System.in);
        boolean satisfied = false;
        while (!satisfied) {
            System.out.print("Enter username: ");
            String userName = sc.nextLine();
            System.out.println();
            System.out.print("Enter password: ");
            String passWord = sc.nextLine();
            System.out.println();
            try {
                User user = GlobalDataSource.getDataSource().getUserDao().getUser(userName);
                if(user != null && user.getPassword().equals(passWord) && user.getAccessLevel() == AccessLevel.ADMIN) {
                    LoggedInUser.setLoggedInUser(user);
                    satisfied = true;
                }
                else {
                    System.out.println("Wrong username or password or you're not admin");
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
