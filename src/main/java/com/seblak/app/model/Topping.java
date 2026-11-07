package com.seblak.app.model;

public class Topping {
    private int idTopping;
    private String namaTopping;
    private double harga;

    public Topping(int idTopping, String namaTopping, double harga) {
        this.idTopping = idTopping;
        this.namaTopping = namaTopping;
        this.harga = harga;
    }

    public int getIdTopping() {
        return idTopping;
    }

    public String getNamaTopping() {
        return namaTopping;
    }

    public double getHarga() {
        return harga;
    }
}