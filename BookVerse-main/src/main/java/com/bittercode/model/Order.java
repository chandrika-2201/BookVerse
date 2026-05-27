package com.bittercode.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(name = "book_barcode", nullable = false)
    private String bookBarcode;

    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price_per_item", nullable = false)
    private Double pricePerItem;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "status", nullable = false)
    private String status; // "COMPLETED", "PENDING", "CANCELLED"

    // Constructors
    public Order() {}

    public Order(String customerEmail, String bookBarcode, String bookName, 
                 Integer quantity, Double pricePerItem, Double subtotal, LocalDateTime orderDate) {
        this.customerEmail = customerEmail;
        this.bookBarcode = bookBarcode;
        this.bookName = bookName;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.subtotal = subtotal;
        this.orderDate = orderDate;
        this.status = "COMPLETED";
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getBookBarcode() { return bookBarcode; }
    public void setBookBarcode(String bookBarcode) { this.bookBarcode = bookBarcode; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPricePerItem() { return pricePerItem; }
    public void setPricePerItem(Double pricePerItem) { this.pricePerItem = pricePerItem; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
