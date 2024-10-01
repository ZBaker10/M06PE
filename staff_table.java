import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class StaffDatabaseManager extends JFrame {
    //JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:14193/zbaker";

    //database credentials
    static final String USER = "zbaker32";
    static final String PASS = "Password123";
    
    private JTextField idField = new JTextField(9);
    private JTextField lastNameField = new JTextField(15);
    private JTextField firstNameField = new JTextField(15);
    private JTextField miField = new JTextField(1);
    private JTextField addressField = new JTextField(20);
    private JTextField cityField = new JTextField(20);
    private JTextField stateField = new JTextField(2);
    private JTextField telephoneField = new JTextField(10);
    private JTextField emailField = new JTextField(40);
    
    private JButton viewButton = new JButton("View");
    private JButton insertButton = new JButton("Insert");
    private JButton updateButton = new JButton("Update");

    private Connection connection;
    private PreparedStatement preparedStatement;
    
    public StaffDatabaseManager() {
        //establish JDBC connection
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //panel for fields
        JPanel panel = new JPanel(new GridLayout(9, 2));
        panel.add(new JLabel("ID"));
        panel.add(idField);
        panel.add(new JLabel("Last Name"));
        panel.add(lastNameField);
        panel.add(new JLabel("First Name"));
        panel.add(firstNameField);
        panel.add(new JLabel("MI"));
        panel.add(miField);
        panel.add(new JLabel("Address"));
        panel.add(addressField);
        panel.add(new JLabel("City"));
        panel.add(cityField);
        panel.add(new JLabel("State"));
        panel.add(stateField);
        panel.add(new JLabel("Telephone"));
        panel.add(telephoneField);
        panel.add(new JLabel("Email"));
        panel.add(emailField);

        //panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);
        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        //add action listeners
        viewButton.addActionListener(e -> viewRecord());
        insertButton.addActionListener(e -> insertRecord());
        updateButton.addActionListener(e -> updateRecord());
    }

    private void viewRecord() {
        String id = idField.getText();
        try {
            String query = "SELECT * FROM Staff WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                lastNameField.setText(resultSet.getString("lastName"));
                firstNameField.setText(resultSet.getString("firstName"));
                miField.setText(resultSet.getString("mi"));
                addressField.setText(resultSet.getString("address"));
                cityField.setText(resultSet.getString("city"));
                stateField.setText(resultSet.getString("state"));
                telephoneField.setText(resultSet.getString("telephone"));
                emailField.setText(resultSet.getString("email"));
            } else {
                JOptionPane.showMessageDialog(null, "No record found for ID: " + id);
            }
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void insertRecord() {
        try {
            String query = "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, idField.getText());
            preparedStatement.setString(2, lastNameField.getText());
            preparedStatement.setString(3, firstNameField.getText());
            preparedStatement.setString(4, miField.getText());
            preparedStatement.setString(5, addressField.getText());
            preparedStatement.setString(6, cityField.getText());
            preparedStatement.setString(7, stateField.getText());
            preparedStatement.setString(8, telephoneField.getText());
            preparedStatement.setString(9, emailField.getText());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Record inserted successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateRecord() {
        try {
            String query = "UPDATE Staff SET lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, state = ?, telephone = ?, email = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, lastNameField.getText());
            preparedStatement.setString(2, firstNameField.getText());
            preparedStatement.setString(3, miField.getText());
            preparedStatement.setString(4, addressField.getText());
            preparedStatement.setString(5, cityField.getText());
            preparedStatement.setString(6, stateField.getText());
            preparedStatement.setString(7, telephoneField.getText());
            preparedStatement.setString(8, emailField.getText());
            preparedStatement.setString(9, idField.getText());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Record updated successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        StaffDatabaseManager frame = new StaffDatabaseManager();
        frame.setTitle("Staff Database Manager");
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}