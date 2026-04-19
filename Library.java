import java.util.ArrayList;
public class Library {
    private ArrayList<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book b) {
        books.add(b);
    }

    public boolean removeBookById(int id) {
        return books.removeIf(book -> book.getId() == id);
    }

    public int getBookCount() {
        return books.size();
    }

    public Book getBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public ArrayList<Book> getAllBooks() {
        return books;
    }

    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> available = new ArrayList<>();
        for (Book book : books) {
            if (!book.isBorrowed()) {
                available.add(book);
            }
        }
        return available;
    }

    public Book getMostExpensiveBook() {
        if (books.isEmpty()) return null;
        Book mostExpensive = books.get(0);
        for (Book book : books) {
            if (book.getPrice() > mostExpensive.getPrice()) {
                mostExpensive = book;
            }
        }
        return mostExpensive;
    }

    public boolean idExists(int id) {
        return getBookById(id) != null;
    }

    public ArrayList<Book> search(String query) {
        ArrayList<Book> results = new ArrayList<>();
        String q = query.toLowerCase();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(q) ||
                book.getAuthor().toLowerCase().contains(q)) {
                results.add(book);
            }
        }
        return results;
    }
}
