package com.seblak.app.model;

public class Topping {
    private int idTopping;
    private String namaTopping;
    private double harga;
    private boolean tersedia;

    public Topping(int idTopping, String namaTopping, double harga, boolean tersedia) {
        this.idTopping = idTopping;
        this.namaTopping = namaTopping;
        this.harga = harga;
        this.tersedia = tersedia;
    }

    public Topping(int idTopping, String namaTopping, double harga) {
        this.idTopping = idTopping;
        this.namaTopping = namaTopping;
        this.harga = harga;
        this.tersedia = true;
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

    public boolean isTersedia() {
        return tersedia;
    }
}