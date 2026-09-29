package lk.melodybeatsapp.model;

import java.math.BigDecimal;

public class SalesSummary {
    private final String period;
    private final int numberOfSales;
    private final BigDecimal totalDiscount;
    private final BigDecimal totalSales;

    public SalesSummary(String period, int numberOfSales,
            BigDecimal totalDiscount, BigDecimal totalSales) {
        this.period = period;
        this.numberOfSales = numberOfSales;
        this.totalDiscount = totalDiscount;
        this.totalSales = totalSales;
    }

    public String period() { return period; }
    public int numberOfSales() { return numberOfSales; }
    public BigDecimal totalDiscount() { return totalDiscount; }
    public BigDecimal totalSales() { return totalSales; }
}
