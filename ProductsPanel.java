package panel;



	import javax.swing.*;
	import javax.swing.table.DefaultTableModel;
	import java.awt.*;
	import java.awt.event.*;
	import java.sql.*;
	import java.math.BigDecimal;

	public class ProductsPanel extends JPanel {

	    private Connection conn;
	    private int userId;

	    // Product Components
	    private JTextField txtProductID = new JTextField();
	    private JTextField txtProductName = new JTextField();
	    private JTextField txtProductDesc = new JTextField();
	    private JTextField txtProductPrice = new JTextField();
	    private JTextField txtProductStock = new JTextField();
	    private JTextField txtProductCategory = new JTextField();
	    private JComboBox cmbProductStatus;

	    private JTable productsTable;
	    private DefaultTableModel productsModel;

	    public ProductsPanel(Connection conn, int userId) {
	        this.conn = conn;
	        this.userId = userId;
	        setLayout(null);
	        setBackground(Color.WHITE);
	        initializeUI();
	        loadProducts();
	    }

	    private void initializeUI() {
	        JLabel title = new JLabel("PRODUCT MANAGEMENT");
	        title.setBounds(20, 10, 300, 25);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        add(title);

	        int y = 50;
	        addField("Product ID:", txtProductID, 20, y); y += 35;
	        addField("Name*:", txtProductName, 20, y); y += 35;
	        addField("Description:", txtProductDesc, 20, y); y += 35;
	        addField("Price*:", txtProductPrice, 20, y); y += 35;
	        addField("Stock*:", txtProductStock, 20, y); y += 35;
	        addField("Category ID*:", txtProductCategory, 20, y); y += 35;

	        JLabel lblStatus = new JLabel("Status:");
	        lblStatus.setBounds(20, y, 120, 25);
	        add(lblStatus);

	        cmbProductStatus = new JComboBox(new String[]{"active", "inactive", "out_of_stock"});
	        cmbProductStatus.setBounds(150, y, 200, 25);
	        add(cmbProductStatus);
	        y += 50;

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
	        buttonPanel.setBounds(400, y, 770, 40);
	        buttonPanel.setBackground(Color.WHITE);

	        JButton btnAdd = createButton("Add Product", new Color(34, 139, 34));
	        btnAdd.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                addProduct();
	            }
	        });

	        JButton btnUpdate = createButton("Update Product", new Color(70, 130, 180));
	        btnUpdate.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                updateProduct();
	            }
	        });

	        JButton btnDelete = createButton("Delete Product", new Color(255, 200, 200));
	        btnDelete.setForeground(Color.DARK_GRAY);
	        btnDelete.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                deleteProduct();
	            }
	        });

	        JButton btnLoad = createButton("Load Products", new Color(128, 0, 128));
	        btnLoad.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadProducts();
	            }
	        });

	        JButton btnClear = createButton("Clear Form", new Color(169, 169, 169));
	        btnClear.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                clearProductForm();
	            }
	        });

	        buttonPanel.add(btnAdd);
	        buttonPanel.add(btnUpdate);
	        buttonPanel.add(btnDelete);
	        buttonPanel.add(btnLoad);
	        buttonPanel.add(btnClear);
	        add(buttonPanel);

	        y += 50;

	        String[] columns = {"ID", "Name", "Price", "Stock", "Category", "Status", "Created By"};
	        productsModel = new DefaultTableModel(columns, 0);
	        productsTable = new JTable(productsModel);
	        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	        productsTable.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e) {
	                selectProductFromTable();
	            }
	        });

	        JScrollPane scrollPane = new JScrollPane(productsTable);
	        scrollPane.setBounds(20, y, 1150, 250);
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

	    private void loadProducts() {
	        try {
	            productsModel.setRowCount(0);
	            String sql = "SELECT p.*, u.username AS created_by_name FROM products p " +
	                         "LEFT JOIN users u ON p.created_by = u.user_id";
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(sql);

	            while (rs.next()) {
	                productsModel.addRow(new Object[]{
	                        rs.getInt("product_id"),
	                        rs.getString("name"),
	                        rs.getBigDecimal("price"),
	                        rs.getInt("stock_quantity"),
	                        rs.getInt("category_id"),
	                        rs.getString("status"),
	                        rs.getString("created_by_name")
	                });
	            }
	        } catch (SQLException ex) {
	            showError("Error loading products: " + ex.getMessage());
	        }
	    }

	    private void addProduct() {
	        if (!validateProductForm()) return;

	        try {
	            String sql = "INSERT INTO products (name, description, category_id, price, stock_quantity, status, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, txtProductName.getText().trim());
	            ps.setString(2, txtProductDesc.getText().trim());
	            ps.setInt(3, Integer.parseInt(txtProductCategory.getText().trim()));
	            ps.setBigDecimal(4, new BigDecimal(txtProductPrice.getText().trim()));
	            ps.setInt(5, Integer.parseInt(txtProductStock.getText().trim()));
	            ps.setString(6, cmbProductStatus.getSelectedItem().toString());
	            ps.setInt(7, userId);

	            ps.executeUpdate();
	            JOptionPane.showMessageDialog(this, "Product added successfully!");
	            clearProductForm();
	            loadProducts();
	        } catch (Exception ex) {
	            showError("Error adding product: " + ex.getMessage());
	        }
	    }

	    private void updateProduct() {
	        if (txtProductID.getText().isEmpty()) {
	            showError("Please select a product to update.");
	            return;
	        }
	        if (!validateProductForm()) return;

	        try {
	            String sql = "UPDATE products SET name=?, description=?, category_id=?, price=?, stock_quantity=?, status=? WHERE product_id=?";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, txtProductName.getText().trim());
	            ps.setString(2, txtProductDesc.getText().trim());
	            ps.setInt(3, Integer.parseInt(txtProductCategory.getText().trim()));
	            ps.setBigDecimal(4, new BigDecimal(txtProductPrice.getText().trim()));
	            ps.setInt(5, Integer.parseInt(txtProductStock.getText().trim()));
	            ps.setString(6, cmbProductStatus.getSelectedItem().toString());
	            ps.setInt(7, Integer.parseInt(txtProductID.getText().trim()));

	            ps.executeUpdate();
	            JOptionPane.showMessageDialog(this, "Product updated successfully!");
	            clearProductForm();
	            loadProducts();
	        } catch (Exception ex) {
	            showError("Error updating product: " + ex.getMessage());
	        }
	    }

	    private void deleteProduct() {
	        if (txtProductID.getText().isEmpty()) {
	            showError("Please select a product to delete.");
	            return;
	        }

	        int confirm = JOptionPane.showConfirmDialog(this,
	                "Are you sure you want to delete this product?",
	                "Confirm Delete",
	                JOptionPane.YES_NO_OPTION);

	        if (confirm == JOptionPane.YES_OPTION) {
	            try {
	                String sql = "DELETE FROM products WHERE product_id=?";
	                PreparedStatement ps = conn.prepareStatement(sql);
	                ps.setInt(1, Integer.parseInt(txtProductID.getText().trim()));
	                ps.executeUpdate();

	                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
	                clearProductForm();
	                loadProducts();
	            } catch (Exception ex) {
	                showError("Error deleting product: " + ex.getMessage());
	            }
	        }
	    }

	    private void selectProductFromTable() {
	        int row = productsTable.getSelectedRow();
	        if (row >= 0) {
	            txtProductID.setText(productsModel.getValueAt(row, 0).toString());
	            txtProductName.setText(productsModel.getValueAt(row, 1).toString());
	            txtProductPrice.setText(productsModel.getValueAt(row, 2).toString());
	            txtProductStock.setText(productsModel.getValueAt(row, 3).toString());
	            txtProductCategory.setText(productsModel.getValueAt(row, 4).toString());
	            cmbProductStatus.setSelectedItem(productsModel.getValueAt(row, 5).toString());
	        }
	    }

	    private boolean validateProductForm() {
	        if (txtProductName.getText().trim().isEmpty() ||
	            txtProductPrice.getText().trim().isEmpty() ||
	            txtProductStock.getText().trim().isEmpty() ||
	            txtProductCategory.getText().trim().isEmpty()) {
	            showError("Please fill in all required fields (*).");
	            return false;
	        }
	        return true;
	    }

	    private void clearProductForm() {
	        txtProductID.setText("");
	        txtProductName.setText("");
	        txtProductDesc.setText("");
	        txtProductPrice.setText("");
	        txtProductStock.setText("");
	        txtProductCategory.setText("");
	        cmbProductStatus.setSelectedIndex(0);
	    }

	    private void showError(String message) {
	        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

