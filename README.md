# Library Management System
**University of Miskolc – Java Programming – Semester Project**
Course code: GEIAL31A-B2a

---

## Project Structure

```
LibraryApp/
├── src/
│   ├── Item.java           – Abstract base class (Model Layer)
│   ├── Borrowable.java     – Interface for borrowable items
│   ├── Book.java           – Concrete class extending Item + Borrowable
│   ├── Library.java        – Collection handler (ArrayList<Book>)
│   ├── FileHandler.java    – File I/O with BufferedReader / BufferedWriter
│   ├── AddBookDialog.java  – JDialog for adding books (Bonus)
│   ├── LibraryFrame.java   – Main Swing JFrame GUI
│   └── LibraryApp.java     – Entry point (main method)
├── books.txt               – Sample data file
├── run.bat                 – Build & run script (Windows)
├── run.sh                  – Build & run script (Linux / macOS)
└── README.md
```