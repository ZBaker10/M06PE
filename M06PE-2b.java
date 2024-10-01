import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class BatchUpdateComparison extends JFrame {
    private JButton connectButton = new JButton("Connect to Database");
    private JButton insertWithoutBatchButton = new JButton("Insert Without Batch");
    private JButton insertWithBatchButton = new JButton("Insert With Batch");

    private Connection connection;

    public BatchUpdateComparison() {
        setLayout(new FlowLayout());
        add(connectButton);
        add(insertWithoutBatchButton);
        add(insertWithBatchButton);

        DBConnectionPanel connectionPanel = new DBConnectionPanel();

        connectButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, connectionPanel, "Database Connection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                connection = connectionPanel.getConnection();
            }
        });

        insertWithoutBatchButton.addActionListener(e -> {
            if (connection == null) {
                JOptionPane.showMessageDialog(null, "Please connect to the database first!");
                return;
            }
            performInsertion(false);
        });

        insertWithBatchButton.addActionListener(e -> {
            if (connection == null) {
                JOptionPane.showMessageDialog(null, "Please connect to the database first!");
                return;
            }
            performInsertion(true);
        });
    }

    private void performInsertion(boolean useBatch) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)");
            Random random = new Random();
            
            long startTime = System.currentTimeMillis();

            for (int i = 0; i < 1000; i++) {
                double num1 = random.nextDouble();
                double num2 = random.nextDouble();
                double num3 = random.nextDouble();

                preparedStatement.setDouble(1, num1);
                preparedStatement.setDouble(2, num2);
                preparedStatement.setDouble(3, num3);

                if (useBatch) {
                    preparedStatement.addBatch();
                    if (i % 100 == 0) {
                        preparedStatement.executeBatch(); //execute batch every 100 inserts
                    }
                } else {
                    preparedStatement.executeUpdate();
                }
            }

            if (useBatch) {
                preparedStatement.executeBatch(); //execute any remaining inserts in the batch
            }

            long endTime = System.currentTimeMillis();
            JOptionPane.showMessageDialog(null, "Insertion " + (useBatch ? "with" : "without") + " batch completed in " + (endTime - startTime) + " ms");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BatchUpdateComparison frame = new BatchUpdateComparison();
        frame.setTitle("Batch Update Comparison");
        frame.setSize(400, 150);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}