package io.library.main;

import io.library.datasource.DataSourceDatabase;
import io.library.datasource.DataSourceJSON;
import io.library.datasource.GlobalDataSource;
import io.library.menu.MainMenu;
import io.library.service.LoggingService;
import io.library.service.Utility;
import io.library.service.XMLUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.*;

public class Main {

    private static final Logger logger = LoggingService.getLogger(Main.class);

    public static void main(String[] args) {
        String fileName = "application.properties";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();

        logger.log(Level.INFO, "Program started");

        try (InputStream resourceStream = classLoader.getResourceAsStream(fileName)) {
            properties.load(resourceStream);
            if (properties.getProperty("data-source").equals("json")) {

                logger.log(Level.INFO, "Using json as Datasource");

                File users = new File("src/main/json/users.json");
                File books = new File("src/main/json/books.json");
                File borrow = new File("src/main/json/borrow.json");
                XMLUtility.init();
                DataSourceJSON.getInstance().createConnection(books, users, borrow);
                GlobalDataSource.setDataSource(DataSourceJSON.getInstance());
            } 
            else if(properties.getProperty("data-source").equals("db")) {

                logger.log(Level.INFO, "Using DB as Datasource");

                DataSourceDatabase.getInstance().createConnection(properties);
                GlobalDataSource.setDataSource(DataSourceDatabase.getInstance());
            }
            else {
                System.out.println("Data source not supported");
                logger.log(Level.INFO, "Data source not supported");
            }

            MainMenu menu = new MainMenu();
            menu.show();
            DataSourceDatabase.closeDataBaseConnection();
        } catch (FileNotFoundException exception) {

            logger.log(Level.SEVERE, exception.toString(), exception);
            System.out.println(exception.getMessage());

        } catch (Exception e) {

            logger.log(Level.SEVERE, e.toString(), e);

            System.out.println(e.getMessage());
            System.out.println("Some error occurred. Please Try Later");
        }
        finally {
            logger.log(Level.INFO, "Program exited ");

            Utility.closeScanner();
            LoggingService.closeHandler();
        }
        // Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Hello World")));
    }
}
