import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class PhoneBookUI extends JFrame {
    private JTextField nameField, phoneField, emailField, addressField, filterField;
    private JTable contactTable;
    private DefaultTableModel model;
    private ArrayList<Contact> contactList = new ArrayList<>();
    private JButton addButton, editButton, deleteButton, searchButton, saveButton, loadButton, clearButton;
    private JRadioButton personalButton, workButton;
    private JCheckBox favoriteCheckBox;
    private JComboBox<String> filterTypeComboBox;

    public PhoneBookUI() {
        setTitle("Phone Book Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeComponents();
        setListeners();
    }

    private void initializeComponents() {
        getContentPane().setBackground(new Color(240, 248, 255)); // Alice Blue background

        // Top Panel Layout
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(255, 255, 255)); // White background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Adding Fields
        nameField = createFieldWithLabel(topPanel, gbc, "Name:", 0);
        phoneField = createFieldWithLabel(topPanel, gbc, "Phone:", 1);
        emailField = createFieldWithLabel(topPanel, gbc, "Email:", 2);
        addressField = createFieldWithLabel(topPanel, gbc, "Address:", 3);
        favoriteCheckBox = createCheckboxWithLabel(topPanel, gbc, "Favorite:", 4);

        // Contact Type (Personal/Work)
        gbc.gridx = 0;
        gbc.gridy = 5;
        topPanel.add(new JLabel("Contact Type:"), gbc);
        gbc.gridx = 1;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        personalButton = new JRadioButton("Personal", true);
        workButton = new JRadioButton("Work");
        ButtonGroup group = new ButtonGroup();
        group.add(personalButton);
        group.add(workButton);
        radioPanel.add(personalButton);
        radioPanel.add(workButton);
        topPanel.add(radioPanel, gbc);

        // Buttons for Adding and Clearing
        addButton = new JButton("Add Contact");
        clearButton = new JButton("Clear Fields");
        gbc.gridx = 0;
        gbc.gridy++;
        topPanel.add(addButton, gbc);
        gbc.gridx = 1;
        topPanel.add(clearButton, gbc);

        // Filter Options
        gbc.gridx = 0;
        gbc.gridy++;
        filterTypeComboBox = new JComboBox<>(new String[]{"All", "Personal", "Work"});
        filterField = new JTextField(15);
        topPanel.add(new JLabel("Filter by Type:"), gbc);
        gbc.gridx = 1;
        topPanel.add(filterTypeComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        topPanel.add(new JLabel("Search (Name/Phone):"), gbc);
        gbc.gridx = 1;
        topPanel.add(filterField, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Contact Table
        contactTable = new JTable();
        model = new DefaultTableModel(new Object[]{"Name", "Phone", "Email", "Address", "Type", "Favorite"}, 0);
        contactTable.setModel(model);
        JScrollPane scrollPane = new JScrollPane(contactTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel for Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(255, 255, 255));
        editButton = new JButton("Edit Contact");
        deleteButton = new JButton("Delete Contact");
        searchButton = new JButton("Search Contact");
        saveButton = new JButton("Save Contacts");
        loadButton = new JButton("Load Contacts");

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTextField createFieldWithLabel(JPanel panel, GridBagConstraints gbc, String labelText, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        JTextField field = new JTextField(15);
        panel.add(field, gbc);
        return field;
    }

    private JCheckBox createCheckboxWithLabel(JPanel panel, GridBagConstraints gbc, String labelText, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        JCheckBox checkBox = new JCheckBox();
        panel.add(checkBox, gbc);
        return checkBox;
    }

    private void setListeners() {
        addButton.addActionListener(e -> addContact());
        clearButton.addActionListener(e -> clearFields());
        deleteButton.addActionListener(e -> deleteContact());
        editButton.addActionListener(e -> editContact());
        searchButton.addActionListener(e -> searchContact());
        saveButton.addActionListener(e -> saveContacts());
        loadButton.addActionListener(e -> loadContacts());
        filterTypeComboBox.addActionListener(e -> updateTableModel());
        filterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateTableModel();
            }
        });
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String type = personalButton.isSelected() ? "Personal" : "Work";
        boolean favorite = favoriteCheckBox.isSelected();
        
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contact = new Contact(name, phone, email, address, type, favorite);
        contactList.add(contact);
        updateTableModel();
        clearFields();
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        favoriteCheckBox.setSelected(false);
        personalButton.setSelected(true);
    }

    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow != -1) {
            contactList.remove(selectedRow);
            updateTableModel();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow != -1) {
            Contact contact = contactList.get(selectedRow);
            nameField.setText(contact.getName());
            phoneField.setText(contact.getPhone());
            emailField.setText(contact.getEmail());
            addressField.setText(contact.getAddress());
            favoriteCheckBox.setSelected(contact.isFavorite());
            if (contact.getType().equals("Personal")) personalButton.setSelected(true);
            else workButton.setSelected(true);
            contactList.remove(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchContact() {
        updateTableModel(); // Filter contacts by search field text and type
    }

    private void saveContacts() {
        // Serialize and save contacts to file
    }

    private void loadContacts() {
        // Deserialize and load contacts from file
    }

    private void updateTableModel() {
        model.setRowCount(0);
        String searchQuery = filterField.getText().toLowerCase();
        String filterType = (String) filterTypeComboBox.getSelectedItem();
        for (Contact contact : contactList) {
            boolean matchesType = filterType.equals("All") || contact.getType().equalsIgnoreCase(filterType);
            boolean matchesSearch = contact.getName().toLowerCase().contains(searchQuery) ||
                                    contact.getPhone().toLowerCase().contains(searchQuery);
            if (matchesType && matchesSearch) {
                model.addRow(new Object[]{contact.getName(), contact.getPhone(), contact.getEmail(),
                        contact.getAddress(), contact.getType(), contact.isFavorite() ? "Yes" : "No"});
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PhoneBookUI app = new PhoneBookUI();
            app.setVisible(true);
        });
    }
}
