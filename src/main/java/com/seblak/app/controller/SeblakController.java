
package com.seblak.app.controller;

import com.seblak.app.model.DetailPesanan;
import com.seblak.app.model.Menu;
import com.seblak.app.model.Pesanan;
import com.seblak.app.model.Topping;

import com.seblak.app.repository.MenuRepository;
import com.seblak.app.repository.ToppingRepository;
import com.seblak.app.service.PesananService;

import java.util.List;

public class SeblakController {

    private final MenuRepository menuRepository;
    private final ToppingRepository toppingRepository;
    private final PesananService pesananService;

    public SeblakController(
            MenuRepository menuRepository,
            ToppingRepository toppingRepository,
            PesananService pesananService) {

        this.menuRepository = menuRepository;
        this.toppingRepository = toppingRepository;
        this.pesananService = pesananService;
    }

    public List<Menu> getDaftarMenu() {
        return menuRepository.getAllAvailableMenus();
    }

    public List<Topping> getDaftarTopping() {
        return toppingRepository.getAllAvailableToppings();
    }

    public double hitungTotal(
            List<DetailPesanan> details,
            double ongkir) {

        return pesananService.hitungTotal(details, ongkir);
    }

    public boolean prosesPesanan(
            Pesanan pesanan,
            List<DetailPesanan> details,
            List<List<Topping>> toppingsPerItem) {

        return pesananService.prosesPesanan(
                pesanan, details, toppingsPerItem);
    }
}
