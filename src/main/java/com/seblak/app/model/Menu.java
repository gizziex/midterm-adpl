package com.seblak.app.model;

public class Menu {
    private int idMenu;
    private String namaMenu;
    private double hargaBase;
    private boolean tersedia;

    public Menu(int idMenu, String namaMenu, double hargaBase, boolean tersedia) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.hargaBase = hargaBase;
        this.tersedia = tersedia;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public double getHargaBase() {
        return hargaBase;
    }

    public boolean isTersedia() {
        return tersedia;
    }
}