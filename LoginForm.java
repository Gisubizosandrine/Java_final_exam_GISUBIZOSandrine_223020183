package ecommerceform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class LoginForm extends JFrame {


    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit, btnRegister;
    private Connection conn;

    public LoginForm() {
        initializeUI();
        connectToDatabase();
        setupEventListeners();
    }

    private void initializeUI() {
        setTitle("E-commerce Platform Management System Login Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        JLabel lblTitle = new JLabel("E-COMMERCE PLATFORM LOGIN ", JLabel.CENTER);
        lblTitle.setBounds(50, 20, 300, 30);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.BLUE);
        add(lblTitle);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 70, 100, 25);
        txtUsername = new JTextField();
        txtUsername.setBounds(150, 70, 200, 25);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 110, 100, 25);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 110, 200, 25);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(50, 160, 100, 30);
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(160, 160, 100, 30);
        btnRegister.setBackground(new Color(34, 139, 34));
        btnRegister.setForeground(Color.WHITE);

        btnExit = new JButton("Exit");
        btnExit.setBounds(270, 160, 80, 30);
        btnExit.setBackground(new Color(178, 34, 34));
        btnExit.setForeground(Color.WHITE);

            add(lblUsername);
        add(txtUsername);
        add(lblPassword);
        add(txtPassword);
        add(btnLogin);
        add(btnRegister);
        add(btnExit);

          txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    attemptLogin();
                }
            }
        });
    }

    private void setupEventListeners() {
       
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRegistrationForm();
            }
        });

        
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeConnection();
                System.exit(0);
            }
        });
    }

    private void attemptLogin() {
        String username = txtUsername.getText().trim();
        char[] password = txtPassword.getPassword();

        if (username.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String passwordHash = hashPassword(new String(password));
        java.util.Arrays.fill(password, '\0');

     
        String sql = "SELECT user_id, username, password_hash, full_name, role FROM users WHERE username = ? AND is_active = TRUE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                  
                    String storedPassword = rs.getString("password_hash");
                    
                   
                    if (storedPassword.equals(passwordHash)) {
                        int userId = rs.getInt("user_id");
                        String fullName = rs.getString("full_name");
                        String role = rs.getString("role");
                     
                        updateLastLogin(userId);
                        
                        JOptionPane.showMessageDialog(this, 
                            "Welcome " + fullName + "!\nRole: " + role, 
                            "Login Successful", 
                            JOptionPane.INFORMATION_MESSAGE);
                       
                        openDashboard(userId, fullName, role);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Invalid username or password.", 
                            "Login Failed", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid username or password.", 
                        "Login Failed", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Could not update last login: " + ex.getMessage());
        }
    }

    private void openDashboard(final int userId, final String fullName, final String role) {
   
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainDashboard(userId, fullName, role).setVisible(true);
            }
        });
        this.dispose(); 
    }

    private void openRegistrationForm() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegistrationForm().setVisible(true);
            }
        });
        
        setState(JFrame.ICONIFIED);
    
    }

    private void connectToDatabase() {
        try {
            conn = DB.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connected successfully for login!");
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

    private void closeConnection() {
        DB.closeConnection();
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

    public static void main(String[] args) {
    
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}
