
package com.seblak.app;

import com.seblak.app.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("   SEBLAK IBU LINA");
        System.out.println("   Database Connection Test");
        System.out.println("================================");

        try (Connection connection =
                DatabaseConnection.getConnection()) {

            System.out.println("Koneksi database berhasil!");
            System.out.println("Database: " +
                    connection.getCatalog());

        } catch (SQLException e) {

            System.out.println("Koneksi database gagal!");
            System.out.println("Pesan error: " + e.getMessage());

        }

    }
}
