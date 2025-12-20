package panel;


	import javax.swing.*;
	import javax.swing.table.DefaultTableModel;
	import java.awt.*;
	import java.awt.event.*;
	import java.sql.*;

	public class PaymentsPanel extends JPanel {

	    private Connection conn;

	    private JTable paymentsTable;
	    private DefaultTableModel paymentsModel;

	    public PaymentsPanel(Connection conn) {
	        this.conn = conn;
	        setLayout(new BorderLayout());
	        setBackground(Color.WHITE);
	        initializeUI();
	        loadPayments();
	    }

	    private void initializeUI() {

	        JLabel title = new JLabel("PAYMENT MANAGEMENT", JLabel.CENTER);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
	        add(title, BorderLayout.NORTH);

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 25, 10));

	        JButton btnLoad = createButton("Load Payments", new Color(70, 130, 180));
	        btnLoad.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadPayments();
	            }
	        });

	        buttonPanel.add(btnLoad);
	        add(buttonPanel, BorderLayout.CENTER);

	        String[] columns = {
	                "Payment ID", "Order ID",
	                "Amount", "Payment Method",
	                "Status", "Payment Date"
	        };

	        paymentsModel = new DefaultTableModel(columns, 0);
	        paymentsTable = new JTable(paymentsModel);

	        JScrollPane scrollPane = new JScrollPane(paymentsTable);
	        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        add(scrollPane, BorderLayout.SOUTH);
	    }

	    private JButton createButton(String text, Color color) {
	        JButton button = new JButton(text);
	        button.setBackground(color);
	        button.setForeground(Color.WHITE);
	        button.setFocusPainted(false);
	        button.setFont(new Font("Arial", Font.BOLD, 12));
	        return button;
	    }

	    private void loadPayments() {
	        try {
	            paymentsModel.setRowCount(0);
	            String sql = "SELECT payment_id, order_id, amount, payment_method, status, payment_date FROM payments";
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(sql);

	            while (rs.next()) {
	                paymentsModel.addRow(new Object[]{
	                        rs.getInt("payment_id"),
	                        rs.getInt("order_id"),
	                        rs.getBigDecimal("amount"),
	                        rs.getString("payment_method"),
	                        rs.getString("status"),
	                        rs.getTimestamp("payment_date")
	                });
	            }
	        } catch (SQLException ex) {
	            showError("Error loading payments: " + ex.getMessage());
	        }
	    }

	    private void showError(String message) {
	        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

