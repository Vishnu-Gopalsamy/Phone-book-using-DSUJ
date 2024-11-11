import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils; // Ensure to add the Apache Commons Collections library

public class PhoneBookApp extends JFrame {
    private JTextField nameField, phoneField, emailField, addressField, filterField;
    private JTable contactTable;
    private DefaultTableModel model;
    private ArrayList<Contact> contactList = new ArrayList<>();
    private JButton addButton, editButton, deleteButton, searchButton, saveButton, loadButton, clearButton;
    private JRadioButton personalButton, workButton;
    private JCheckBox favoriteCheckBox;
    private JComboBox<String> filterTypeComboBox;

    public PhoneBookApp() {
        setTitle("Phone Book Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(240, 248, 255)); // Alice Blue

        JPanel topPanel = new JPanel(new GridLayout(8, 2));
        topPanel.setBackground(new Color(255, 255, 255)); // White
        topPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        topPanel.add(nameField);

        topPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        topPanel.add(phoneField);

        topPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        topPanel.add(emailField);

        topPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        topPanel.add(addressField);

        topPanel.add(new JLabel("Contact Type:"));
        JPanel radioPanel = new JPanel();
        personalButton = new JRadioButton("Personal", true);
        workButton = new JRadioButton("Work");
        ButtonGroup group = new ButtonGroup();
        group.add(personalButton);
        group.add(workButton);
        radioPanel.add(personalButton);
        radioPanel.add(workButton);
        topPanel.add(radioPanel);

        favoriteCheckBox = new JCheckBox("Favorite");
        topPanel.add(favoriteCheckBox);

        addButton = new JButton("Add Contact");
        topPanel.add(addButton);

        clearButton = new JButton("Clear Fields");
        topPanel.add(clearButton);

        // Filter options
        topPanel.add(new JLabel("Filter by Type:"));
        filterTypeComboBox = new JComboBox<>(new String[]{"All", "Personal", "Work"});
        topPanel.add(filterTypeComboBox);

        topPanel.add(new JLabel("Search (Name/Phone):"));
        filterField = new JTextField();
        topPanel.add(filterField);

        add(topPanel, BorderLayout.NORTH);

        contactTable = new JTable();
        model = new DefaultTableModel(new Object[]{"Name", "Phone", "Email", "Address", "Type", "Favorite"}, 0);
        contactTable.setModel(model);
        JScrollPane scrollPane = new JScrollPane(contactTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(255, 255, 255)); // White
        editButton = new JButton("Edit Contact");
        deleteButton = new JButton("Delete Contact");
        searchButton = new JButton("Search Contact");
        saveButton = new JButton("Save Contacts");
        loadButton = new JButton("Load Contacts");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addContact());
        clearButton.addActionListener(e -> clearFields());
        deleteButton.addActionListener(e -> deleteContact());
        editButton.addActionListener(e -> editContact());
        searchButton.addActionListener(e -> searchContact());
        saveButton.addActionListener(e -> saveContacts());
        loadButton.addActionListener(e -> loadContacts());

        filterTypeComboBox.addActionListener(e -> updateTableModel()); // Update table when filter changes
        filterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateTableModel(); // Update table when search field changes
            }
        });
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String type = personalButton.isSelected() ? "Personal" : "Work";
        boolean isFavorite = favoriteCheckBox.isSelected();

        if (!name.isEmpty() && !phone.isEmpty()) {
            Contact contact = new Contact(name, phone, email, address, type, isFavorite);
            contactList.add(contact);
            
            // Sort the contact list after adding a new contact
            sortContacts();
            updateTableModel();
            
            clearFields();
        } else {
            JOptionPane.showMessageDialog(null, "Name and Phone are required fields.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        personalButton.setSelected(true);
        favoriteCheckBox.setSelected(false);
        filterField.setText(""); // Clear filter field
        filterTypeComboBox.setSelectedIndex(0); // Reset filter type
        updateTableModel(); // Update table after clearing
    }

    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow >= 0) {
            contactList.remove(selectedRow);
            updateTableModel();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a contact to delete.");
        }
    }

    private void editContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow >= 0) {
            String newName = JOptionPane.showInputDialog("Enter new name:", model.getValueAt(selectedRow, 0));
            String newPhone = JOptionPane.showInputDialog("Enter new phone:", model.getValueAt(selectedRow, 1));
            String newEmail = JOptionPane.showInputDialog("Enter new email:", model.getValueAt(selectedRow, 2));
            String newAddress = JOptionPane.showInputDialog("Enter new address:", model.getValueAt(selectedRow, 3));
            String newType = JOptionPane.showInputDialog("Enter new type (Personal/Work):", model.getValueAt(selectedRow, 4));
            boolean isFavorite = JOptionPane.showConfirmDialog(null, "Is this contact a favorite?", "Favorite", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

            Contact contact = contactList.get(selectedRow);
            contact.setName(newName);
            contact.setPhone(newPhone);
            contact.setEmail(newEmail);
            contact.setAddress(newAddress);
            contact.setType(newType);
            contact.setFavorite(isFavorite);
            
            // Sort after editing and update table
            sortContacts();
            updateTableModel();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a contact to edit.");
        }
    }

    private void searchContact() {
        String searchText = JOptionPane.showInputDialog("Enter name or phone to search:");
        
        // Sort contacts before searching
        sortContacts();

        // Perform binary search
        int index = Collections.binarySearch(contactList, new Contact(searchText, "", "", "", "", false),
                Comparator.comparing(Contact::getName).thenComparing(Contact::getPhone));

        if (index >= 0) {
            Contact contact = contactList.get(index);
            JOptionPane.showMessageDialog(null, "Contact Found: " + contact);
        } else {
            JOptionPane.showMessageDialog(null, "Contact not found.");
        }
    }

    private void saveContacts() {
        try (PrintWriter writer = new PrintWriter(new File("contacts.txt"))) {
            for (Contact contact : contactList) {
                writer.println(contact.getName() + "," + contact.getPhone() + "," + contact.getEmail() + "," + contact.getAddress() + "," + contact.getType() + "," + contact.isFavorite());
            }
            JOptionPane.showMessageDialog(null, "Contacts saved successfully.");
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error saving contacts.");
        }
    }

    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("contacts.txt"))) {
            String line;
            contactList.clear(); 
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    Contact contact = new Contact(data[0], data[1], data[2], data[3], data[4], Boolean.parseBoolean(data[5]));
                    contactList.add(contact);
                }
            }
            sortContacts(); // Sort contacts after loading
            updateTableModel();
            JOptionPane.showMessageDialog(null, "Contacts loaded successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading contacts.");
        }
    }

    private void sortContacts() {
        contactList.sort(Comparator.comparing(Contact::getName));
    }

    private void updateTableModel() {
        model.setRowCount(0); // Clear the current model
        String filterType = (String) filterTypeComboBox.getSelectedItem();
        String searchText = filterField.getText().toLowerCase();

        for (Contact contact : contactList) {
            boolean matchesType = filterType.equals("All") || contact.getType().equals(filterType);
            boolean matchesSearch = contact.getName().toLowerCase().contains(searchText) || contact.getPhone().contains(searchText);
            if (matchesType && matchesSearch) {
                model.addRow(new Object[]{contact.getName(), contact.getPhone(), contact.getEmail(), contact.getAddress(), contact.getType(), contact.isFavorite()});
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PhoneBookApp app = new PhoneBookApp();
            app.setVisible(true);
        });
    }
}

class Contact {
    private String name;
    private String phone;
    private String email;
    private String address;
    private String type; // Personal or Work
    private boolean favorite;

    public Contact(String name, String phone, String email, String address, String type, boolean favorite) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.type = type;
        this.favorite = favorite;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return String.format("Contact{name='%s', phone='%s', email='%s', address='%s', type='%s', favorite=%b}", name, phone, email, address, type, favorite);
    }
}
