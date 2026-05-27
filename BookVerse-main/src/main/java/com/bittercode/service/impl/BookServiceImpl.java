package com.bittercode.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bittercode.model.Book;
import org.springframework.lang.Nullable;
import com.bittercode.repository.BookRepository;
import com.bittercode.service.BookService;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Nullable
    public Book getBookById(String barcode) {
        return bookRepository.findById(barcode).orElse(null);
    }

    @Override
    public String addBook(Book book) {
        try {
            if (bookRepository.existsById(book.getBarcode())) {
                return "FAILURE: Book already exists";
            }
            bookRepository.save(book);
            return "SUCCESS";
        } catch (Exception e) {
            return "FAILURE: " + e.getMessage();
        }
    }

    @Override
    public String deleteBookById(String barcode) {
        try {
            if (barcode == null || barcode.trim().isEmpty() || !bookRepository.existsById(barcode)) {
                return "FAILURE: Book not found";
            }
            bookRepository.deleteById(barcode);
            return "SUCCESS";
        } catch (Exception e) {
            return "FAILURE: " + e.getMessage();
        }
    }

    @Override
    public String updateBook(Book book) {
        try {
            String barcode = book == null ? null : book.getBarcode();
            if (barcode == null || barcode.trim().isEmpty()) {
                return "FAILURE: Book not found";
            }
            if (!bookRepository.existsById(barcode)) {
                return "FAILURE: Book not found";
            }
            bookRepository.save(book);
            return "SUCCESS";
        } catch (Exception e) {
            return "FAILURE: " + e.getMessage();
        }
    }

    @Override
    public String updateBookQtyById(String barcode, int qty) {
        try {
            Book book = bookRepository.findById(barcode).orElse(null);
            if (book == null) return "FAILURE";
            book.setQuantity(qty);
            bookRepository.save(book);
            return "SUCCESS";
        } catch (Exception e) {
            return "FAILURE";
        }
    }

    @Override
    public List<Book> getBooksByCommaSeperatedBookIds(String barcodes) {
        if (barcodes == null || barcodes.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<String> list = Arrays.asList(barcodes.split(","));
        return bookRepository.findByBarcodeIn(list);
    }
}
