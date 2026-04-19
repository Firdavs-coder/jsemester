import java.io.IOException;
import javax.swing.*;

public class LibraryApp {
    private static final String DATA_FILE = "books.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Library library = new Library();

            try {
                FileHandler.loadFromFile(library, DATA_FILE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                    "Could not load data file:\n" + e.getMessage() +
                    "\n\nStarting with an empty library.",
                    "Load Warning", JOptionPane.WARNING_MESSAGE);
            }

            LibraryFrame frame = new LibraryFrame(library);
            frame.setVisible(true);
        });
    }
}
