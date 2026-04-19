import java.io.*;
public class FileHandler {

    public static void loadFromFile(Library library, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length != 5) {
                    throw new IOException("Invalid format on line " + lineNumber + ": \"" + line + "\"");
                }
                try {
                    int id         = Integer.parseInt(parts[0].trim());
                    String title   = parts[1].trim();
                    String author  = parts[2].trim();
                    int price      = Integer.parseInt(parts[3].trim());
                    boolean borrowed = Boolean.parseBoolean(parts[4].trim());
                    library.addBook(new Book(id, title, author, price, borrowed));
                } catch (NumberFormatException e) {
                    throw new IOException("Number parse error on line " + lineNumber + ": " + e.getMessage());
                }
            }
        }
    }

    public static void saveToFile(Library library, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : library.getAllBooks()) {
                writer.write(book.toString());
                writer.newLine();
            }
        }
    }
}
