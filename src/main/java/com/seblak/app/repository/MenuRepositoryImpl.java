package com.seblak.app.repository;

import com.seblak.app.config.DatabaseConnection;
import com.seblak.app.model.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuRepositoryImpl implements MenuRepository {

    @Override
    public List<Menu> getAllActiveMenus() {
        List<Menu> menus = new ArrayList<>();
        String query = "SELECT * FROM menu WHERE status_aktif = 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Menu menu = new Menu(
                    rs.getInt("id_menu"),
                    rs.getString("nama_menu"),
                    rs.getDouble("harga"),
                    rs.getBoolean("status_aktif")
                );
                menus.add(menu);
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data menu: " + e.getMessage());
        }

        return menus;
    }
}