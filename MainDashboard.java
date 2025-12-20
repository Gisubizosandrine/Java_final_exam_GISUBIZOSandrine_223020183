package ecommerceform;

import javax.swing.*;

import panel.OrdersPanel;
import panel.PaymentsPanel;
import panel.ProductsPanel;
import panel.ReviewsPanel;
import panel.ShipmentsPanel;
import panel.UsersPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class MainDashboard extends JFrame {

    private int userId = 1; 
    private String fullName = "Admin User";
    private String role = "admin";
    private Connection conn;

    private JTabbedPane tabbedPane;

    private JLabel lblTitle;
    private JLabel lblUserInfo;
    private JButton btnLogout;

    public MainDashboard(int userId, String fullName, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
        this.conn = DB.getConnection();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("E-commerce Platform - Main Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setupHeader();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Users", new UsersPanel(conn));
        tabbedPane.addTab("Products", new ProductsPanel(conn));
        tabbedPane.addTab("Orders", new OrdersPanel(conn));
        tabbedPane.addTab("Payments", new PaymentsPanel(conn));
        tabbedPane.addTab("Shipments", new ShipmentsPanel(conn));
        tabbedPane.addTab("Reviews", new ReviewsPanel(conn));

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void setupHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTitle = new JLabel("E-COMMERCE PLATFORM DASHBOARD", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.BLUE);

        lblUserInfo = new JLabel("Welcome, " + fullName + " | Role: " + role, JLabel.CENTER);
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


	}
