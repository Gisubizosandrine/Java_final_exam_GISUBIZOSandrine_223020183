package panel;



	import javax.swing.*;
	import javax.swing.table.DefaultTableModel;
	import java.awt.*;
	import java.awt.event.*;
	import java.sql.*;

	public class ReviewsPanel extends JPanel {

	    private Connection conn;

	    private JTable reviewsTable;
	    private DefaultTableModel reviewsModel;

	    public ReviewsPanel(Connection conn) {
	        this.conn = conn;
	        setLayout(new BorderLayout());
	        setBackground(Color.WHITE);
	        initializeUI();
	        loadReviews();
	    }

	    private void initializeUI() {

	        JLabel title = new JLabel("PRODUCT REVIEWS", JLabel.CENTER);
	        title.setFont(new Font("Arial", Font.BOLD, 16));
	        title.setForeground(new Color(0, 100, 0));
	        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
	        add(title, BorderLayout.NORTH);

	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 25, 10));

	        JButton btnLoad = createButton("Load Reviews", new Color(70, 130, 180));
	        btnLoad.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                loadReviews();
	            }
	        });

	        buttonPanel.add(btnLoad);
	        add(buttonPanel, BorderLayout.CENTER);

	        String[] columns = {
	                "Review ID", "Product ID",
	                "User ID", "Rating",
	                "Review Text", "Created At"
	        };

	        reviewsModel = new DefaultTableModel(columns, 0);
	        reviewsTable = new JTable(reviewsModel);

	        JScrollPane scrollPane = new JScrollPane(reviewsTable);
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

	    private void showError(String message) {
	        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

