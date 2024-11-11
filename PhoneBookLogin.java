import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhoneBookLogin extends JFrame implements ActionListener {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton, cancelButton;
    private JCheckBox showPasswordCheckBox;

    public PhoneBookLogin() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Title Label
        JLabel titleLabel = new JLabel("Welcome to PhoneBook");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(100, 20, 250, 30);
        add(titleLabel);

        // User Name Label
        JLabel userLabel = new JLabel("User Name");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setBounds(50, 80, 100, 25);
        add(userLabel);

        // User Name Text Field
        userField = new JTextField();
        userField.setBounds(150, 80, 150, 25);
        add(userField);

        // Password Label
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setBounds(50, 120, 100, 25);
        add(passLabel);

        // Password Field
        passField = new JPasswordField();
        passField.setBounds(150, 120, 150, 25);
        add(passField);

        // Show Password Checkbox
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBounds(150, 150, 150, 20);
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passField.setEchoChar((char) 0); // Show password
            } else {
                passField.setEchoChar('*'); // Hide password
            }
        });
        add(showPasswordCheckBox);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(90, 180, 80, 30);
        loginButton.addActionListener(this);
        add(loginButton);

        // Cancel Button
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(180, 180, 80, 30);
        cancelButton.addActionListener(this);
        add(cancelButton);

        // Avatar Icon
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon("D:/project/kdl/img/icon.png")); // Replace with your icon path
        iconLabel.setBounds(310, 80, 64, 64);
        add(iconLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            // Placeholder authentication
            if (username.equals("admin") && password.equals("admin")) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                
                // Proceed to PhoneBookHomeScreen
                new PhoneBookApp().setVisible(true); // Open PhoneBookHomeScreen
                this.dispose(); // Close the login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
        } else if (e.getSource() == cancelButton) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PhoneBookLogin().setVisible(true);
        });
    }
}
