package panel;

	import javax.swing.*;
	import javax.swing.table.DefaultTableModel;
	import java.awt.*;
	import java.awt.event.*;
	import java.sql.*;

	public class OrdersPanel extends JPanel {

	    private Connection conn;

	    // Order Components
	    private JTextField txtOrderID = new JTextField();
	    private JTextField txtOrderNumber = new JTextField();
	    private JComboBox cmbOrderStatus;

	    private JTable ordersTable;
	    private DefaultTableModel ordersModel;

	    public OrdersPanel(Connection conn) {
	        this.conn = conn;
	        setLayout(null);
	        setBackground(Color.WHITE);
	        initializeUI();
	        loadOrders();
	    }

	    private void initializeUI() {
	        JLabel title = new JLabel("ORDER MANAGEMENT");
	        title.setBounds(20, 10, 300, 25);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        add(title);

	        int y = 50;
	        addField("Order ID:", txtOrderID, 20, y); 
	        y += 35;
	        addField("Order Number:", txtOrderNumber, 20, y); 
	        y += 35;

	        JLabel lblStatus = new JLabel("Status:");
	        lblStatus.setBounds(20, y, 120, 25);
	        add(lblStatus);

	        cmbOrderStatus = new JComboBox(new String[]{
	                "pending", "confirmed", "processing",
	                "shipped", "delivered", "cancelled"
	        });
	        cmbOrderStatus.setBounds(150, y, 200, 25);
	        add(cmbOrderStatus);
	        y += 50;

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
	        buttonPanel.setBounds(400, y, 770, 40);
	        buttonPanel.setBackground(Color.WHITE);

	        JButton btnLoad = createButton("Load Orders", new Color(128, 0, 128));
	        btnLoad.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadOrders();
	            }
	        });

	        JButton btnUpdate = createButton("Update Status", new Color(70, 130, 180));
	        btnUpdate.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                updateOrderStatus();
	            }
	        });

	        JButton btnClear = createButton("Clear Form", new Color(169, 169, 169));
	        btnClear.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                clearOrderForm();
	            }
	        });

	        buttonPanel.add(btnLoad);
	        buttonPanel.add(btnUpdate);
	        buttonPanel.add(btnClear);
	        add(buttonPanel);

	        y += 50;

	        String[] columns = {
	                "Order ID", "Order Number", "User ID",
	                "Total Amount", "Status", "Order Date"
	        };
	        ordersModel = new DefaultTableModel(columns, 0);
	        ordersTable = new JTable(ordersModel);

	        JScrollPane scrollPane = new JScrollPane(ordersTable);
	        scrollPane.setBounds(20, y, 1150, 300);
	        add(scrollPane);
	    }

	    private void addField(String label, JTextField field, int x, int y) {
	        JLabel lbl = new JLabel(label);
	        lbl.setBounds(x, y, 120, 25);
	        field.setBounds(x + 130, y, 200, 25);
	        add(lbl);
	        add(field);
	    }

	    private JButton createButton(String text, Color color) {
	        JButton button = new JButton(text);
	        button.setBackground(color);
	        button.setForeground(Color.WHITE);
	        button.setFocusPainted(false);
	        button.setFont(new Font("Arial", Font.BOLD, 12));
	        return button;
	    }

	    private void loadOrders() {
	        try {
	            ordersModel.setRowCount(0);
	            String sql = "SELECT order_id, order_number, user_id, total_amount, status, order_date FROM orders";
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(sql);

	            while (rs.next()) {
	                ordersModel.addRow(new Object[]{
	                        rs.getInt("order_id"),
	                        rs.getString("order_number"),
	                        rs.getInt("user_id"),
	                        rs.getBigDecimal("total_amount"),
	                        rs.getString("status"),
	                        rs.getTimestamp("order_date")
	                });
	            }
	        } catch (SQLException ex) {
	            showError("Error loading orders: " + ex.getMessage());
	        }
	    }

	    private void updateOrderStatus() {
	        if (txtOrderID.getText().isEmpty()) {
	            showError("Please select an order.");
	            return;
	        }

	        try {
	            String sql = "UPDATE orders SET status=? WHERE order_id=?";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, cmbOrderStatus.getSelectedItem().toString());
	            ps.setInt(2, Integer.parseInt(txtOrderID.getText().trim()));

	            int rows = ps.executeUpdate();
	            if (rows > 0) {
	                JOptionPane.showMessageDialog(this, "Order status updated successfully!");
	                loadOrders();
	            } else {
	                showError("Order not found!");
	            }
	        } catch (SQLException ex) {
	            showError("Error updating order: " + ex.getMessage());
	        } catch (NumberFormatException ex) {
	            showError("Please enter a valid order ID.");
	        }
	    }

	    private void clearOrderForm() {
	        txtOrderID.setText("");
	        txtOrderNumber.setText("");
	        cmbOrderStatus.setSelectedIndex(0);
	    }

	    private void showError(String message) {
	        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
