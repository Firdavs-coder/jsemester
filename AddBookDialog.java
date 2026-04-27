import java.awt.*;
import javax.swing.*;

public class AddBookDialog extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private JTextField priceField;
    private boolean confirmed = false;

    public AddBookDialog(JFrame parent) {
        super(parent, "Add New Book", true);
        buildUI();
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        form.add(new JLabel("Title:"));
        titleField = new JTextField();
        form.add(titleField);

        form.add(new JLabel("Author:"));
        authorField = new JTextField();
        form.add(authorField);

        form.add(new JLabel("Price (HUF):"));
        priceField = new JTextField();
        form.add(priceField);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 16, 12, 16));

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> onAdd());
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        buttons.add(addBtn);
        buttons.add(cancelBtn);
        add(buttons, BorderLayout.SOUTH);
    }

    private void onAdd() {
        String title     = titleField.getText().trim();
        String author    = authorField.getText().trim();
        String priceText = priceField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int price = Integer.parseInt(priceText);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Price must be a non-negative whole number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }

    public String getBookTitle() { return titleField.getText().trim(); }
    public String getAuthor()    { return authorField.getText().trim(); }
    public int getPrice()        { return Integer.parseInt(priceField.getText().trim()); }
}
