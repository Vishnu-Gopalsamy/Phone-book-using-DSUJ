import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhoneBookHome extends JFrame {
    public PhoneBookHome() {
        // Set up frame
        setTitle("Home Section");
        setSize(350, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Phone Book \n\n\n", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 10, 10)); // 6 rows, 1 column with spacing

        // Buttons
        JButton entryButton = new JButton("Entry");
        JButton searchButton = new JButton("Search");
        JButton allContactsButton = new JButton("All Contacts");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton exitButton = new JButton("Exit");

        // Add buttons to panel
        buttonPanel.add(entryButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(allContactsButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exitButton);

        // Icon panel
        JPanel iconPanel = new JPanel();
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon("icon.png")); // Replace with your icon file path
        iconPanel.add(iconLabel);

        // Add panels to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(iconPanel, BorderLayout.EAST);

        // Add action listener for exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add main panel to frame
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PhoneBookHome frame = new PhoneBookHome();
            frame.setVisible(true);
        });
    }
}
