#!/bin/bash
echo "=== Library Management System – Build Script ==="

mkdir -p out

echo "Compiling..."
javac -d out src/Item.java src/Borrowable.java src/Book.java src/Library.java \
             src/FileHandler.java src/AddBookDialog.java src/LibraryFrame.java src/LibraryApp.java

if [ $? -ne 0 ]; then
    echo "Compilation FAILED."
    exit 1
fi
echo "Compilation successful."

cp books.txt out/ 2>/dev/null || true

echo "Starting application..."
cd out && java LibraryApp
