package lk.melodybeatsapp.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Promotion {
    private final int promotionId;
    private final int itemId;
    private final String itemTitle;
    private final String promotionName;
    private final BigDecimal discountPercentage;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final boolean active;

    public Promotion(int promotionId, int itemId, String itemTitle,
            String promotionName, BigDecimal discountPercentage,
            LocalDate startDate, LocalDate endDate, boolean active) {
        this.promotionId = promotionId;
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.promotionName = promotionName;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    public int promotionId() { return promotionId; }
    public int itemId() { return itemId; }
    public String itemTitle() { return itemTitle; }
    public String promotionName() { return promotionName; }
    public BigDecimal discountPercentage() { return discountPercentage; }
    public LocalDate startDate() { return startDate; }
    public LocalDate endDate() { return endDate; }
    public boolean active() { return active; }
}
