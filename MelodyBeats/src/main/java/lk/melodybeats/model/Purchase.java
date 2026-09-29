package lk.melodybeatsapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Purchase {
    private final User salesAssistant;
    private final List<PurchaseItem> items = new ArrayList<>();

    public Purchase(User salesAssistant) { this.salesAssistant = salesAssistant; }
    public User getSalesAssistant() { return salesAssistant; }
    public void addItem(PurchaseItem item) { items.add(item); }
    public void removeItem(int index) { items.remove(index); }
    public void clear() { items.clear(); }
    public List<PurchaseItem> getItems() { return Collections.unmodifiableList(items); }
    public BigDecimal getSubtotal() {
        return items.stream().map(PurchaseItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getDiscountAmount() {
        return items.stream().map(PurchaseItem::getDiscountAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getTotalAmount() { return getSubtotal().subtract(getDiscountAmount()); }
}
