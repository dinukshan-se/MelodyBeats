package lk.melodybeats.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lk.melodybeatsapp.model.SalesSummary;
import lk.melodybeats.util.DBConnection;

public class ReportDAO {
    public List<SalesSummary> getDailySummary() throws SQLException {
        return query("SELECT CONVERT(varchar(10),SaleDateTime,23) Period,COUNT(*) NumberOfSales,"+
                "SUM(DiscountAmount) TotalDiscount,SUM(TotalAmount) TotalSales FROM SALE "+
                "GROUP BY CONVERT(varchar(10),SaleDateTime,23) ORDER BY Period DESC");
    }
    public List<SalesSummary> getMonthlySummary() throws SQLException {
        return query("SELECT CONCAT(YEAR(SaleDateTime),'-',RIGHT('0'+CAST(MONTH(SaleDateTime) AS varchar(2)),2)) Period,"+
                "COUNT(*) NumberOfSales,SUM(DiscountAmount) TotalDiscount,SUM(TotalAmount) TotalSales FROM SALE "+
                "GROUP BY YEAR(SaleDateTime),MONTH(SaleDateTime) ORDER BY YEAR(SaleDateTime) DESC,MONTH(SaleDateTime) DESC");
    }
    private List<SalesSummary> query(String sql) throws SQLException {
        List<SalesSummary> rows = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) rows.add(new SalesSummary(rs.getString(1),rs.getInt(2),rs.getBigDecimal(3),rs.getBigDecimal(4)));
        }
        return rows;
    }
}
