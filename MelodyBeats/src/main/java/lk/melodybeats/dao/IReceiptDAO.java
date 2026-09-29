package lk.melodybeatsapp.dao;

import java.sql.SQLException;
import java.util.Optional;
import lk.melodybeatsapp.model.Receipt;

public interface IReceiptDAO {
    Optional<Receipt> findByReceiptNumber(String receiptNumber) throws SQLException;
}
