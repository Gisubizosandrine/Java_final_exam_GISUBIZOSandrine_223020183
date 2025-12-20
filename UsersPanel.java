package panel;




import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UsersPanel extends JPanel {

    private Connection conn;

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

    public UsersPanel(Connection conn) {
        this.conn = conn;
        setLayout(null);
        setBackground(Color.WHITE);
        initializeUI();
        loadUsers();
    }

    private void initializeUI() {

        JLabel title = new JLabel("USER MANAGEMENT");
        title.setBounds(20, 10, 300, 25);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(0, 100, 0));
        add(title);

        int y = 50;
        addField("User ID:", txtUserID, 20, y); y += 35;
        addField("Username*:", txtUsername, 20, y); y += 35;
        addField("Email*:", txtEmail, 20, y); y += 35;
        addField("Full Name*:", txtFullName, 20, y); y += 35;
        addField("Role:", txtUserRole, 20, y); y += 35;
        addField("Address:", txtUserAddress, 20, y); y += 35;
        addField("Phone:", txtUserPhone, 20, y); y += 35;

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(20, y, 120, 25);
        add(lblStatus);

        cmbUserStatus = new JComboBox(new String[]{"active", "inactive"});
        cmbUserStatus.setBounds(150, y, 200, 25);
        add(cmbUserStatus);
        y += 50;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBounds(400, y, 770, 40);
        buttonPanel.setBackground(Color.WHITE);

        JButton btnAddUser = createButton("Add User", new Color(34, 139, 34));
        btnAddUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        JButton btnUpdateUser = createButton("Update User", new Color(70, 130, 180));
        btnUpdateUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        JButton btnDeleteUser = createButton("Delete User", new Color(255, 200, 200));
        btnDeleteUser.setForeground(Color.DARK_GRAY);
        btnDeleteUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        JButton btnLoadUsers = createButton("Load Users", new Color(128, 0, 128));
        btnLoadUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });

        JButton btnClearUser = createButton("Clear Form", new Color(169, 169, 169));
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
        add(buttonPanel);

        y += 50;

        String[] columns = {
            "User ID", "Username", "Email",
            "Full Name", "Role",
            "Status", "Last Login"
        };

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

    private void addUser() {
        if (!validateUserForm()) return;

        try {
            String sql = "INSERT INTO users (username, password_hash, email, full_name, role, address, phone, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtUsername.getText().trim());
            ps.setString(2, "default123");
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

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "User updated successfully!");
            clearUserForm();
            loadUsers();
        } catch (Exception ex) {
            showError("Error updating user: " + ex.getMessage());
        }
    }

    private void deleteUser() {
        if (txtUserID.getText().isEmpty()) {
            showError("Please select a user to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this user?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM users WHERE user_id=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(txtUserID.getText().trim()));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                clearUserForm();
                loadUsers();
            } catch (Exception ex) {
                showError("Error deleting user: " + ex.getMessage());
            }
        }
    }

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

    private boolean validateUserForm() {
        if (txtUsername.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            txtFullName.getText().trim().isEmpty()) {
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
