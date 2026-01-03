# POS Simple UMKM - Prima Elektronik

A comprehensive Point of Sales (POS) system designed to streamline day-to-day operations for small to medium-sized electronics retailers (UMKM). This application handles inventory, sales transactions, financial reporting, and billing management.

DEMO: https://pos.widifirmaan.web.id

Admin: 
Username: hamam
Password: 123

Kasir: 
Username: kasir
Password: 123


## 🚀 Features

### 📦 Stock Management (Inventory)
*   **Real-time Stock Tracking**: Monitor product quantities across multiple stores.
*   **Product Management**: Add, edit, and delete product details (Name, Price, Quantity).
*   **Search**: Quickly find products by name or ID.

### 💰 Finance & Transactions
*   **Point of Sale**: Process sales with multiple payment methods (Cash, QRIS, Transfer).
*   **Transaction History**: View detailed logs of all sales.
*   **Reports**: Filter transactions by date range and store location.
*   **Financial Insights**: View "Laba Kotor" (Gross Profit) and total items sold.

### 🧾 Billing & Invoicing
*   **Bill Tracking**: Manage bills for sales, including due dates and payment status (Lunas, Belum Bayar, Belum Lunas).
*   **Payment Proof**: Upload and view images of payment proofs/receipts.
*   **Status Management**: Easily update bill statuses and track outstanding balances.

### 🖨️ Pricing & Export
*   **Receipt Printing**: Print thermal receipts for customers.
*   **Report Export**: Export transaction tables to **Excel (.xlsx)** for further analysis.
*   **Print Reports**: Print filtered transaction reports directly from the browser.

## 🛠️ Tech Stack

*   **Backend**: Java 17+, Spring Boot 3.2.4
*   **Database**: MongoDB
*   **Frontend**: Thymeleaf, HTML5, Bootstrap 5, JavaScript
*   **Build Tool**: Maven

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

*   [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/downloads/) or higher
*   [MongoDB](https://www.mongodb.com/try/download/community) (running locally on default port `27017`)
*   [Maven](https://maven.apache.org/download.cgi)

## ⚙️ Installation & Running

1.  **Clone the repository**
    ```bash
    git clone https://github.com/widifirmaan/Simple-POS-Springboot.git
    cd Simple-POS-Springboot/spring-boot-app
    ```

2.  **Configure Database**
    Ensure MongoDB is running. The application connects to `mongodb://localhost:27017/pos` by default. You can change this in `src/main/resources/application.properties`.

3.  **Build the Project**
    ```bash
    mvn clean package
    ```

4.  **Run the Application**
    Using Maven:
    ```bash
    mvn spring-boot:run
    ```
    OR using the built JAR file:
    ```bash
    java -jar target/pos-0.0.1-SNAPSHOT.jar
    ```

5.  **Access the Application**
    Open your browser and navigate to:
    ```
    http://localhost:8081
    ```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License.
