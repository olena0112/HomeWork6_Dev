package org.example.client;
import org.example.exception.InvalidDataException;
import org.example.storage.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

    public long create(String name) throws SQLException {
        validateName(name);

        try (Connection connection = Database.getInstance().getConnection()) {
            String sql = "INSERT INTO client (NAME) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
            throw new SQLException("Client creation failed, no ID obtained.");
        }
    }

    public String getById(long id) throws SQLException {
        try (Connection connection = Database.getInstance().getConnection()) {
            String sql = "SELECT NAME FROM client WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("NAME");
            } else {
                throw new SQLException("Client not found with ID: " + id);
            }
        }
    }

    public void setName(long id, String name) throws SQLException {
        validateName(name);

        try (Connection connection = Database.getInstance().getConnection()) {
            String sql = "UPDATE client SET NAME = ? WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Client not found with ID: " + id);
            }
        }
    }

    public void deleteById(long id) throws SQLException {
        try (Connection connection = Database.getInstance().getConnection()) {
            String sql = "DELETE FROM client WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Client not found with ID: " + id);
            }
        }
    }

    public List<Client> listAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = Database.getInstance().getConnection()) {
            String sql = "SELECT ID, NAME FROM client";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");
                clients.add(new Client(id, name));
            }
        }
        return clients;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() > 1000) {
            throw new InvalidDataException("Invalid client name.");
        }
    }
}

