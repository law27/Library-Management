package io.lawrance.auth;

import io.lawrance.dao.UserDao;
import io.lawrance.model.User;

import java.util.Scanner;

public class UserLogin implements ILogin {

    private static UserLogin userLogin = null;

    private UserLogin() {

    }

    public static UserLogin getInstance() {
        if (userLogin == null) {
            userLogin = new UserLogin();
        }
        return userLogin;
    }

    @Override
    public boolean loginMechanism() {
        Scanner sc = new Scanner(System.in);
        boolean satisfied = false;
        while (!satisfied) {
            System.out.print("Enter userName: ");
            String userName = sc.nextLine();
            System.out.println();
            System.out.print("Enter password: ");
            String passWord = sc.nextLine();
            System.out.println();
            User user = UserDao.getUser(userName);
            if(user.getPassword().equals(passWord)) {
                LoggedInUser.setLoggedInUser(user);
                satisfied = true;
            }
            else {
                System.out.println("Wrong username or password");
            }
        }
        return true;
    }
}
