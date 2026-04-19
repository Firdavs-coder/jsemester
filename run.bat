@echo off
echo === Library Management System – Build Script ===

if not exist out mkdir out

echo Compiling...
javac -d out Item.java Borrowable.java Book.java Library.java FileHandler.java AddBookDialog.java LibraryFrame.java LibraryApp.java
if %errorlevel% neq 0 (
    echo Compilation FAILED.
    pause
    exit /b 1
)
echo Compilation successful.

copy books.txt out\ >nul 2>&1

echo Starting application...
cd out
java LibraryApp
cd ..
