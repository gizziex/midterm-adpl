package com.seblak.app.model;

public class Menu {
    private int idMenu;
    private String namaMenu;
    private double harga;
    private boolean statusAktif;

    public Menu(int idMenu, String namaMenu, double harga, boolean statusAktif) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.statusAktif = statusAktif;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public double getHarga() {
        return harga;
    }

    public boolean isStatusAktif() {
        return statusAktif;
    }

    @Override
    public String toString() {
        return namaMenu + " - Rp " + harga;
    }
}
public class Menu {
    
}
