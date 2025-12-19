package ecommerceform;


	import javax.swing.*;
	import java.awt.*;
	import java.awt.event.*;
	import java.sql.*;
	import java.security.MessageDigest;
	import java.security.NoSuchAlgorithmException;
	import java.nio.charset.StandardCharsets;

	public class RegistrationForm extends JFrame {
	
		    private JTextField txtUsername, txtEmail, txtFullName, txtAddress, txtPhone;
		    private JPasswordField txtPassword, txtConfirmPassword;
		    private JComboBox<String> cmbRole;
		    private JButton btnRegister, btnClear, btnBackToLogin;
		    private Connection conn;

		    public RegistrationForm() {
		        initializeUI();
		        connectToDatabase();
		        setupEventListeners();
		    }
		    private void initializeUI() {
		        setTitle("E-commerce Platform *User Registration*");
		        setSize(500, 550);
		        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        setLocationRelativeTo(null);
		        setLayout(null);
		        setResizable(false);
		        JLabel lblTitle = new JLabel("USER REGISTRATION", JLabel.CENTER);
		        lblTitle.setBounds(50, 20, 400, 30);
		        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
		        lblTitle.setForeground(Color.BLUE);
		        add(lblTitle);

		        // Form fields
		        int y = 70;
		        addField("Username*:", txtUsername = new JTextField(), 50, y); y += 45;
		        addField("Email*:", txtEmail = new JTextField(), 50, y); y += 45;
		        addField("Password*:", txtPassword = new JPasswordField(), 50, y); y += 45;
		        addField("Confirm Password*:", txtConfirmPassword = new JPasswordField(), 50, y); y += 45;
		        addField("Full Name*:", txtFullName = new JTextField(), 50, y); y += 45;
		        
		        // Role selection
		        JLabel lblRole = new JLabel("Role:");
		        lblRole.setBounds(50, y, 120, 25);
		        add(lblRole);
		        cmbRole = new JComboBox<>(new String[]{"customer", "vendor", "admin"});
		        cmbRole.setBounds(180, y, 250, 25);
		        add(cmbRole);
		        y += 45;

		        addField("Address:", txtAddress = new JTextField(), 50, y); y += 45;
		        addField("Phone:", txtPhone = new JTextField(), 50, y); y += 60;

		        // Buttons
		        btnRegister = createButton("Register", new Color(34, 139, 34), 50, y, 120, 35);
		        btnClear = createButton("Clear Form", new Color(169, 169, 169), 190, y, 120, 35);
		        btnBackToLogin = createButton("Back to Login", new Color(70, 130, 180), 330, y, 120, 35);

		        add(btnRegister);
		        add(btnClear);
		        add(btnBackToLogin);

		        txtConfirmPassword.addKeyListener(new KeyAdapter() {
		            public void keyPressed(KeyEvent e) {
		                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		                    attemptRegistration();
		                }
		            }
		        });
		    }

		    private void addField(String label, JTextField field, int x, int y) {
		        JLabel lbl = new JLabel(label);
		        lbl.setBounds(x, y, 120, 25);
		        field.setBounds(180, y, 250, 25);
		        add(lbl);
		        add(field);
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

		    private void setupEventListeners() {
		        btnRegister.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		                attemptRegistration();
		            }
		        });

		        btnClear.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		                clearForm();
		            }
		        });

		        btnBackToLogin.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		                goBackToLogin();
		            }
		        });
		    }

		    private void attemptRegistration() {
		        if (!validateForm()) {
		            return;
		        }

		        String username = txtUsername.getText().trim();
		        String email = txtEmail.getText().trim();
		        String password = new String(txtPassword.getPassword());
		        String fullName = txtFullName.getText().trim();
		        String role = cmbRole.getSelectedItem().toString();
		        String address = txtAddress.getText().trim();
		        String phone = txtPhone.getText().trim();

		        if (isUsernameExists(username)) {
		            JOptionPane.showMessageDialog(this, 
		                "Username already exists. Please choose a different username.", 
		                "Registration Failed", 
		                JOptionPane.ERROR_MESSAGE);
		            return;
		        }
		        if (isEmailExists(email)) {
		            JOptionPane.showMessageDialog(this, 
		                "Email already exists. Please use a different email address.", 
		                "Registration Failed", 
		                JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        String passwordHash = hashPassword(password);

		        try {
		            String sql = "INSERT INTO users (username, password_hash, email, full_name, role, address, phone, is_active) " +
		                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		            PreparedStatement ps = conn.prepareStatement(sql);
		            ps.setString(1, username);
		            ps.setString(2, passwordHash);
		            ps.setString(3, email);
		            ps.setString(4, fullName);
		            ps.setString(5, role);
		            ps.setString(6, address);
		            ps.setString(7, phone);
		            ps.setBoolean(8, true);

		            int rowsAffected = ps.executeUpdate();
		            if (rowsAffected > 0) {
		                JOptionPane.showMessageDialog(this, 
		                    "Registration successful! You can now login with your credentials.", 
		                    "Registration Successful", 
		                    JOptionPane.INFORMATION_MESSAGE);
		                clearForm();
		                goBackToLogin();
		            } else {
		                JOptionPane.showMessageDialog(this, 
		                    "Registration failed. Please try again.", 
		                    "Registration Failed", 
		                    JOptionPane.ERROR_MESSAGE);
		            }
		        } catch (SQLException ex) {
		            JOptionPane.showMessageDialog(this, 
		                "Database error: " + ex.getMessage(), 
		                "Registration Error", 
		                JOptionPane.ERROR_MESSAGE);
		            ex.printStackTrace();
		        }
		    }

		    private boolean validateForm() {
		        String username = txtUsername.getText().trim();
		        String email = txtEmail.getText().trim();
		        String password = new String(txtPassword.getPassword());
		        String confirmPassword = new String(txtConfirmPassword.getPassword());
		        String fullName = txtFullName.getText().trim();
		        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
		            JOptionPane.showMessageDialog(this, 
		                "Please fill in all required fields (*).", 
		                "Validation Error", 
		                JOptionPane.WARNING_MESSAGE);
		            return false;
		        }

		        if (username.length() < 3 || username.length() > 50) {
		            JOptionPane.showMessageDialog(this, 
		                "Username must be between 3 and 50 characters.", 
		                "Validation Error", 
		                JOptionPane.WARNING_MESSAGE);
		            return false;
		        }
		        if (!isValidEmail(email)) {
		            JOptionPane.showMessageDialog(this, 
		                "Please enter a valid email address.", 
		                "Validation Error", 
		                JOptionPane.WARNING_MESSAGE);
		            return false;
		        }

		        if (password.length() < 6) {
		            JOptionPane.showMessageDialog(this, 
		                "Password must be at least 6 characters long.", 
		                "Validation Error", 
		                JOptionPane.WARNING_MESSAGE);
		            return false;
		        }

		        if (!password.equals(confirmPassword)) {
		            JOptionPane.showMessageDialog(this, 
		                "Passwords do not match.", 
		                "Validation Error", 
		                JOptionPane.WARNING_MESSAGE);
		            return false;
		        }

		        return true;
		    }

		    private boolean isUsernameExists(String username) {
		        try {
		            String sql = "SELECT user_id FROM users WHERE username = ?";
		            PreparedStatement ps = conn.prepareStatement(sql);
		            ps.setString(1, username);
		            ResultSet rs = ps.executeQuery();
		            return rs.next();
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		            return false;
		        }
		    }

		    private boolean isEmailExists(String email) {
		        try {
		            String sql = "SELECT user_id FROM users WHERE email = ?";
		            PreparedStatement ps = conn.prepareStatement(sql);
		            ps.setString(1, email);
		            ResultSet rs = ps.executeQuery();
		            return rs.next();
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		            return false;
		        }
		    }

		    private boolean isValidEmail(String email) {
		        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		        return email.matches(emailRegex);
		    }

		    private String hashPassword(String password) {
		        try {
		            MessageDigest md = MessageDigest.getInstance("SHA-256");
		            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
		            StringBuilder sb = new StringBuilder();
		            for (byte b : hash) {
		                sb.append(String.format("%02x", b));
		            }
		            return sb.toString();
		        } catch (NoSuchAlgorithmException e) {
		            throw new RuntimeException("SHA-256 algorithm not available", e);
		        }
		    }

		    private void clearForm() {
		        txtUsername.setText("");
		        txtEmail.setText("");
		        txtPassword.setText("");
		        txtConfirmPassword.setText("");
		        txtFullName.setText("");
		        txtAddress.setText("");
		        txtPhone.setText("");
		        cmbRole.setSelectedIndex(0);
		        txtUsername.requestFocus();
		    }

		    private void goBackToLogin() {
		        this.dispose();
		        SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		                
		                new LoginForm().setVisible(true);
		            }
		        });
		    }

		    private void connectToDatabase() {
		        try {
		            conn = DB.getConnection();
		            if (conn != null && !conn.isClosed()) {
		                System.out.println("Database connected successfully for registration!");
		            } else {
		                JOptionPane.showMessageDialog(this, 
		                    "Failed to connect to database!", 
		                    "Connection Error", 
		                    JOptionPane.ERROR_MESSAGE);
		            }
		        } catch (SQLException ex) {
		            JOptionPane.showMessageDialog(this, 
		                "Database connection failed!\n" + ex.getMessage(), 
		                "Connection Error", 
		                JOptionPane.ERROR_MESSAGE);
		            ex.printStackTrace();
		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(this, 
		                "Unexpected error: " + ex.getMessage(), 
		                "Error", 
		                JOptionPane.ERROR_MESSAGE);
		            ex.printStackTrace();
		        }
		    }

		    public static void main(String[] args) {
		        SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		                new RegistrationForm().setVisible(true);
		            }
		        });
		    }


		}