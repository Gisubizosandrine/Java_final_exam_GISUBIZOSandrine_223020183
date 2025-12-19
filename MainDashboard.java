package ecommerceform;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.math.BigDecimal;

public class MainDashboard extends JFrame {

	    private int userId;
	    private String fullName;
	    private String role;
	    private Connection conn;
	  
	    private JTabbedPane tabbedPane;
	    
	    // User Components
	    private JTextField txtUserID = new JTextField();
	    private JTextField txtUsername = new JTextField();
	    private JTextField txtEmail = new JTextField();
	    private JTextField txtFullName = new JTextField();
	    private JTextField txtUserRole = new JTextField();
	    private JTextField txtUserAddress = new JTextField();
	    private JTextField txtUserPhone = new JTextField();
	    private JComboBox cmbUserStatus;
	    private JTable usersTable;
	    private DefaultTableModel usersModel;
	    
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
	    
	    // Order Components
	    private JTextField txtOrderID = new JTextField();
	    private JTextField txtOrderNumber = new JTextField();
	    private JComboBox cmbOrderStatus;
	    private JTable ordersTable, paymentsTable, shipmentsTable, reviewsTable;
	    private DefaultTableModel ordersModel, paymentsModel, shipmentsModel, reviewsModel;
	    
	    // Buttons
	    private JButton btnLogout;

	    public MainDashboard(int userId, String fullName, String role) {
	        this.userId = userId;
	        this.fullName = fullName;
	        this.role = role;
	        this.conn = DB.getConnection();
	        
	        initializeUI();
	        loadAllData();
	    }

	    private void initializeUI() {
	        setTitle("E-commerce Platform - Main Dashboard");
	        setSize(1200, 700);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setLayout(new BorderLayout());

	        setupHeader();
	     
	        tabbedPane = new JTabbedPane();
	        setupUsersTab();    
	        setupProductsTab(); 
	        setupOrdersTab();
	        setupPaymentsTab();
	        setupShipmentsTab();
	        setupReviewsTab();
	        
	        add(tabbedPane, BorderLayout.CENTER);

	        setVisible(true);
	    }

