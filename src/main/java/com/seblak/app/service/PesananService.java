
package com.seblak.app.service;

import com.seblak.app.model.DetailPesanan;
import com.seblak.app.model.Pesanan;
import com.seblak.app.model.Topping;
import com.seblak.app.repository.PesananRepository;

import java.util.List;

public class PesananService {

    private final PesananRepository pesananRepository;

    public PesananService(PesananRepository pesananRepository) {
        this.pesananRepository = pesananRepository;
    }

    // Menghitung total seluruh pesanan
    public double hitungTotal(
            List<DetailPesanan> details,
            double ongkir) {

        double subtotalSemua = 0;

        for (DetailPesanan detail : details) {
            subtotalSemua += detail.getSubtotal();
        }

        return subtotalSemua + ongkir;
    }

    // Memvalidasi dan menyimpan pesanan
    public boolean prosesPesanan(
            Pesanan pesanan,
            List<DetailPesanan> details,
            List<List<Topping>> toppingsPerItem) {

        if (pesanan == null || details == null || details.isEmpty()) {
            return false;
        }

        if (toppingsPerItem == null ||
                toppingsPerItem.size() != details.size()) {
            return false;
        }

        return pesananRepository.saveOrder(
                pesanan, details, toppingsPerItem);
    }
}
