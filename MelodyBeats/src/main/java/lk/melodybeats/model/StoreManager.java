package lk.melodybeatsapp.model;

public class StoreManager extends SalesAssistant {
    public StoreManager(int userId, String username, String fullName, boolean active) {
        super(userId, username, fullName, active);
    }

    @Override
    public String getRole() { return "STORE_MANAGER"; }
}
