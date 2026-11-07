package com.seblak.app.model;

public class DetailPesanan {
    private int idMenu;
    private int levelPedas;
    private int jumlahPorsi;
    private double subtotal;

    public DetailPesanan(int idMenu, int levelPedas, int jumlahPorsi, double subtotal) {
        this.idMenu = idMenu;
        this.levelPedas = levelPedas;
        this.jumlahPorsi = jumlahPorsi;
        this.subtotal = subtotal;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public int getLevelPedas() {
        return levelPedas;
    }

    public int getJumlahPorsi() {
        return jumlahPorsi;
    }

    public double getSubtotal() {
        return subtotal;
    }
}