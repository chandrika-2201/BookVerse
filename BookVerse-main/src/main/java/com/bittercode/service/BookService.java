package com.bittercode.service;

import java.util.List;

import org.springframework.lang.Nullable;

import com.bittercode.model.Book;

public interface BookService {
    List<Book> getAllBooks();
    @Nullable
    Book getBookById(String barcode);
    String addBook(Book book);
    String deleteBookById(String barcode);
    String updateBook(Book book);
    String updateBookQtyById(String barcode, int qty);
    List<Book> getBooksByCommaSeperatedBookIds(String barcodes);
}
