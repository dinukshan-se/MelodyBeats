package lk.melodybeatsapp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Receipt {
    private final int receiptId;
    private final int saleId;
    private final String receiptNumber;
    private final LocalDateTime issuedAt;
    private final BigDecimal totalAmount;

    public Receipt(int receiptId, int saleId, String receiptNumber,
                   LocalDateTime issuedAt, BigDecimal totalAmount) {
        this.receiptId = receiptId;
        this.saleId = saleId;
        this.receiptNumber = receiptNumber;
        this.issuedAt = issuedAt;
        this.totalAmount = totalAmount;
    }

    public int getReceiptId() { return receiptId; }
    public int getSaleId() { return saleId; }
    public String getReceiptNumber() { return receiptNumber; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}
