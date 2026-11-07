package com.seblak.app.controller;

import com.seblak.app.model.DetailPesanan;
import com.seblak.app.model.Menu;
import com.seblak.app.model.Pesanan;
import com.seblak.app.model.Topping;
import com.seblak.app.repository.MenuRepository;
import com.seblak.app.repository.PesananRepository;
import com.seblak.app.repository.ToppingRepository;

import java.util.List;

public class SeblakController {

    private final MenuRepository menuRepository;
    private final ToppingRepository toppingRepository;
    private final PesananRepository pesananRepository;

    public SeblakController(MenuRepository menuRepository, ToppingRepository toppingRepository, PesananRepository pesananRepository) {
        this.menuRepository = menuRepository;
        this.toppingRepository = toppingRepository;
        this.pesananRepository = pesananRepository;
    }

    public List<Menu> getDaftarMenu() {
        return menuRepository.getAllAvailableMenus();
    }

    public List<Topping> getDaftarTopping() {
        return toppingRepository.getAllAvailableToppings();
    }

    public boolean prosesPesanan(Pesanan pesanan, List<DetailPesanan> details, List<List<Topping>> toppingsPerItem) {
        return pesananRepository.saveOrder(pesanan, details, toppingsPerItem);
    }
}