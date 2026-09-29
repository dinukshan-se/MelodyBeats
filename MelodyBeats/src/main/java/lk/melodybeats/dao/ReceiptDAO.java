package lk.melodybeats.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import lk.melodybeatsapp.model.Receipt;
import lk.melodybeats.util.DBConnection;
import lk.melodybeatsapp.dao.IReceiptDAO;

public class ReceiptDAO implements IReceiptDAO {
    @Override
    public Optional<Receipt> findByReceiptNumber(String receiptNumber) throws SQLException {
        String sql = "SELECT r.ReceiptID,r.SaleID,r.ReceiptNumber,r.IssuedAt,s.TotalAmount "
                + "FROM RECEIPT r JOIN SALE s ON r.SaleID=s.SaleID WHERE r.ReceiptNumber=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, receiptNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(new Receipt(rs.getInt(1), rs.getInt(2), rs.getString(3),
                        rs.getTimestamp(4).toLocalDateTime(), rs.getBigDecimal(5)));
            }
        }
    }
}
