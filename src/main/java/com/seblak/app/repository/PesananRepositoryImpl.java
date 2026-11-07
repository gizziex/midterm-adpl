package com.seblak.app.repository;

import com.seblak.app.config.DatabaseConnection;
import com.seblak.app.model.DetailPesanan;
import com.seblak.app.model.Pesanan;
import com.seblak.app.model.Topping;

import java.sql.*;
import java.util.List;

public class PesananRepositoryImpl implements PesananRepository {

    @Override
    public boolean saveOrder(Pesanan pesanan, List<DetailPesanan> details, List<List<Topping>> toppingsPerItem) {
        String queryPesanan = "INSERT INTO pesanan (metode_pengambilan, ongkir, total_harga, status_pesanan) VALUES (?, ?, ?, ?)";
        String queryDetail = "INSERT INTO detail_pesanan (id_pesanan, id_menu, level_pedas, jumlah_porsi, subtotal) VALUES (?, ?, ?, ?, ?)";
        String queryTopping = "INSERT INTO detail_topping (id_detail, id_topping, jumlah) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPesanan = conn.prepareStatement(queryPesanan, Statement.RETURN_GENERATED_KEYS)) {
                stmtPesanan.setString(1, pesanan.getMetodePengambilan());
                stmtPesanan.setDouble(2, pesanan.getOngkir());
                stmtPesanan.setDouble(3, pesanan.getTotalHarga());
                stmtPesanan.setString(4, pesanan.getStatusPesanan());
                stmtPesanan.executeUpdate();

                try (ResultSet rsKeys = stmtPesanan.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        int idPesananBaru = rsKeys.getInt(1);
                        pesanan.setIdPesanan(idPesananBaru);
                    } else {
                        throw new SQLException("Gagal mendapatkan ID pesanan yang baru dibuat.");
                    }
                }
            }

            for (int i = 0; i < details.size(); i++) {
                DetailPesanan detail = details.get(i);
                int idDetailBaru = 0;

                try (PreparedStatement stmtDetail = conn.prepareStatement(queryDetail, Statement.RETURN_GENERATED_KEYS)) {
                    stmtDetail.setInt(1, pesanan.getIdPesanan());
                    stmtDetail.setInt(2, detail.getIdMenu());
                    stmtDetail.setInt(3, detail.getLevelPedas());
                    stmtDetail.setInt(4, detail.getJumlahPorsi());
                    stmtDetail.setDouble(5, detail.getSubtotal());
                    stmtDetail.executeUpdate();

                    try (ResultSet rsDetailKeys = stmtDetail.getGeneratedKeys()) {
                        if (rsDetailKeys.next()) {
                            idDetailBaru = rsDetailKeys.getInt(1);
                        } else {
                            throw new SQLException("Gagal mendapatkan ID detail pesanan.");
                        }
                    }
                }

                List<Topping> toppings = toppingsPerItem.get(i);
                if (toppings != null && !toppings.isEmpty()) {
                    try (PreparedStatement stmtTopping = conn.prepareStatement(queryTopping)) {
                        for (Topping topping : toppings) {
                            stmtTopping.setInt(1, idDetailBaru);
                            stmtTopping.setInt(2, topping.getIdTopping());
                            stmtTopping.setInt(3, detail.getJumlahPorsi());
                            stmtTopping.addBatch();
                        }
                        stmtTopping.executeBatch();
                    }
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("Gagal menyimpan pesanan: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}