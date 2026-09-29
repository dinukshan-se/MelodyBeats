package lk.melodybeats.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import lk.melodybeatsapp.model.Purchase;
import lk.melodybeatsapp.model.PurchaseItem;
import lk.melodybeatsapp.model.Receipt;
import lk.melodybeats.util.DBConnection;
import lk.melodybeatsapp.dao.ISalesDAO;

public class SalesDAO implements ISalesDAO {
    @Override
    public Receipt processSale(Purchase purchase) throws SQLException {
        if (purchase.getItems().isEmpty()) throw new IllegalArgumentException("The cart is empty.");
        String saleSql = "INSERT INTO SALE(UserID,Subtotal,DiscountAmount,TotalAmount) VALUES(?,?,?,?)";
        String lineSql = "INSERT INTO SALE_ITEM(SaleID,ItemID,Quantity,UnitPrice,DiscountPercentage,LineTotal) VALUES(?,?,?,?,?,?)";
        String receiptSql = "INSERT INTO RECEIPT(SaleID,ReceiptNumber) VALUES(?,?)";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                int saleId;
                try (PreparedStatement ps = con.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, purchase.getSalesAssistant().getUserId());
                    ps.setBigDecimal(2, purchase.getSubtotal());
                    ps.setBigDecimal(3, purchase.getDiscountAmount());
                    ps.setBigDecimal(4, purchase.getTotalAmount());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Sale ID was not generated.");
                        saleId = keys.getInt(1);
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(lineSql)) {
                    for (PurchaseItem line : purchase.getItems()) {
                        ps.setInt(1, saleId);
                        ps.setInt(2, line.getMusicItem().getItemId());
                        ps.setInt(3, line.getQuantity());
                        ps.setBigDecimal(4, line.getMusicItem().getPrice());
                        ps.setBigDecimal(5, line.getDiscountPercentage());
                        ps.setBigDecimal(6, line.getLineTotal());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                String number = "MB-" + java.time.Year.now().getValue() + "-" + String.format("%06d", saleId);
                int receiptId;
                try (PreparedStatement ps = con.prepareStatement(receiptSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, saleId);
                    ps.setString(2, number);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Receipt ID was not generated.");
                        receiptId = keys.getInt(1);
                    }
                }
                con.commit();
                return new Receipt(receiptId, saleId, number, LocalDateTime.now(), purchase.getTotalAmount());
            } catch (SQLException | RuntimeException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
