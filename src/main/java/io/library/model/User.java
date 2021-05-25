package io.library.model;

import io.library.menu.IMenu;

public class User {

    private String userName;
    private String password;
    private String mobileNumber;
    private int age;
    private AccessLevel accessLevel;
    private IMenu menu;


    public User(String userName,
                String password,
                String mobileNumber,
                int age,
                AccessLevel accessLevel,
                IMenu menu) {
        this.userName = userName;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.age = age;
        this.accessLevel = accessLevel;
        this.menu = menu;
    }

    public User(String userName, String password, AccessLevel accessLevel, IMenu menu) {
        this.userName = userName;
        this.password = password;
        this.accessLevel = accessLevel;
        this.menu = menu;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public IMenu getMenu() {
        return menu;
    }

    public void setMenu(IMenu menu) {
        this.menu = menu;
    }
}
