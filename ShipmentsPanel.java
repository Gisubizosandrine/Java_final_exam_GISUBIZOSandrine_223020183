package panel;


	import javax.swing.*;
	import javax.swing.table.DefaultTableModel;
	import java.awt.*;
	import java.awt.event.*;
	import java.sql.*;

	public class ShipmentsPanel extends JPanel {

	    private Connection conn;

	    private JTable shipmentsTable;
	    private DefaultTableModel shipmentsModel;

	    public ShipmentsPanel(Connection conn) {
	        this.conn = conn;
	        setLayout(new BorderLayout());
	        setBackground(Color.WHITE);
	        initializeUI();
	        loadShipments();
	    }

	    private void initializeUI() {

	        JLabel title = new JLabel("SHIPMENT MANAGEMENT", JLabel.CENTER);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
	        add(title, BorderLayout.NORTH);

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 25, 10));

	        JButton btnLoad = createButton("Load Shipments", new Color(70, 130, 180));
	        btnLoad.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadShipments();
	            }
	        });

	        buttonPanel.add(btnLoad);
	        add(buttonPanel, BorderLayout.CENTER);

	        String[] columns = {
	                "Shipment ID", "Order ID",
	                "Tracking Number", "Carrier",
	                "Status", "Shipment Date"
	        };

	        shipmentsModel = new DefaultTableModel(columns, 0);
	        shipmentsTable = new JTable(shipmentsModel);

	        JScrollPane scrollPane = new JScrollPane(shipmentsTable);
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

	    private void loadShipments() {
	        try {
	            shipmentsModel.setRowCount(0);
	            String sql = "SELECT shipment_id, order_id, tracking_number, carrier, status, shipment_date FROM shipments";
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(sql);

	            while (rs.next()) {
	                shipmentsModel.addRow(new Object[]{
	                        rs.getInt("shipment_id"),
	                        rs.getInt("order_id"),
	                        rs.getString("tracking_number"),
	                        rs.getString("carrier"),
	                        rs.getString("status"),
	                        rs.getTimestamp("shipment_date")
	                });
	            }
	        } catch (SQLException ex) {
	            showError("Error loading shipments: " + ex.getMessage());
	        }
	    }

	    private void showError(String message) {
	        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
