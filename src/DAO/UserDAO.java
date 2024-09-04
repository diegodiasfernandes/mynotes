package DAO;

import Classes.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    // Constructor
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(String username, String password) throws SQLException {
        String query = "INSERT INTO user (name, password) VALUES (?, ?)";

        PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.executeUpdate();
    }

    public void createUsers(List<User> users) throws SQLException {
        for (User user : users) {
            create(
               user.getName(), 
               user.getPassword() 
            );
        }
    }

    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("name"), rs.getString("password"));
            } else {
                return null;
            }
        }
    }

    public List<User> read() throws SQLException {
        List<User> users = new ArrayList<>();

        String query = "SELECT * FROM user";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                users.add(
                    new User ( rs.getInt("id"), 
                               rs.getString("name"), 
                               rs.getString("password"))
                );
            }
        }
        return users;
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE user SET name = ?, password = ?" + 
                       "WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM user WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public User checkLogin(String username, String password) {
        String query = "SELECT id, name, password FROM user WHERE name = ? AND password = ?";
    
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
    
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String pass = rs.getString("password");
                
                return new User(id, name, pass);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