	    private void setupHeader() {
	        JPanel headerPanel = new JPanel(new BorderLayout());
	        headerPanel.setBackground(new Color(240, 240, 240));
	        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        
	        JLabel lblTitle = new JLabel("E-COMMERCE PLATFORM DASHBOARD", JLabel.CENTER);
	        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
	        lblTitle.setForeground(Color.BLUE);
	        
	        JLabel lblUserInfo = new JLabel("Welcome, " + fullName + " | Role: " + role, JLabel.CENTER);
	        lblUserInfo.setFont(new Font("Arial", Font.PLAIN, 14));
	        
	        headerPanel.add(lblTitle, BorderLayout.NORTH);
	        headerPanel.add(lblUserInfo, BorderLayout.CENTER);
	        
	        btnLogout = new JButton("Logout");
	        btnLogout.setBackground(new Color(220, 80, 60));
	        btnLogout.setForeground(Color.WHITE);
	        btnLogout.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                logout();
	            }
	        });
	        headerPanel.add(btnLogout, BorderLayout.EAST);

	        add(headerPanel, BorderLayout.NORTH);
	    }

	    private void setupUsersTab() {
	        JPanel panel = new JPanel(null);
	        panel.setBackground(Color.WHITE);
	        JLabel title = new JLabel("USER MANAGEMENT");
	        title.setBounds(20, 10, 300, 25);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        panel.add(title);

	        int y = 50;
	        addField(panel, "User ID:", txtUserID, 20, y); y += 35;
	        addField(panel, "Username*:", txtUsername, 20, y); y += 35;
	        addField(panel, "Email*:", txtEmail, 20, y); y += 35;
	        addField(panel, "Full Name*:", txtFullName, 20, y); y += 35;
	        addField(panel, "Role:", txtUserRole, 20, y); y += 35;
	        addField(panel, "Address:", txtUserAddress, 20, y); y += 35;
	        addField(panel, "Phone:", txtUserPhone, 20, y); y += 35;
	        
	        JLabel lblStatus = new JLabel("Status:");
	        lblStatus.setBounds(20, y, 120, 25);
	        panel.add(lblStatus);
	        cmbUserStatus = new JComboBox(new String[]{"active", "inactive"});
	        cmbUserStatus.setBounds(150, y, 200, 25);
	        panel.add(cmbUserStatus);
	        y += 50;

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
	        buttonPanel.setBounds(400, y, 770, 40);
	        buttonPanel.setBackground(Color.WHITE);
	        
	        JButton btnAddUser = createButton("Add User", new Color(34, 139, 34), 0, 0, 120, 30);
	        btnAddUser.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                addUser();
	            }
	        });
	        
	        JButton btnUpdateUser = createButton("Update User", new Color(70, 130, 180), 0, 0, 120, 30);
	        btnUpdateUser.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                updateUser();
	            }
	        });
	        
	        JButton btnDeleteUser = createButton("Delete User", new Color(255, 200, 200), 0, 0, 120, 30);
	        btnDeleteUser.setForeground(Color.DARK_GRAY);
	        btnDeleteUser.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                deleteUser();
	            }
	        });
	        
	        JButton btnLoadUsers = createButton("Load Users", new Color(128, 0, 128), 0, 0, 120, 30);
	        btnLoadUsers.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadUsers();
	            }
	        });
	        
	        JButton btnClearUser = createButton("Clear Form", new Color(169, 169, 169), 0, 0, 120, 30);
	        btnClearUser.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                clearUserForm();
	            }
	        });
	        
	        buttonPanel.add(btnAddUser);
	        buttonPanel.add(btnUpdateUser);
	        buttonPanel.add(btnDeleteUser);
	        buttonPanel.add(btnLoadUsers);
	        buttonPanel.add(btnClearUser);
	        panel.add(buttonPanel);
	        y += 50;

	        String[] columns = {"User ID", "Username", "Email", "Full Name", "Role", "Status", "Last Login"};
	        usersModel = new DefaultTableModel(columns, 0);
	        usersTable = new JTable(usersModel);
	        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        
	        usersTable.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e) {
	                selectUserFromTable();
	            }
	        });
	        
	        JScrollPane scrollPane = new JScrollPane(usersTable);
	        scrollPane.setBounds(20, y, 1150, 250);
	        panel.add(scrollPane);

	        tabbedPane.addTab("Users", panel);
	    }

	    private void setupProductsTab() {
	        JPanel panel = new JPanel(null);
	        panel.setBackground(Color.WHITE);

	        JLabel title = new JLabel("PRODUCT MANAGEMENT");
	        title.setBounds(20, 10, 300, 25);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        panel.add(title);

	        int y = 50;
	        addField(panel, "Product ID:", txtProductID, 20, y); y += 35;
	        addField(panel, "Name*:", txtProductName, 20, y); y += 35;
	        addField(panel, "Description:", txtProductDesc, 20, y); y += 35;
	        addField(panel, "Price*:", txtProductPrice, 20, y); y += 35;
	        addField(panel, "Stock*:", txtProductStock, 20, y); y += 35;
	        addField(panel, "Category ID*:", txtProductCategory, 20, y); y += 35;
	        
	        JLabel lblStatus = new JLabel("Status:");
	        lblStatus.setBounds(20, y, 120, 25);
	        panel.add(lblStatus);
	        cmbProductStatus = new JComboBox(new String[]{"active", "inactive", "out_of_stock"});
	        cmbProductStatus.setBounds(150, y, 200, 25);
	        panel.add(cmbProductStatus);
	        y += 50;

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
	        buttonPanel.setBounds(400, y, 770, 40);
	        buttonPanel.setBackground(Color.WHITE);
	        
	        JButton btnAdd = createButton("Add Product", new Color(34, 139, 34), 0, 0, 120, 30);
	        btnAdd.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                addProduct();
	            }
	        });
	        
	        JButton btnUpdate = createButton("Update Product", new Color(70, 130, 180), 0, 0, 120, 30);
	        btnUpdate.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                updateProduct();
	            }
	        });
	        
	        JButton btnDelete = createButton("Delete Product", new Color(255, 200, 200), 0, 0, 120, 30);
	        btnDelete.setForeground(Color.DARK_GRAY);
	        btnDelete.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                deleteProduct();
	            }
	        });
	        
	        JButton btnLoad = createButton("Load Products", new Color(128, 0, 128), 0, 0, 120, 30);
	        btnLoad.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadProducts();
	            }
	        });
	        
	        JButton btnClear = createButton("Clear Form", new Color(169, 169, 169), 0, 0, 120, 30);
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
	        panel.add(buttonPanel);
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
	        panel.add(scrollPane);

	        tabbedPane.addTab("Products", panel);
	    }

	    private void setupOrdersTab() {
	        JPanel panel = new JPanel(null);
	        panel.setBackground(Color.WHITE);

	        JLabel title = new JLabel("ORDER MANAGEMENT");
	        title.setBounds(20, 10, 300, 25);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        panel.add(title);

	        int y = 50;
	        addField(panel, "Order ID:", txtOrderID, 20, y); y += 35;
	        addField(panel, "Order Number:", txtOrderNumber, 20, y); y += 35;
	        
	        JLabel lblStatus = new JLabel("Status:");
	        lblStatus.setBounds(20, y, 120, 25);
	        panel.add(lblStatus);
	        cmbOrderStatus = new JComboBox(new String[]{"pending", "confirmed", "processing", "shipped", "delivered", "cancelled"});
	        cmbOrderStatus.setBounds(150, y, 200, 25);
	        panel.add(cmbOrderStatus);
	        y += 50;
	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
	        buttonPanel.setBounds(400, y, 770, 40);
	        buttonPanel.setBackground(Color.WHITE);
	        
	        JButton btnLoad = createButton("Load Orders", new Color(128, 0, 128), 0, 0, 120, 30);
	        btnLoad.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadOrders();
	            }
	        });
	        
	        JButton btnUpdate = createButton("Update Status", new Color(70, 130, 180), 0, 0, 120, 30);
	        btnUpdate.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                updateOrderStatus();
	            }
	        });
	        
	        JButton btnClear = createButton("Clear Form", new Color(169, 169, 169), 0, 0, 120, 30);
	        btnClear.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                clearOrderForm();
	            }
	        });
	        
	        buttonPanel.add(btnLoad);
	        buttonPanel.add(btnUpdate);
	        buttonPanel.add(btnClear);
	        panel.add(buttonPanel);
	        y += 50;

	        String[] columns = {"Order ID", "Order Number", "User ID", "Total Amount", "Status", "Order Date"};
	        ordersModel = new DefaultTableModel(columns, 0);
	        ordersTable = new JTable(ordersModel);
	        
	        JScrollPane scrollPane = new JScrollPane(ordersTable);
	        scrollPane.setBounds(20, y, 1150, 300);
	        panel.add(scrollPane);

	        tabbedPane.addTab("Orders", panel);
	    }

	    private void setupPaymentsTab() {
	        JPanel panel = new JPanel(new BorderLayout());
	        panel.setBackground(Color.WHITE);

	        JLabel title = new JLabel("PAYMENT MANAGEMENT", JLabel.CENTER);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
	        panel.add(title, BorderLayout.NORTH);

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 25, 10));
	        
	        JButton btnLoad = createButton("Load Payments", new Color(70, 130, 180), 0, 0, 150, 30);
	        btnLoad.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadPayments();
	            }
	        });
	        
	        buttonPanel.add(btnLoad);
	        panel.add(buttonPanel, BorderLayout.CENTER);

	        // Payments Table
	        String[] columns = {"Payment ID", "Order ID", "Amount", "Payment Method", "Status", "Payment Date"};
	        paymentsModel = new DefaultTableModel(columns, 0);
	        paymentsTable = new JTable(paymentsModel);
	        
	        JScrollPane scrollPane = new JScrollPane(paymentsTable);
	        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        panel.add(scrollPane, BorderLayout.SOUTH);

	        tabbedPane.addTab("Payments", panel);
	    }

	    private void setupShipmentsTab() {
	        JPanel panel = new JPanel(new BorderLayout());
	        panel.setBackground(Color.WHITE);

	        JLabel title = new JLabel("SHIPMENT MANAGEMENT", JLabel.CENTER);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
	        panel.add(title, BorderLayout.NORTH);

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 25, 10));
	        
	        JButton btnLoad = createButton("Load Shipments", new Color(70, 130, 180), 0, 0, 150, 30);
	        btnLoad.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadShipments();
	            }
	        });
	        
	        buttonPanel.add(btnLoad);
	        panel.add(buttonPanel, BorderLayout.CENTER);

	        // Shipments Table
	        String[] columns = {"Shipment ID", "Order ID", "Tracking Number", "Carrier", "Status", "Shipment Date"};
	        shipmentsModel = new DefaultTableModel(columns, 0);
	        shipmentsTable = new JTable(shipmentsModel);
	        
	        JScrollPane scrollPane = new JScrollPane(shipmentsTable);
	        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        panel.add(scrollPane, BorderLayout.SOUTH);

	        tabbedPane.addTab("Shipments", panel);
	    }

	    private void setupReviewsTab() {
	        JPanel panel = new JPanel(new BorderLayout());
	        panel.setBackground(Color.WHITE);

	        JLabel title = new JLabel("PRODUCT REVIEWS", JLabel.CENTER);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
	        panel.add(title, BorderLayout.NORTH);

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 25, 10));
	        
	        JButton btnLoad = createButton("Load Reviews", new Color(70, 130, 180), 0, 0, 150, 30);
	        btnLoad.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadReviews();
	            }
	        });
	        
	        buttonPanel.add(btnLoad);
	        panel.add(buttonPanel, BorderLayout.CENTER);

	        // Reviews Table
	        String[] columns = {"Review ID", "Product ID", "User ID", "Rating", "Review Text", "Created At"};
	        reviewsModel = new DefaultTableModel(columns, 0);
	        reviewsTable = new JTable(reviewsModel);
	        
	        JScrollPane scrollPane = new JScrollPane(reviewsTable);
	        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        panel.add(scrollPane, BorderLayout.SOUTH);

	        tabbedPane.addTab("Reviews", panel);
	    }

	    private void addField(JPanel panel, String label, JTextField field, int x, int y) {
	        JLabel lbl = new JLabel(label);
	        lbl.setBounds(x, y, 120, 25);
	        field.setBounds(x + 130, y, 200, 25);
	        panel.add(lbl);
	        panel.add(field);
	    }

	    private JButton createButton(String text, Color color, int x, int y, int width, int height) {
	        JButton button = new JButton(text);
	        button.setBounds(x, y, width, height);
	        button.setBackground(color);
	        button.setForeground(Color.WHITE);
	        button.setFocusPainted(false);
	        button.setFont(new Font("Arial", Font.BOLD, 12));
	        return button;
	    }
	    private void loadAllData() {
	        loadUsers();   
	        loadProducts(); 
	        loadOrders();
	    }

	    private void loadUsers() {
	        try {
	            usersModel.setRowCount(0);
	            String sql = "SELECT user_id, username, email, full_name, role, is_active, last_login FROM users";
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(sql);
	            
	            while (rs.next()) {
	                String status = rs.getBoolean("is_active") ? "active" : "inactive";
	                usersModel.addRow(new Object[]{
	                    rs.getInt("user_id"),
	                    rs.getString("username"),
	                    rs.getString("email"),
	                    rs.getString("full_name"),
	                    rs.getString("role"),
	                    status,
	                    rs.getTimestamp("last_login")
	                });
	            }
	        } catch (SQLException ex) {
	            showError("Error loading users: " + ex.getMessage());
	        }
	    }

	    private void loadProducts() {
	        try {
	            productsModel.setRowCount(0);
	            String sql = "SELECT p.*, u.username as created_by_name FROM products p " +
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

	    private void loadReviews() {
	        try {
	            reviewsModel.setRowCount(0);
	            String sql = "SELECT review_id, product_id, user_id, rating, review_text, created_at FROM product_reviews";
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(sql);
	            
	            while (rs.next()) {
	                reviewsModel.addRow(new Object[]{
	                    rs.getInt("review_id"),
	                    rs.getInt("product_id"),
	                    rs.getInt("user_id"),
	                    rs.getInt("rating"),
	                    rs.getString("review_text"),
	                    rs.getTimestamp("created_at")
	                });
	            }
	        } catch (SQLException ex) {
	            showError("Error loading reviews: " + ex.getMessage());
	        }
	    }

	    // USER CRUD Operations (keep the same as before)
	    private void addUser() {
	        if (!validateUserForm()) return;
	        
	        try {
	            String sql = "INSERT INTO users (username, password_hash, email, full_name, role, address, phone, is_active) " +
	                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, txtUsername.getText().trim());
	            ps.setString(2, "default123"); // Default password, should be hashed
	            ps.setString(3, txtEmail.getText().trim());
	            ps.setString(4, txtFullName.getText().trim());
	            ps.setString(5, txtUserRole.getText().trim());
	            ps.setString(6, txtUserAddress.getText().trim());
	            ps.setString(7, txtUserPhone.getText().trim());
	            ps.setBoolean(8, cmbUserStatus.getSelectedItem().equals("active"));

	            ps.executeUpdate();
	            JOptionPane.showMessageDialog(this, "User added successfully!");
	            clearUserForm();
	            loadUsers();
	        } catch (SQLException ex) {
	            showError("Error adding user: " + ex.getMessage());
	        }
	    }

	    private void updateUser() {
	        if (txtUserID.getText().isEmpty()) {
	            showError("Please select a user to update.");
	            return;
	        }
	        if (!validateUserForm()) return;
	        
	        try {
	            String sql = "UPDATE users SET username=?, email=?, full_name=?, role=?, address=?, phone=?, is_active=? WHERE user_id=?";
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setString(1, txtUsername.getText().trim());
	            ps.setString(2, txtEmail.getText().trim());
	            ps.setString(3, txtFullName.getText().trim());
	            ps.setString(4, txtUserRole.getText().trim());
	            ps.setString(5, txtUserAddress.getText().trim());
	            ps.setString(6, txtUserPhone.getText().trim());
	            ps.setBoolean(7, cmbUserStatus.getSelectedItem().equals("active"));
	            ps.setInt(8, Integer.parseInt(txtUserID.getText().trim()));

	            int rows = ps.executeUpdate();
	            if (rows > 0) {
	                JOptionPane.showMessageDialog(this, "User updated successfully!");
	                clearUserForm();
	                loadUsers();
	            } else {
	                showError("User not found!");
	            }
	        } catch (SQLException ex) {
	            showError("Error updating user: " + ex.getMessage());
	        } catch (NumberFormatException ex) {
	            showError("Please enter a valid user ID.");
	        }
	    }

	    private void deleteUser() {
	        if (txtUserID.getText().isEmpty()) {
	            showError("Please select a user to delete.");
	            return;
	        }
	        
	        int confirm = JOptionPane.showConfirmDialog(this, 
	            "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
	        
	        if (confirm == JOptionPane.YES_OPTION) {
	            try {
	                String sql = "DELETE FROM users WHERE user_id=?";
	                PreparedStatement ps = conn.prepareStatement(sql);
	                ps.setInt(1, Integer.parseInt(txtUserID.getText().trim()));
	                
	                int rows = ps.executeUpdate();
	                if (rows > 0) {
	                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
	                    clearUserForm();
	                    loadUsers();
	                } else {
	                    showError("User not found!");
	                }
	            } catch (SQLException ex) {
	                showError("Error deleting user: " + ex.getMessage());
	            } catch (NumberFormatException ex) {
	                showError("Please enter a valid user ID.");
	            }
	        }
	    }

	    // PRODUCT CRUD Operations (keep the same as before)
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
	        } catch (SQLException ex) {
	            showError("Error adding product: " + ex.getMessage());
	        } catch (NumberFormatException ex) {
	            showError("Please enter valid numbers for price, stock, and category ID.");
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

	            int rows = ps.executeUpdate();
	            if (rows > 0) {
	                JOptionPane.showMessageDialog(this, "Product updated successfully!");
	                clearProductForm();
	                loadProducts();
	            } else {
	                showError("Product not found!");
	            }
	        } catch (SQLException ex) {
	            showError("Error updating product: " + ex.getMessage());
	        } catch (NumberFormatException ex) {
	            showError("Please enter valid numbers.");
	        }
	    }

	    private void deleteProduct() {
	        if (txtProductID.getText().isEmpty()) {
	            showError("Please select a product to delete.");
	            return;
	        }
	        
	        int confirm = JOptionPane.showConfirmDialog(this, 
	            "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
	        
	        if (confirm == JOptionPane.YES_OPTION) {
	            try {
	                String sql = "DELETE FROM products WHERE product_id=?";
	                PreparedStatement ps = conn.prepareStatement(sql);
	                ps.setInt(1, Integer.parseInt(txtProductID.getText().trim()));
	                
	                int rows = ps.executeUpdate();
	                if (rows > 0) {
	                    JOptionPane.showMessageDialog(this, "Product deleted successfully!");
	                    clearProductForm();
	                    loadProducts();
	                } else {
	                    showError("Product not found!");
	                }
	            } catch (SQLException ex) {
	                showError("Error deleting product: " + ex.getMessage());
	            } catch (NumberFormatException ex) {
	                showError("Please enter a valid product ID.");
	            }
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

	    // Selection Methods (keep the same as before)
	    private void selectUserFromTable() {
	        int row = usersTable.getSelectedRow();
	        if (row >= 0) {
	            txtUserID.setText(usersModel.getValueAt(row, 0).toString());
	            txtUsername.setText(usersModel.getValueAt(row, 1).toString());
	            txtEmail.setText(usersModel.getValueAt(row, 2).toString());
	            txtFullName.setText(usersModel.getValueAt(row, 3).toString());
	            txtUserRole.setText(usersModel.getValueAt(row, 4).toString());
	            cmbUserStatus.setSelectedItem(usersModel.getValueAt(row, 5).toString());
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

	    // Validation Methods (keep the same as before)
	    private boolean validateUserForm() {
	        if (txtUsername.getText().trim().isEmpty() ||
	            txtEmail.getText().trim().isEmpty() ||
	            txtFullName.getText().trim().isEmpty()) {
	            showError("Please fill in all required fields (*).");
	            return false;
	        }
	        return true;
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

	    private void clearUserForm() {
	        txtUserID.setText("");
	        txtUsername.setText("");
	        txtEmail.setText("");
	        txtFullName.setText("");
	        txtUserRole.setText("");
	        txtUserAddress.setText("");
	        txtUserPhone.setText("");
	        cmbUserStatus.setSelectedIndex(0);
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

	    private void clearOrderForm() {
	        txtOrderID.setText("");
	        txtOrderNumber.setText("");
	        cmbOrderStatus.setSelectedIndex(0);
	    }

	    private void showError(String message) {
	        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	    }

	    private void logout() {
	        int confirm = JOptionPane.showConfirmDialog(this, 
	            "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
	        
	        if (confirm == JOptionPane.YES_OPTION) {
	            DB.closeConnection();
	            System.exit(0);
	        }
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                new MainDashboard(1, "Admin User", "admin");
	            }
	        });
	    }
	}