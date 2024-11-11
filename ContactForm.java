import javax.swing.*;
import java.awt.*;

public class ContactForm {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContactForm::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Create Contact");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.DARK_GRAY);

        // Add picture button
        JPanel picturePanel = new JPanel();
        picturePanel.setBackground(Color.DARK_GRAY);
        JButton addPictureButton = new JButton("Add picture");
        addPictureButton.setPreferredSize(new Dimension(100, 100));
        picturePanel.add(addPictureButton);

        // Contact fields
        JTextField firstNameField = createTextField("First name");
        JTextField surnameField = createTextField("Surname");
        JTextField companyField = createTextField("Company");

        // Phone field with country code selector
        JPanel phonePanel = new JPanel();
        phonePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        phonePanel.setBackground(Color.DARK_GRAY);
        
        String[] countries = {"+91 (India)", "+1 (USA)", "+44 (UK)"};
        JComboBox<String> countryCode = new JComboBox<>(countries);
        JTextField phoneField = new JTextField(20);

        phonePanel.add(countryCode);
        phonePanel.add(phoneField);

        // Buttons for adding additional info
        JButton addPhoneButton = new JButton("Add phone");
        JButton addEmailButton = new JButton("Add email");
        JButton addBirthdayButton = new JButton("Add birthday");

        // Panel setup for the form layout
        mainPanel.add(picturePanel);
        mainPanel.add(firstNameField);
        mainPanel.add(surnameField);
        mainPanel.add(companyField);
        mainPanel.add(phonePanel);
        mainPanel.add(addPhoneButton);
        mainPanel.add(addEmailButton);
        mainPanel.add(addBirthdayButton);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(80, 30));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to frame
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(saveButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField(20);
        textField.setMaximumSize(new Dimension(350, 30));
        textField.setBorder(BorderFactory.createTitledBorder(placeholder));
        return textField;
    }
}
