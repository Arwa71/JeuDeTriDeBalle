package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/ball_sort";
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try {
            // This will ensure the MySQL driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
    }

    // Returns a list of [coups, pseudo, date] for the top N scores (lowest coups)
    public static List<String[]> getTopScores(int n) throws SQLException {
        List<String[]> scores = new ArrayList<>();
        String sql = "SELECT coups, pseudo, date FROM scores ORDER BY coups ASC, date ASC LIMIT ?";

        Connection conn = null;
        try {
            try {
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException e) {
                System.err.println("Database connection error: " + e.getMessage());
                System.err.println("Make sure XAMPP is running and MySQL is started");
                throw e;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, n);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        scores.add(new String[]{
                            String.valueOf(rs.getInt("coups")),
                            rs.getString("pseudo"),
                            rs.getString("date")
                        });
                    }
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
        return scores;
    }

    // Optionally: get the best score only
    public static String[] getBestScore() throws SQLException {
        List<String[]> top = getTopScores(1);
        return top.isEmpty() ? null : top.get(0);
    }

    // Optionally: save a new score
    public static void saveScore(String pseudo, int coups) throws SQLException {
        String sql = "INSERT INTO scores (pseudo, coups) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, pseudo);
                ps.setInt(2, coups);
                ps.executeUpdate();
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
