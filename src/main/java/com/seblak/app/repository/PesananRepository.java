package com.seblak.app.repository;

import com.seblak.app.model.DetailPesanan;
import com.seblak.app.model.Pesanan;
import com.seblak.app.model.Topping;
import java.util.List;

public interface PesananRepository {
    boolean saveOrder(Pesanan pesanan, List<DetailPesanan> details, List<List<Topping>> toppingsPerItem);
}