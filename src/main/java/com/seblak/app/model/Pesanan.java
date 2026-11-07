package com.seblak.app.model;

public class Pesanan {
    private int idPesanan;
    private String metodePengambilan;
    private double ongkir;
    private double totalHarga;
    private String statusPesanan;

    public Pesanan(String metodePengambilan, double ongkir, double totalHarga) {
        this.metodePengambilan = metodePengambilan;
        this.ongkir = ongkir;
        this.totalHarga = totalHarga;
        this.statusPesanan = "DITERIMA";
    }

    public int getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(int idPesanan) {
        this.idPesanan = idPesanan;
    }

    public String getMetodePengambilan() {
        return metodePengambilan;
    }

    public double getOngkir() {
        return ongkir;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public String getStatusPesanan() {
        return statusPesanan;
    }
}