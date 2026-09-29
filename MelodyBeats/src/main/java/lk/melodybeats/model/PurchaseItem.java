package lk.melodybeatsapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PurchaseItem {
    private final MusicItem musicItem;
    private final int quantity;
    private final BigDecimal discountPercentage;

    public PurchaseItem(MusicItem musicItem, int quantity, BigDecimal discountPercentage) {
        this.musicItem = musicItem;
        this.quantity = quantity;
        this.discountPercentage = discountPercentage;
    }

    public MusicItem getMusicItem() { return musicItem; }
    public int getQuantity() { return quantity; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public BigDecimal getSubtotal() {
        return musicItem.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
    public BigDecimal getDiscountAmount() {
        return getSubtotal().multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
    public BigDecimal getLineTotal() { return getSubtotal().subtract(getDiscountAmount()); }
}
