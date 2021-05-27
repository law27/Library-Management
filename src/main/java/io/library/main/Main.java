package io.library.main;

import io.library.datasource.DataSourceDatabase;
import io.library.datasource.DataSourceJSON;
import io.library.datasource.GlobalDataSource;
import io.library.menu.MainMenu;
import io.library.service.Utility;

import java.io.File;
import java.io.FileNotFoundException;
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
                MainMenu menu = new MainMenu();
                menu.show();
                Utility.closeScanner();
                DataSourceDatabase.closeDataBaseConnection();
            }
            catch (FileNotFoundException exception) {
                System.out.println(exception.getMessage());
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Some error occurred. Please Try Later");
            }
            // Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Hello World")));
    }
}
