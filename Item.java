public abstract class Item {
    protected int id;
    protected String title;

    public Item(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public abstract String getDescription();
}
