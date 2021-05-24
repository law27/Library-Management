package io.lawrance.main;

import io.lawrance.datasource.DataSourceDatabase;
import io.lawrance.datasource.DataSourceJSON;
import io.lawrance.datasource.GlobalDataSource;
import io.lawrance.menu.MainMenu;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        String fileName = "application.properties";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try(InputStream resourceStream = classLoader.getResourceAsStream(fileName)) {
            properties.load(resourceStream);
            if(properties.getProperty("data-source").equals("json")) {
                File users = new File("src/main/json/users.json");
                File books = new File("src/main/json/books.json");
                File borrow = new File("src/main/json/borrow.json");
                DataSourceJSON.getInstance().createConnection(books, users, borrow);
                GlobalDataSource.setDataSource(DataSourceJSON.getInstance());
            }
            else {
                DataSourceDatabase.getInstance().createConnection(properties);
                GlobalDataSource.setDataSource(DataSourceDatabase.getInstance());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        MainMenu menu = new MainMenu();
        menu.show();
    }
}
