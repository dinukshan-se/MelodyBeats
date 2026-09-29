package lk.melodybeatsapp.model;

public abstract class User {
    private final int userId;
    private final String username;
    private final String fullName;
    private final boolean active;

    protected User(int userId, String username, String fullName, boolean active) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.active = active;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public boolean isActive() { return active; }
    public abstract String getRole();
    public boolean isManager() { return this instanceof StoreManager; }
}
