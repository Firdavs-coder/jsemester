import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.table.*;

public class LibraryFrame extends JFrame {

    private Library library;
    private final String dataFile = "books.txt";

    private JTable bookTable;
    private DefaultTableModel tableModel;

    private JTextArea detailArea;

    private JButton addBtn;
    private JButton deleteBtn;
    private JButton borrowReturnBtn;
    private JButton loadBtn;
    private JButton saveBtn;
    private JButton searchBtn;
    private JButton sortBtn;

    private JTextField searchField;

    private JLabel statusLabel;

    public LibraryFrame(Library library) {
        super("Library Management System – University of Miskolc");
        this.library = library;
        buildUI();
        refreshView();
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });
        setLayout(new BorderLayout(6, 6));
        setPreferredSize(new Dimension(900, 580));

        add(buildToolbar(),      BorderLayout.NORTH);
        add(buildCenterPanel(),  BorderLayout.CENTER);
        add(buildStatusBar(),    BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildToolbar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));

        addBtn         = new JButton("Add Book");
        deleteBtn      = new JButton("Delete Book");
        borrowReturnBtn = new JButton("Borrow / Return");
        loadBtn        = new JButton("Load from File");
        saveBtn        = new JButton("Save to File");

        searchField = new JTextField(14);
        searchBtn   = new JButton("Search");
        sortBtn     = new JButton("Sort by Title");

        addBtn.addActionListener(e -> onAddBook());
        deleteBtn.addActionListener(e -> onDeleteBook());
        borrowReturnBtn.addActionListener(e -> onBorrowReturn());
        loadBtn.addActionListener(e -> onLoad());
        saveBtn.addActionListener(e -> onSave());
        searchBtn.addActionListener(e -> onSearch());
        sortBtn.addActionListener(e -> onSort());

        panel.add(addBtn);
        panel.add(deleteBtn);
        panel.add(borrowReturnBtn);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(loadBtn);
        panel.add(saveBtn);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(new JLabel("Search:"));
        panel.add(searchField);
        panel.add(searchBtn);
        panel.add(sortBtn);

        return panel;
    }

    private JSplitPane buildCenterPanel() {
        String[] columns = {"ID", "Title", "Author", "Price (HUF)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setRowHeight(22);
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(220);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(160);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(90);

        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onSelectionChanged();
        });

        JScrollPane tableScroll = new JScrollPane(bookTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Books"));

        detailArea = new JTextArea(6, 40);
        detailArea.setEditable(false);
        detailArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        detailArea.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JScrollPane detailScroll = new JScrollPane(detailArea);
        detailScroll.setBorder(BorderFactory.createTitledBorder("Book Details"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, detailScroll);
        split.setResizeWeight(0.72);
        split.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        return split;
    }

    private JPanel buildStatusBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        statusLabel = new JLabel("Ready.");
        panel.add(statusLabel);
        return panel;
    }

    public void refreshView() {
        refreshView(library.getAllBooks());
    }

    private void refreshView(ArrayList<Book> books) {
        tableModel.setRowCount(0);
        for (Book b : books) {
            tableModel.addRow(new Object[]{
                b.getId(),
                b.getTitle(),
                b.getAuthor(),
                b.getPrice(),
                b.isBorrowed() ? "Borrowed" : "Available"
            });
        }
        detailArea.setText("");
        updateStatus();
    }

    private void updateStatus() {
        int total     = library.getBookCount();
        int available = library.getAvailableBooks().size();
        Book exp      = library.getMostExpensiveBook();
        String expInfo = (exp != null) ? exp.getTitle() + " (" + exp.getPrice() + " HUF)" : "N/A";
        statusLabel.setText("Total: " + total + " | Available: " + available +
                            " | Most expensive: " + expInfo);
    }

    private void onSelectionChanged() {
        int row = bookTable.getSelectedRow();
        if (row < 0) {
            detailArea.setText("");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        Book book = library.getBookById(id);
        if (book != null) {
            detailArea.setText(book.getDescription());
        }
    }

    private void onAddBook() {
        AddBookDialog dialog = new AddBookDialog(this);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        int id = library.getNextBookId();
        Book book = new Book(id, dialog.getBookTitle(), dialog.getAuthor(), dialog.getPrice(), false);
        library.addBook(book);
        refreshView();
        setStatus("Book \"" + book.getTitle() + "\" added with ID " + id + ".");
    }

    private void onDeleteBook() {
        int row = bookTable.getSelectedRow();
        if (row < 0) { showError("Please select a book to delete."); return; }
        int id    = (int) tableModel.getValueAt(row, 0);
        String title = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete \"" + title + "\"?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        library.removeBookById(id);
        refreshView();
        setStatus("Book \"" + title + "\" deleted.");
    }

    private void onBorrowReturn() {
        int row = bookTable.getSelectedRow();
        if (row < 0) { showError("Please select a book to borrow or return."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        Book book = library.getBookById(id);
        if (book == null) return;
        if (book.isBorrowed()) {
            book.returnItem();
            setStatus("\"" + book.getTitle() + "\" returned.");
        } else {
            book.borrow();
            setStatus("\"" + book.getTitle() + "\" borrowed.");
        }
        refreshView();
        if (row < bookTable.getRowCount()) {
            bookTable.setRowSelectionInterval(row, row);
        }
    }

    private void onLoad() {
        JFileChooser chooser = new JFileChooser(".");
        chooser.setDialogTitle("Load Books from File");
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;
        String path = chooser.getSelectedFile().getAbsolutePath();
        try {
            Library fresh = new Library();
            FileHandler.loadFromFile(fresh, path);
            this.library = fresh;
            refreshView();
            setStatus("Loaded " + library.getBookCount() + " books from " + path);
        } catch (IOException ex) {
            showError("Error loading file:\n" + ex.getMessage());
        }
    }

    private void onSave() {
        JFileChooser chooser = new JFileChooser(".");
        chooser.setDialogTitle("Save Books to File");
        chooser.setSelectedFile(new java.io.File(dataFile));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;
        String path = chooser.getSelectedFile().getAbsolutePath();
        try {
            FileHandler.saveToFile(library, path);
            setStatus("Saved " + library.getBookCount() + " books to " + path);
        } catch (IOException ex) {
            showError("Error saving file:\n" + ex.getMessage());
        }
    }

    private void onSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshView();
            setStatus("Search cleared – showing all books.");
            return;
        }
        ArrayList<Book> results = library.search(query);
        refreshView(results);
        setStatus("Search for \"" + query + "\": " + results.size() + " result(s).");
    }

    private void onSort() {
        ArrayList<Book> sorted = new ArrayList<>(library.getAllBooks());
        sorted.sort(Comparator.comparing(Book::getTitle));
        refreshView(sorted);
        setStatus("Books sorted by title.");
    }

    private void onExit() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Save changes before exiting?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
        if (choice == JOptionPane.CANCEL_OPTION) return;
        if (choice == JOptionPane.YES_OPTION) {
            try {
                FileHandler.saveToFile(library, dataFile);
            } catch (IOException ex) {
                showError("Could not save file on exit:\n" + ex.getMessage());
            }
        }
        System.exit(0);
    }

    private void setStatus(String msg) {
        statusLabel.setText(msg);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
