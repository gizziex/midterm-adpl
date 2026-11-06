package com.seblak.app.repository;

import com.seblak.app.config.DatabaseConnection;
import com.seblak.app.model.Topping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ToppingRepositoryImpl implements ToppingRepository {

    @Override
    public List<Topping> getAllAvailableToppings() {
        List<Topping> toppings = new ArrayList<>();
        String query = "SELECT * FROM topping WHERE tersedia = 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Topping topping = new Topping(
                    rs.getInt("id_topping"),
                    rs.getString("nama_topping"),
                    rs.getDouble("harga"),
                    rs.getBoolean("tersedia")
                );
                toppings.add(topping);
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data topping: " + e.getMessage());
        }

        return toppings;
    }
}