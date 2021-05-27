package io.library.service;


import io.library.dao.IBookDao;
import io.library.datasource.GlobalDataSource;
import io.library.model.Book;

import java.sql.SQLException;
import java.util.Scanner;

public class BookService {
    private static Scanner sc = null;
    private IBookDao bookDao;
    public static BookService bookService = null;

    private BookService() {
        sc = new Scanner(System.in);
        bookDao = GlobalDataSource.getDataSource().getBookDao();
    }

    public static BookService getInstance() {
        if(bookService == null) {
            bookService = new BookService();
        }
        return bookService;
    }

    // For Mocking purpose
    protected static BookService getInstance(IBookDao bookDao) {
        if(bookService == null) {
            bookService = new BookService();
        }
        bookService.bookDao = bookDao;
        return bookService;
    }

    private void printHeadings() {
        System.out.println("Id\t\t\t\t\t\t\t\t\t\tBook Name\tAuthor\t\tQuantity\t\tGenre");
        System.out.println("==\t\t\t\t\t\t\t\t\t\t=========\t======\t\t========\t\t=====");
    }

    private void printBook(Book book) {
        System.out.printf("%s\t%s\t\t%s\t%d\t\t\t%s\n", book.getId(), book.getBookName(), book.getAuthor(), book.getQuantity(), book.getGenre());
    }

    private void searchByAuthor() {
        System.out.println("Enter author name: ");
        String author = sc.nextLine();
        try {
            var books = bookDao.getBookByAuthor(author);
            if(books.isEmpty()) {
                System.out.println("No Book available on given author name");
            }
            else {
                printHeadings();
                for(Book book : books) {
                    printBook(book);
                }
            }
        }
        catch (SQLException exception) {
            System.out.println("SQL Error");
            exception.printStackTrace();
        }
    }

    private void searchByBookName() {
        System.out.println("Enter book name");
        String bookName = sc.nextLine();
        try {
            var book = bookDao.getBookByName(bookName);
            if(book == null) {
                System.out.println("No Such Book");
            }
            else {
                printHeadings();
                printBook(book);
            }
        }
        catch (SQLException exception) {
            System.out.println("SQL Exception");
            exception.printStackTrace();
        }
    }

    private void searchByBookId() {
        System.out.println("Enter Id");
        String id = sc.nextLine();
        try {
            var book = bookDao.getBookById(id);
            if (book == null) {
                System.out.println("No Such Book");
            }
            else {
                printHeadings();
                printBook(book);
            }
        }
        catch (SQLException exception) {
            System.out.println("SQL Error");
            exception.printStackTrace();
        }

    }

    private void searchByGenre() {
        System.out.println("Enter genre");
        String genre = sc.nextLine();
        try {
            var books = bookDao.getBookByGenre(genre);
            if(books.isEmpty()) {
                System.out.println("No Book Available");
            }
            else {
                printHeadings();
                for(var book : books) {
                    this.printBook(book);
                }
            }
        }
        catch (SQLException exception) {
            System.out.println("SQL Error");
            exception.printStackTrace();
        }
    }

    public void printOptions() {
        System.out.println("1. Search by Author");
        System.out.println("2. Search by Book Name");
        System.out.println("3. Search by Id");
        System.out.println("4. Search by Genre");
        System.out.println("5. Main Menu");
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
        int quantity  = sc.nextInt();
        sc.nextLine();
        System.out.println();

        System.out.println("Enter Genre: ");
        String genre = sc.nextLine().toLowerCase();
        System.out.println();

        try {
            Book checkBookIsAlreadyThere = bookDao.getBookByName(bookName);
            if(checkBookIsAlreadyThere != null) {
                System.out.println("The book is already there do you want to increase the quantity ?");
                System.out.println("Type 'yes' or type anything to cancel");
                String option = sc.nextLine();
                if(option.equals("yes")) {
                    bookDao.increaseQuantityOfBook(checkBookIsAlreadyThere.getId(), quantity);
                }

            }
            else {
                bookDao.addBook(new Book(bookName, bookAuthor, quantity, genre));
                System.out.println();
            }
        }
        catch (SQLException exception) {
            System.out.println("SQL Error");
            exception.printStackTrace();
        }

    }

    public void searchOptions() {
        boolean satisfied = false;

        while (!satisfied) {
            printOptions();
            System.out.print("Enter your option:   ");
            int userInput = sc.nextInt();
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
