package com.seblak.app.view;

import com.seblak.app.controller.SeblakController;
import com.seblak.app.model.DetailPesanan;
import com.seblak.app.model.Menu;
import com.seblak.app.model.Pesanan;
import com.seblak.app.model.Topping;
import com.seblak.app.repository.MenuRepositoryImpl;
import com.seblak.app.repository.PesananRepositoryImpl;
import com.seblak.app.repository.ToppingRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainView {

    public static void main(String[] args) {
        SeblakController controller = new SeblakController(
            new MenuRepositoryImpl(),
            new ToppingRepositoryImpl(),
            new PesananRepositoryImpl()
        );

        Scanner scanner = new Scanner(System.in);
        List<DetailPesanan> details = new ArrayList<>();
        List<List<Topping>> toppingsPerItem = new ArrayList<>();

        System.out.println("==========================================");
        System.out.println("     SELAMAT DATANG DI SEBLAK IBU LINA    ");
        System.out.println("==========================================");

        List<Menu> daftarMenu = controller.getDaftarMenu();
        if (daftarMenu.isEmpty()) {
            System.out.println("Menu tidak tersedia saat ini.");
            return;
        }

        System.out.println("\n--- DAFTAR MENU ---");
        for (int i = 0; i < daftarMenu.size(); i++) {
            Menu m = daftarMenu.get(i);
            System.out.printf("%d. %s - Rp %.0f\n", (i + 1), m.getNamaMenu(), m.getHargaBase());
        }

        double totalHarga = 0.0;
        boolean tambahMenuLagi = true;

        while (tambahMenuLagi) {
            System.out.print("\nPilih nomor menu yang ingin dibeli: ");
            int pilihanMenu = scanner.nextInt() - 1;

            if (pilihanMenu < 0 || pilihanMenu >= daftarMenu.size()) {
                System.out.println("Pilihan menu tidak valid!");
                continue;
            }

            Menu menuDipilih = daftarMenu.get(pilihanMenu);

            System.out.print("Level pedas (0 - 5): ");
            int levelPedas = scanner.nextInt();

            System.out.print("Jumlah porsi: ");
            int jumlahPorsi = scanner.nextInt();

            double subtotalItem = menuDipilih.getHargaBase() * jumlahPorsi;
            List<Topping> toppingsDipilih = new ArrayList<>();

            System.out.print("Apakah ingin tambah topping? (y/n): ");
            String jawabTopping = scanner.next();

            if (jawabTopping.equalsIgnoreCase("y")) {
                List<Topping> daftarTopping = controller.getDaftarTopping();
                System.out.println("\n--- DAFTAR TOPPING ---");
                for (int j = 0; j < daftarTopping.size(); j++) {
                    Topping t = daftarTopping.get(j);
                    System.out.printf("%d. %s - Rp %.0f\n", (j + 1), t.getNamaTopping(), t.getHarga());
                }

                boolean tambahToppingLagi = true;
                while (tambahToppingLagi) {
                    System.out.print("Pilih nomor topping (0 untuk selesai): ");
                    int pilihTopping = scanner.nextInt();
                    if (pilihTopping > 0 && pilihTopping <= daftarTopping.size()) {
                        Topping toppingDipilih = daftarTopping.get(pilihTopping - 1);
                        toppingsDipilih.add(toppingDipilih);
                        subtotalItem += (toppingDipilih.getHarga() * jumlahPorsi);
                    } else {
                        tambahToppingLagi = false;
                    }
                }
            }

            DetailPesanan detail = new DetailPesanan(menuDipilih.getIdMenu(), levelPedas, jumlahPorsi, subtotalItem);
            details.add(detail);
            toppingsPerItem.add(toppingsDipilih);

            totalHarga += subtotalItem;

            System.out.print("Tambah menu seblak lain? (y/n): ");
            String jawabLagi = scanner.next();
            if (!jawabLagi.equalsIgnoreCase("y")) {
                tambahMenuLagi = false;
            }
        }

        System.out.println("\n--- METODE PENGAMBILAN ---");
        System.out.println("1. Takeaway / Bawa Pulang");
        System.out.println("2. Delivery / Antar");
        System.out.print("Pilih (1/2): ");
        int pilihMetode = scanner.nextInt();

        String metode = (pilihMetode == 2) ? "DELIVERY" : "TAKEAWAY";
        double ongkir = (pilihMetode == 2) ? 5000.0 : 0.0;
        totalHarga += ongkir;

        Pesanan pesananBaru = new Pesanan(metode, ongkir, totalHarga);

        System.out.println("\nMemproses pesanan...");
        boolean sukses = controller.prosesPesanan(pesananBaru, details, toppingsPerItem);

        if (sukses) {
            System.out.println("==========================================");
            System.out.println("   PESANAN BERHASIL DISIMPAN KE DATABASE! ");
            System.out.println("   ID Pesanan: " + pesananBaru.getIdPesanan());
            System.out.printf("   Total Bayar: Rp %.0f\n", pesananBaru.getTotalHarga());
            System.out.println("==========================================");
        } else {
            System.out.println("Gagal memproses pesanan. Silakan coba lagi.");
        }
    }
}