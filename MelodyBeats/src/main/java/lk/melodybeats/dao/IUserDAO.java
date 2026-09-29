package lk.melodybeatsapp.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lk.melodybeatsapp.model.User;

public interface IUserDAO {
    Optional<User> authenticate(String username, String password) throws SQLException;
    List<User> getAllUsers() throws SQLException;
    void createUser(String username, String password, String fullName, String role) throws SQLException;
    void setAccountActive(int userId, boolean active) throws SQLException;
}
