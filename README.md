# 📱 Smart Budget Tracker

Smart Budget Tracker is an intuitive Kotlin-based Android application designed to help users manage their personal finances effectively. It provides features for tracking income, expenses, budgets, and also includes gamification and visualization tools to improve user experience.

---

## 🚀 Features

### ✅ Core Features
- **User Authentication** with Firebase (Sign up & Login)
- **Dashboard Overview**
  - Total Balance
  - Income & Expenses
  - Points and Level indicators
- **Transaction Management**
  - Add, view, and manage Income and Expense entries
  - Budget tracking by category
- **Budget Allocation**
  - Assign budgets to specific categories
  - Visual feedback when spending exceeds budget
- **Statistics Visualization**
  - Dynamic bar chart showing:
    - Total Income
    - Total Expenses
    - Remaining Balance
- **Room Database Integration**
  - All transactions and user statistics are stored locally

---

## 🌟 Custom Features

### 🌙 Dark Mode Support
- Users can toggle between **Light** and **Dark** mode using a built-in switch on the dashboard.
- Settings persist between app launches.

### 🧮 Built-in Calculator
- A **fully functional calculator** is accessible via the bottom navigation bar.
- Great for quick financial calculations without leaving the app.

---

## 🏆 Gamification
- Users earn **Points** and **Levels** based on financial activity and app engagement.
- A fun way to stay motivated while managing your money!

---

## 📊 Technologies Used
- **Kotlin**
- **Room Database** for local storage
- **Firebase Authentication**
- **Firebase Realtime Database**
- **MPAndroidChart** for statistical visualization
- **Material Design Components**
- **MVVM (where applicable)**

---

## 📂 Project Structure
- `MainActivity.kt` – Dashboard and navigation hub
- `TransactionActivity.kt` – View and manage all transactions
- `StatsActivity.kt` – Visual breakdown of financial data
- `CalculatorActivity.kt` – Basic calculator screen
- `AddIncomeActivity.kt`, `AddExpenseActivity.kt`, `AddBudgetActivity.kt` – Add transaction entries
- `Room` – DAO, Entity, and Database for local data persistence
- `Firebase` – Handles user authentication

---

## 🔧 Setup Instructions

1. Clone the repository:
