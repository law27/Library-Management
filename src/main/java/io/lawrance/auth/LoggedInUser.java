package io.lawrance.auth;

import io.lawrance.model.User;

public class LoggedInUser {
    private static User user = null;

    public static void setLoggedInUser(User loggedInUser) {
        user = loggedInUser;
    }

    public static User getLoggedInUser() {
        return user;
    }
}
