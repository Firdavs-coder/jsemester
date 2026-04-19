public class Book extends Item implements Borrowable {
    private String author;
    private int price;
    private boolean borrowed;

    public Book(int id, String title, String author, int price, boolean borrowed) {
        super(id, title);
        this.author = author;
        this.price = price;
        this.borrowed = borrowed;
    }

    public String getAuthor() {
        return author;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean isBorrowed() {
        return borrowed;
    }

    @Override
    public void borrow() {
        this.borrowed = true;
    }

    @Override
    public void returnItem() {
        this.borrowed = false;
    }

    @Override
    public String getDescription() {
        return String.format(
            "ID: %d | Title: %s | Author: %s | Price: %d HUF | Status: %s",
            id, title, author, price, borrowed ? "Borrowed" : "Available"
        );
    }

    @Override
    public String toString() {
        return id + ";" + title + ";" + author + ";" + price + ";" + borrowed;
    }
}
