import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionPanel extends JPanel {
    private JTextField urlField = new JTextField("jdbc:mysql://localhost:14193/zbaker", 30);
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);
    private JButton connectButton = new JButton("Connect to Database");

    private Connection connection;

    public DBConnectionPanel() {
        setLayout(new GridLayout(4, 2));
        add(new JLabel("Database URL: "));
        add(urlField);
        add(new JLabel("Username: "));
        add(usernameField);
        add(new JLabel("Password: "));
        add(passwordField);
        add(new JLabel(""));
        add(connectButton);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connection = DriverManager.getConnection(urlField.getText(), usernameField.getText(), new String(passwordField.getPassword()));
                    JOptionPane.showMessageDialog(null, "Connected to the database successfully!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to connect to the database!");
                }
            }
        });
    }

    public Connection getConnection() {
        return connection;
    }
}