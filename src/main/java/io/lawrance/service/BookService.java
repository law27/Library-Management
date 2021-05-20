package io.lawrance.service;


import io.lawrance.dao.BookDao;
import io.lawrance.model.Book;

import java.util.Scanner;

public class BookService {
    Scanner sc;

    public BookService() {
        sc = new Scanner(System.in);
    }

    private void searchByAuthor() {
        System.out.println("Enter author name: ");
        String author = sc.nextLine();
        var books = BookDao.getBookByAuthor(author);
    }

    private void searchByBookName() {
        System.out.println("Enter book name");
        String bookName = sc.nextLine();
        var book = BookDao.getBookByName(bookName);
    }

    private void searchByBookId() {
        System.out.println("Enter Id");
        String id = sc.nextLine();
        var book = BookDao.getBookById(id);
    }

    public void printOptions() {
        System.out.println("1. Search by Author");
        System.out.println("2. Search by Book Name");
        System.out.println("3. Search by Id");
        System.out.println("4. Main Menu");
    }

    public void searchOptions() {
        boolean satisfied = false;

        while (!satisfied) {
            printOptions();
            System.out.print("Enter your option:   ");
            int userInput = sc.nextInt();
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
                    satisfied = true;
                    break;
                default:
                    System.out.println("Enter a valid option");
                    break;
            }
        }
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
        System.out.println();

        Book checkBookIsAlreadyThere = BookDao.getBookByName(bookName);
        if(checkBookIsAlreadyThere != null) {
            System.out.println("The book is already there do you want to increase the quantity ?");
            System.out.println("Type 'yes' or type anything to cancel");
            String option = sc.nextLine();
            if(option.equals("yes")) {
                BookDao.increaseQuantityOfBook(checkBookIsAlreadyThere.getId(), quantity);
            }

        }
        else {
            BookDao.addBook(new Book(bookName, bookAuthor, quantity));
            System.out.println("Successfully Added");
            System.out.println();
        }

    }

}
