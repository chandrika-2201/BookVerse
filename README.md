# 📖 Online Book Store - Premium Glassmorphic Web App

A state-of-the-art, high-fidelity **Online Book Store** application designed with premium glassmorphism aesthetics, glowing visual components, and real-time business dashboards. Built using **Spring Boot 3**, **Spring Data JPA**, **Java 21**, **Thymeleaf**, and **MySQL/H2**.

---

## ✨ Features

### 🌌 Design & Visuals
* **Premium Glassmorphic UI:** Modern dark neon layout featuring glass panels, backdrop blur filters, glowing ambient backlights, and floating transitions.
* **Modern Typography & Icons:** Clean font rendering using *Outfit* via Google Fonts and vector icons via *FontAwesome*.
* **Responsive Layouts:** Fluid alignments across mobile, tablet, and desktop interfaces.

---

### 🛒 Customer Experience
* **Dynamic Welcome Banner:** Greets logged-in customers dynamically with their full names and an active digital date tracker.
* **Dynamic Account Statistics Grid:** Instant metric displays for:
  * **🛍️ Total Purchased Books:** Sum of all past purchases.
  * **💳 Total Expenditure:** Total currency spent (formatted elegantly in Rupees, e.g. `₹1,250.00`).
  * **🛒 Shopping Cart Items:** Real-time indicator of active cart products.
* **Available Books Catalog:** Explore and search the store library, view prices/details, and add copies directly to the cart.
* **Interactive Shopping Cart:** Review chosen items, adjust order quantities dynamically, and check out.
* **Successful Checkout Invoice:** Beautiful, printable success billing receipt immediately following order completions.
* **Complete Purchase History:** Interactive transaction table listed directly on the home dashboard.

---

### 👑 Admin / Seller Panel
* **Comprehensive Executive Dashboard:** Full overview of all critical business parameters in one control panel.
* **Business intelligence Grid (4-Stat Grid):**
  * **Total Sales Revenue (Green Glow):** Sum of all purchases globally on the platform.
  * **Orders Processed (Blue Glow):** Total number of catalog sales completed.
  * **Unique Book Titles (Teal Glow):** Count of catalog variations available in the library.
  * **Registered Users (Red Glow):** Count of registered customer accounts.
* **⚠️ Critical Low Stock replenishment Alert:** Automatically warns and displays items with `< 10` copies left in inventory, offering a direct quick-restock option.
* **📋 Global Sales Ledger:** Real-time customer transaction feed showing purchaser email, book, subtotal, and exact order timestamps.
* **✏️ Inventory Management Tools:** Clean custom card panels to seamlessly search inventory, register new titles, modify pricing/authors, and delete barcodes.

---

## 🛠️ Technology Stack

* **Back-End:** Java 21, Spring Boot 3.2.5, Spring Data JPA
* **Front-End:** Thymeleaf templating, HTML5, Bootstrap 4, Custom Glassmorphic CSS3
* **Databases:** 
  * MySQL (Primary Local & Production Database)
  * H2 (In-Memory DB supported for fast developer setups)
* **Build System:** Gradle

---

## 🚀 Getting Started

### 📋 Prerequisites
* **Java SDK 21** or higher.
* **MySQL Server** (optional, fallback H2 requires zero setup).

---

### ⚙️ Database Configuration Setup

By default, the application is pre-configured to run with a local **MySQL** database. All configuration properties are managed in [src/main/resources/application.properties](file:///c:/Users/Raghu%20Ram/Desktop/Online-Book-Store-main/src/main/resources/application.properties).

#### Option A: Running with MySQL (Default)
1. Ensure your local MySQL server is running.
2. The app will automatically try to connect to:
   * **URL:** `jdbc:mysql://localhost:3306/onlinebookstore` (it will auto-create the database if it doesn't exist).
   * **Username:** `root`
   * **Password:** `My_sql_WorkBench_root_Password`
3. If your MySQL credentials are different, you can customize them easily using a `.env` file (see the [Environment Variables](#-environment-variables) section below).

#### Option B: Running with H2 In-Memory DB (Zero-Setup)
If you do not have MySQL installed, you can switch to a lightweight In-Memory database:
1. Open [application.properties](file:///c:/Users/Raghu%20Ram/Desktop/Online-Book-Store-main/src/main/resources/application.properties).
2. Comment out the **MySQL Configuration** section (lines 5–9).
3. Uncomment the **H2 Memory DB Configs** section (lines 20–25).
4. Run the project!

---

### 🔑 Environment Variables
This project supports standard environment overrides for sensitive credentials. You can create a file named `.env` in the project root folder based on the provided [.env.example](file:///c:/Users/Raghu%20Ram/Desktop/Online-Book-Store-main/.env.example):

```env
# Server Port
PORT=8081

# Database Credentials
DB_URL=jdbc:mysql://localhost:3306/onlinebookstore?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

---

### ⚙️ Running the Project

1. Open your terminal in the root folder of the project.
2. Run the following command:

**On Windows (Command Prompt / PowerShell):**
```powershell
.\gradlew.bat bootRun
```

**On macOS / Linux (Terminal):**
```bash
chmod +x gradlew
./gradlew bootRun
```

3. Once the terminal displays `Started OnlineBookStoreApplication`, open your web browser and navigate to:
   👉 **[http://localhost:8081](http://localhost:8081)**

---

## 📂 Project Structure

```text
Online-Book-Store-main/
│
├── .env.example                 # Environment variables config template
├── build.gradle                 # Gradle dependencies & version configurations
├── gradlew / gradlew.bat        # Gradle wrappers for executing tasks
│
├── src/
│   └── main/
│       ├── java/com/bittercode/
│       │   ├── controller/      # Web controller mapping handlers
│       │   ├── model/           # Jpa models (Book, User, Order, Cart)
│       │   ├── repository/      # Spring Data Jpa database queries
│       │   ├── service/         # Business logic layer interfaces
│       │   └── config/          # DataLoader for mock startup credentials
│       │
│       └── resources/
│           ├── static/          # Custom stylesheets (styles.css) and assets
│           ├── templates/       # Glassmorphic Thymeleaf HTML files
│           └── application.properties  # Central application configurations
```

---

## 🧪 Demo Login Credentials

The application uses [DataLoader.java](file:///c:/Users/Raghu%20Ram/Desktop/Online-Book-Store-main/src/main/java/com/bittercode/config/DataLoader.java) to automatically seed default customer and admin/seller accounts upon startup for quick testing:

### 👤 Customer Login Profile:
* **Role:** Customer
* **Username/Email:** `Ram@gmail.com`
* **Password:** `Ram@1784`

### 🔑 Admin/Seller Login Profile:
* **Role:** Admin / Seller
* **Username/Email:** `admin`
* **Password:** `admin`
