package lk.melodybeats.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lk.melodybeatsapp.model.Promotion;
import lk.melodybeats.util.DBConnection;

public class PromotionDAO {
    public BigDecimal getActiveDiscount(int itemId) throws SQLException {
        String sql = "SELECT COALESCE(MAX(DiscountPercentage),0) FROM PROMOTION WHERE ItemID=? "
                + "AND Active=1 AND CAST(GETDATE() AS DATE) BETWEEN StartDate AND EndDate";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getBigDecimal(1); }
        }
    }

    public List<Promotion> getAllPromotions() throws SQLException {
        List<Promotion> rows = new ArrayList<>();
        String sql = "SELECT p.PromotionID,p.ItemID,m.Title,p.PromotionName,p.DiscountPercentage,p.StartDate,p.EndDate,p.Active "
                + "FROM PROMOTION p JOIN MUSIC_ITEM m ON p.ItemID=m.ItemID ORDER BY p.PromotionID";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) rows.add(new Promotion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
                    rs.getBigDecimal(5), rs.getDate(6).toLocalDate(), rs.getDate(7).toLocalDate(), rs.getBoolean(8)));
        }
        return rows;
    }

    public void addPromotion(int itemId, String name, BigDecimal rate, LocalDate start, LocalDate end) throws SQLException {
        String sql = "INSERT INTO PROMOTION(ItemID,PromotionName,DiscountPercentage,StartDate,EndDate) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId); ps.setString(2, name); ps.setBigDecimal(3, rate);
            ps.setDate(4, Date.valueOf(start)); ps.setDate(5, Date.valueOf(end)); ps.executeUpdate();
        }
    }

    public void setActive(int promotionId, boolean active) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE PROMOTION SET Active=? WHERE PromotionID=?")) {
            ps.setBoolean(1, active); ps.setInt(2, promotionId); ps.executeUpdate();
        }
    }
}
