package com.bittercode.repository;

import com.bittercode.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByBarcodeIn(List<String> barcodes);
}
