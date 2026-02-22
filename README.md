# ⚡ POS Simple UMKM - Modern Retail Management System

**POS Simple UMKM** is a comprehensive Point of Sales (POS) system designed to streamline day-to-day operations for small to medium-sized electronics retailers (UMKM). Built with **Spring Boot** and **MongoDB**, it provides a robust solution for inventory management, sales transactions, and financial reporting.

![Status](https://img.shields.io/badge/Status-Active-success?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.4-green?style=for-the-badge&logo=spring)
![MongoDB](https://img.shields.io/badge/MongoDB-Latest-forestgreen?style=for-the-badge&logo=mongodb)
![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk)

---

## 📸 Application Showcase

Explore the features of **POS Simple UMKM** through the interface gallery.

| | |
|:---:|:---:|
| ![Login](screenshots/login.png)<br>**Secure Login** | ![Dashboard](screenshots/dashboard.png)<br>**Admin Dashboard** |
| ![Kasir](screenshots/kasir.png)<br>**Point of Sale Interface** | ![Stock Barang](screenshots/stock.png)<br>**Inventory Management** |
| ![Bill](screenshots/bill.png)<br>**Billing & Invoicing** | ![Finance](screenshots/finance.png)<br>**Financial Reporting** |

---

## 🚀 Features Overview

### 📦 Stock Management (Inventory)
*   **Real-time Stock Tracking**: Monitor product quantities across multiple stores.
*   **Product Management**: Add, edit, and delete product details (Name, Price, Quantity).
*   **Quick Search**: Instantly find products by name or unique ID.

### 💰 Finance & Transactions
*   **Point of Sale**: Process sales with multiple payment methods (Cash, QRIS, Transfer).
*   **Transaction History**: View detailed logs of all sales activities.
*   **Smart Reports**: Filter transactions by date range and store location.
*   **Financial Insights**: Track gross profit (Laba Kotor) and total volume sold.

### 🧾 Billing & Invoicing
*   **Bill Tracking**: Manage sales bills with due dates and payment status (Lunas, Belum Bayar/Lunas).
*   **Digital Proof**: Upload and store images of payment receipts for verification.
*   **Status Management**: Seamlessly update billing states and track outstanding balances.

### 🖨️ Pricing & Export
*   **Thermal Printing**: Generate and print physical receipts for customers.
*   **Excel Export**: Download transaction data as **.xlsx** files for advanced analysis.
*   **Print Reports**: Print filtered reports directly from your browser.

---

## 🛠 Tech Stack

### Backend & Database
*   **Framework**: Java 17+ & Spring Boot 3.2.4
*   **Database**: MongoDB (NoSQL)
*   **Build Tool**: Maven

### Frontend & UI
*   **Templating**: Thymeleaf
*   **Styling**: Bootstrap 5 & Custom CSS
*   **Logic**: JavaScript (Vanilla)

---

## 📂 Project Structure

```bash
/
├── src/
│   ├── main/
│   │   ├── java/        # Core Application Logic
│   │   └── resources/   # Templates, Statics, and Config
│   └── test/           # Unit and Integration Tests
├── screenshots/         # Project Documentation Images
├── pom.xml              # Maven Configuration
└── README.md            # Documentation
```

---

## 📦 Getting Started

### Prerequisites
*   **JDK 17+**
*   **MongoDB** (running on port `27017`)
*   **Maven**

### 1. Database Configuration
Ensure MongoDB is running. The application connects to `mongodb://localhost:27017/primaelectronic` by default. Update connection strings in:
`src/main/resources/application.properties`

### 2. Build & Run
```bash
# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run
```

### 3. Access & Login
Open `http://localhost:8081` in your browser.

**Admin Credentials:**
*   **Username:** `hamam`
*   **Password:** `123`

**Kasir Credentials:**
*   **Username:** `kasir`
*   **Password:** `123`

---

## 👥 Authors

Developed by **Widi Firmaan**.

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📄 License

This project is licensed under the MIT License.
