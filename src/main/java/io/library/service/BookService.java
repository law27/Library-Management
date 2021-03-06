package io.library.service;


import io.library.dao.IBookDao;
import io.library.datasource.DataSourceDatabase;
import io.library.datasource.GlobalDataSource;
import io.library.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookService {
    private final static Logger logger = LoggingService.getLogger(BookService.class);
    private static Scanner sc = null;
    private final IBookDao bookDao;
    private static BookService bookService = null;
    private final String HEADINGS = "Id\t\t\t\t\t\t\t\t\t\tBook Name\tAuthor\t\tQuantity\t\tGenre\n" +
                                    "==\t\t\t\t\t\t\t\t\t\t=========\t======\t\t========\t\t=====";

    private BookService() {
        sc = Utility.getScanner();
        bookDao = GlobalDataSource.getDataSource().getBookDao();
    }

    private BookService(IBookDao bookDao) {
        this.bookDao = bookDao;
    }

    public static BookService getInstance() {
        if(bookService == null) {
            bookService = new BookService();
        }
        return bookService;
    }

    public static BookService getInstance(IBookDao bookDao) {
        if(bookService == null) {
            bookService = new BookService(bookDao);
        }
        return bookService;
    }

    protected List<Book> getBookByAuthor(String name) {
        List<Book> booksList = null;
        if(!name.equals("")) {
            try {
                booksList = bookDao.getBookByAuthor(name);
            } catch (SQLException exception) {

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

            }
        }
        return booksList;
    }

    private void searchByAuthor() {
        System.out.println("Enter author name: ");
        String author = sc.nextLine();
        var books = getBookByAuthor(author);
        if (books == null || books.isEmpty()) {
            System.out.println("No Book available on given author name");
        } else {
            System.out.println(HEADINGS);
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    protected Book getBookByName(String bookName) {
        Book book = null;
        try {
            book = bookDao.getBookByName(bookName);
        }
        catch (SQLException exception) {

            logger.log(CustomLevel.ERROR, exception.toString(), exception);

        }
        return book;
    }

    private void searchByBookName() {
        System.out.println("Enter book name");
        String bookName = sc.nextLine();
        var book = getBookByName(bookName);
        if(book == null) {
            System.out.println("No Such Book");
        }
        else {
            System.out.println(HEADINGS);
            System.out.println(book);
        }
    }

    protected Book getBookById(String bookId) {
        Book book = null;
        if(!bookId.equals("")) {
            try {
                book = bookDao.getBookById(bookId);
            }
            catch (SQLException exception) {

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

            }
        }
        return book;
    }

    private void searchByBookId() {
        System.out.println("Enter Id");
        String id = sc.nextLine();
        var book = getBookById(id);
        if (book == null) {
            System.out.println("No Such Book");
        }
        else {
            System.out.println(HEADINGS);
            System.out.println(book);
        }
    }

    protected List<Book> getBooksByGenre(String genre) {
        List<Book> booksList = null;
        if(!genre.equals("")) {
            try {
                booksList = bookDao.getBookByGenre(genre);
            } catch (SQLException exception) {

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

            }
        }
        return booksList;
    }

    private void searchByGenre() {
        System.out.println("Enter genre");
        String genre = sc.nextLine();
        var books = getBooksByGenre(genre);
        if(books == null || books.isEmpty()) {
            System.out.println("No Book Available");
        }
        else {
            System.out.println(HEADINGS);
            for(var book : books) {
                System.out.println(book);
            }
        }
    }

    private void increaseQuantityOfABook(String bookId, int quantity) {
        try {
            bookDao.increaseQuantityOfBook(bookId, quantity);
            if(GlobalDataSource.getDataSource() instanceof DataSourceDatabase) {
                DataSourceDatabase.commitToDatabase();
            }
        }
        catch (SQLException exception) {

            logger.log(CustomLevel.ERROR, exception.toString(), exception);

        }
    }

    protected boolean addNewBook(String bookName, String bookAuthor, int quantity, String genre) {
        boolean isSuccess = false;
        if (bookName.equals("") || bookAuthor.equals("") || genre.equals("")) {
            System.out.println("None of the field can be empty");
        }
        else if(quantity <= 0) {
            System.out.println("Book quantity should be more than 0");
        }
        else {
            try {
                Book book = new Book(bookName, bookAuthor, quantity, genre);
                bookDao.addBook(book);

                logger.log(Level.INFO, "New Book added: " + book);

                isSuccess = true;
            }
            catch (SQLException exception) {

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

            }
        }
        return isSuccess;
    }

    private void printOptions() {
        System.out.println("1. Search by Author");
        System.out.println("2. Search by Book Name");
        System.out.println("3. Search by Id");
        System.out.println("4. Search by Genre");
        System.out.println("5. Main Menu");
    }

    public int getQuantityOfBook(String bookId) throws SQLException {
        return bookDao.getBookById(bookId).getQuantity();
    }

    public void addBook() {
        System.out.println("Enter Book details");

        System.out.print("Book Name: ");
        String bookName = sc.nextLine();
        System.out.println();

        System.out.print("Book Author Name: ");
        String bookAuthor = sc.nextLine();
        System.out.println();

        System.out.print("Book Quantity: ");
        int quantity;
        try {
            quantity = sc.nextInt();
        } catch (Exception exception) {
            sc.nextLine();

            logger.log(CustomLevel.ERROR, exception.toString(), exception);

            System.out.println("Quantity should be of number");
            return;
        }
        sc.nextLine();
        System.out.println();

        System.out.println("Enter Genre: ");
        String genre = sc.nextLine().toLowerCase();
        System.out.println();
        Book checkBookIsAlreadyThere = getBookByName(bookName);
        if (checkBookIsAlreadyThere != null) {
            System.out.println("The book is already there do you want to increase the quantity ?");
            System.out.println("Type 'yes' or type anything to cancel");
            String option = sc.nextLine();
            if (option.equals("yes")) {
                increaseQuantityOfABook(checkBookIsAlreadyThere.getId(), quantity);
            }
        } else {
            if (addNewBook(bookName, bookAuthor, quantity, genre)) {
                System.out.println("Book added Successfully");
            } else {
                System.out.println("Unable to add the book");
            }
            System.out.println();
        }
    }

    public void searchOptions() {
        boolean satisfied = false;

        while (!satisfied) {
            printOptions();
            System.out.print("Enter your option:   ");
            int userInput;
            try {
                userInput = sc.nextInt();
            }
            catch (Exception exception) {
                sc.nextLine();

                logger.log(CustomLevel.ERROR, exception.toString(), exception);

                System.out.println("Enter a valid Number");
                System.out.println();
                continue;
            }
            sc.nextLine();
            System.out.println();
            switch (userInput) {
                case 1:
                    this.searchByAuthor();
                    satisfied = true;
                    break;
                case 2:
                    this.searchByBookName();
                    satisfied = true;
                    break;
                case 3:
                    this.searchByBookId();
                    satisfied = true;
                    break;
                case 4:
                    this.searchByGenre();
                    satisfied = true;
                    break;
                case 5:
                    satisfied = true;
                    break;
                default:
                    System.out.println("Enter a valid option");
                    break;
            }
        }
    }
}
