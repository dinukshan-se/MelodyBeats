package lk.melodybeats.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lk.melodybeatsapp.model.SalesAssistant;
import lk.melodybeatsapp.model.StoreManager;
import lk.melodybeatsapp.model.User;
import lk.melodybeats.util.DBConnection;
import lk.melodybeatsapp.dao.IUserDAO;

public class UserDAO implements IUserDAO {
    @Override
    public Optional<User> authenticate(String username, String password) throws SQLException {
        String sql = "SELECT UserID,Username,FullName,UserRole,Active FROM USER_ACCOUNT "
                + "WHERE Username=? AND AccPassword=? AND Active=1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapUser(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT UserID,Username,FullName,UserRole,Active FROM USER_ACCOUNT ORDER BY UserID";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) users.add(mapUser(rs));
        }
        return users;
    }

    @Override
    public void createUser(String username, String password, String fullName, String role) throws SQLException {
        String sql = "INSERT INTO USER_ACCOUNT(Username,AccPassword,FullName,UserRole) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, password);
            ps.setString(3, fullName.trim());
            ps.setString(4, role);
            ps.executeUpdate();
        }
    }

    @Override
    public void setAccountActive(int userId, boolean active) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE USER_ACCOUNT SET Active=? WHERE UserID=?")) {
            ps.setBoolean(1, active);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        if ("STORE_MANAGER".equals(rs.getString("UserRole"))) {
            return new StoreManager(rs.getInt("UserID"), rs.getString("Username"),
                    rs.getString("FullName"), rs.getBoolean("Active"));
        }
        return new SalesAssistant(rs.getInt("UserID"), rs.getString("Username"),
                rs.getString("FullName"), rs.getBoolean("Active"));
    }
}
