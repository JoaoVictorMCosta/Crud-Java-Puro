package dao;

import db.Database;
import db.DbException;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    // Método para registrar um novo usuário
    public void register(User user) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = Database.getConnection();
            st = conn.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword()); // Em uma aplicação real, use hashing!

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    user.setId(id);
                }
                Database.closeResultSet(rs);
                System.out.println("User registered successfully! ID: " + user.getId());
            } else {
                System.out.println("Failed to register user.");
            }
        } catch (SQLException e) {
            // Captura exceção de username duplicado
            if (e.getMessage().contains("duplicate key value violates unique constraint")) {
                System.err.println("Error: Username '" + user.getUsername() + "' already exists. Please choose another one.");
            } else {
                throw new DbException("Error registering user: " + e.getMessage());
            }
        } finally {
            Database.closeStatement(st);
            Database.closeConnection(conn);
        }
    }

    // Método para autenticar um usuário
    public User login(String username, String password) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = Database.getConnection();
            st = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            st.setString(1, username);
            st.setString(2, password); // Em uma aplicação real, compare hashes!

            rs = st.executeQuery();

            if (rs.next()) {
                // Usuário encontrado e senha correspondente
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password")); // Armazenada a senha para completar o objeto, mas evite em prod.
                return user;
            }
            return null; // Usuário não encontrado ou senha incorreta
        } catch (SQLException e) {
            throw new DbException("Error during login: " + e.getMessage());
        } finally {
            Database.closeResultSet(rs);
            Database.closeStatement(st);
            Database.closeConnection(conn);
        }
    }

    // Você pode adicionar outros métodos CRUD se precisar no futuro, como findById, update, delete.
}