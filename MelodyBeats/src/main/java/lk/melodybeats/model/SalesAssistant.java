package lk.melodybeatsapp.model;

public class SalesAssistant extends User {
    public SalesAssistant(int userId, String username, String fullName, boolean active) {
        super(userId, username, fullName, active);
    }

    @Override
    public String getRole() { return "SALES_ASSISTANT"; }
}
