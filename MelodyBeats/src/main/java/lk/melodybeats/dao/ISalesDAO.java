package lk.melodybeatsapp.dao;

import java.sql.SQLException;
import lk.melodybeatsapp.model.Purchase;
import lk.melodybeatsapp.model.Receipt;

public interface ISalesDAO {
    Receipt processSale(Purchase purchase) throws SQLException;
}
