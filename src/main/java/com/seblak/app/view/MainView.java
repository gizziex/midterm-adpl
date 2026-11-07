package com.seblak.app.view;

import com.seblak.app.controller.SeblakController;
import com.seblak.app.model.DetailPesanan;
import com.seblak.app.model.Menu;
import com.seblak.app.model.Pesanan;
import com.seblak.app.model.Topping;
import com.seblak.app.repository.MenuRepositoryImpl;
import com.seblak.app.repository.PesananRepositoryImpl;
import com.seblak.app.repository.ToppingRepositoryImpl;
import com.seblak.app.service.PesananService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainView extends JFrame {

    private static final Font FONT_BASE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    private final SeblakController controller;
    private JComboBox<String> comboMenu;
    private JSpinner spinLevel;
    private JSpinner spinPorsi;
    private JPanel panelTopping;
    private JRadioButton radioTakeaway;
    private JRadioButton radioDelivery;
    private JTextArea txtDetailOrder;
    private JLabel lblTotal;

    private List<Menu> daftarMenu;
    private List<Topping> daftarTopping;
    private List<JCheckBox> checkToppings;

    private List<DetailPesanan> details = new ArrayList<>();
    private List<List<Topping>> toppingsPerItem = new ArrayList<>();
    private double totalHarga = 0.0;

    public MainView() {
        controller = new SeblakController(
            new MenuRepositoryImpl(),
            new ToppingRepositoryImpl(),
            new PesananService(
                new PesananRepositoryImpl()
            )
        );

        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Seblak Ibu Lina");
        setSize(820, 620);
        setMinimumSize(new Dimension(720, 560));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 10));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 12, 12, 12));

        JLabel lblTitle = new JLabel("SEBLAK IBU LINA", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        mainPanel.add(buildFormPanel());
        mainPanel.add(buildSummaryPanel());
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel form = new JPanel(new BorderLayout(0, 8));
        form.setBorder(panelBorder("Form Pemesanan"));

        comboMenu = new JComboBox<>();
        comboMenu.setFont(FONT_BASE);
        spinLevel = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        spinPorsi = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        styleSpinner(spinLevel);
        styleSpinner(spinPorsi);

        JPanel fields = new JPanel(new GridBagLayout());
        addField(fields, "Pilih Menu", comboMenu, 0);
        addField(fields, "Level Pedas (0-5)", spinLevel, 1);
        addField(fields, "Jumlah Porsi", spinPorsi, 2);
        form.add(fields, BorderLayout.NORTH);

        JPanel toppingWrap = new JPanel(new BorderLayout(0, 4));
        JLabel lblTopping = new JLabel("Pilih Topping");
        lblTopping.setFont(FONT_BOLD);
        toppingWrap.add(lblTopping, BorderLayout.NORTH);

        panelTopping = new JPanel();
        panelTopping.setLayout(new BoxLayout(panelTopping, BoxLayout.Y_AXIS));
        panelTopping.setBorder(new EmptyBorder(4, 6, 4, 6));
        JScrollPane scrollTopping = new JScrollPane(panelTopping);
        scrollTopping.getVerticalScrollBar().setUnitIncrement(16);
        toppingWrap.add(scrollTopping, BorderLayout.CENTER);
        form.add(toppingWrap, BorderLayout.CENTER);

        JButton btnTambah = new JButton("Tambah ke Keranjang");
        btnTambah.setFont(FONT_BOLD);
        btnTambah.setPreferredSize(new Dimension(0, 36));
        btnTambah.addActionListener(e -> tambahKeKeranjang());
        form.add(btnTambah, BorderLayout.SOUTH);

        return form;
    }

    private void addField(JPanel panel, String label, JComponent comp, int row) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;

        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_BOLD);
        gc.gridy = row * 2;
        gc.insets = new Insets(0, 0, 4, 0);
        panel.add(lbl, gc);

        comp.setPreferredSize(new Dimension(0, 30));
        gc.gridy = row * 2 + 1;
        gc.insets = new Insets(0, 0, 12, 0);
        panel.add(comp, gc);
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(FONT_BASE);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setHorizontalAlignment(JTextField.LEFT);
        }
    }

    private JPanel buildSummaryPanel() {
        JPanel summary = new JPanel(new BorderLayout(0, 8));
        summary.setBorder(panelBorder("Ringkasan Pesanan"));

        txtDetailOrder = new JTextArea();
        txtDetailOrder.setEditable(false);
        txtDetailOrder.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtDetailOrder.setMargin(new Insets(8, 8, 8, 8));
        summary.add(new JScrollPane(txtDetailOrder), BorderLayout.CENTER);

        radioTakeaway = new JRadioButton("Takeaway (Bawa Pulang)", true);
        radioDelivery = new JRadioButton("Delivery (+" + rp(5000) + ")");
        radioTakeaway.setFont(FONT_BASE);
        radioDelivery.setFont(FONT_BASE);
        ButtonGroup groupMetode = new ButtonGroup();
        groupMetode.add(radioTakeaway);
        groupMetode.add(radioDelivery);

        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelRadio.add(radioTakeaway);
        panelRadio.add(radioDelivery);

        lblTotal = new JLabel("Total: " + rp(0), JLabel.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton btnSubmit = new JButton("PROSES SIMPAN PESANAN");
        btnSubmit.setBackground(new Color(46, 204, 113));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setOpaque(true);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setPreferredSize(new Dimension(0, 42));

        JPanel bottom = new JPanel(new GridLayout(3, 1, 0, 8));
        bottom.add(panelRadio);
        bottom.add(lblTotal);
        bottom.add(btnSubmit);
        summary.add(bottom, BorderLayout.SOUTH);

        radioTakeaway.addActionListener(e -> hitungTotalAkhir());
        radioDelivery.addActionListener(e -> hitungTotalAkhir());
        btnSubmit.addActionListener(e -> prosesSimpan());

        return summary;
    }

    private javax.swing.border.Border panelBorder(String title) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(" " + title + " "),
            new EmptyBorder(6, 10, 10, 10)
        );
    }

    private String rp(double nilai) {
        return "Rp " + NumberFormat.getInstance(Locale.forLanguageTag("id-ID")).format((long) nilai);
    }

    private void loadData() {
        comboMenu.removeAllItems();
        panelTopping.removeAll();

        daftarMenu = controller.getDaftarMenu();
        for (Menu m : daftarMenu) {
            comboMenu.addItem(m.getNamaMenu() + " - " + rp(m.getHargaBase()));
        }

        daftarTopping = new ArrayList<>();
        Set<String> sudahAda = new HashSet<>();
        for (Topping t : controller.getDaftarTopping()) {
            if (sudahAda.add(t.getNamaTopping().trim().toLowerCase())) {
                daftarTopping.add(t);
            }
        }

        checkToppings = new ArrayList<>();
        for (Topping t : daftarTopping) {
            JCheckBox cb = new JCheckBox(t.getNamaTopping() + " (+" + rp(t.getHarga()) + ")");
            cb.setFont(FONT_BASE);
            checkToppings.add(cb);
            panelTopping.add(cb);
        }
        panelTopping.revalidate();
        panelTopping.repaint();
    }

    private void tambahKeKeranjang() {
        int indexMenu = comboMenu.getSelectedIndex();
        if (indexMenu < 0) return;

        Menu menu = daftarMenu.get(indexMenu);
        int level = (int) spinLevel.getValue();
        int porsi = (int) spinPorsi.getValue();

        double subtotal = menu.getHargaBase() * porsi;
        List<Topping> toppingsDipilih = new ArrayList<>();

        for (int i = 0; i < checkToppings.size(); i++) {
            if (checkToppings.get(i).isSelected()) {
                Topping t = daftarTopping.get(i);
                toppingsDipilih.add(t);
                subtotal += (t.getHarga() * porsi);
            }
        }

        DetailPesanan detail = new DetailPesanan(menu.getIdMenu(), level, porsi, subtotal);
        details.add(detail);
        toppingsPerItem.add(toppingsDipilih);

        txtDetailOrder.append(porsi + "x " + menu.getNamaMenu() + " (Lvl " + level + ")\n");
        for (Topping t : toppingsDipilih) {
            txtDetailOrder.append("  + " + t.getNamaTopping() + "\n");
        }
        txtDetailOrder.append("  Subtotal: " + rp(subtotal) + "\n----------------------------\n");

        hitungTotalAkhir();
        resetFormItem();
    }

    private void hitungTotalAkhir() {
        double ongkir = radioDelivery.isSelected() ? 5000.0 : 0.0;

        totalHarga = controller.hitungTotal(details, ongkir);

        lblTotal.setText("Total: " + rp(totalHarga));
    }

    private void resetFormItem() {
        spinLevel.setValue(0);
        spinPorsi.setValue(1);
        for (JCheckBox cb : checkToppings) {
            cb.setSelected(false);
        }
    }

    private void prosesSimpan() {
        if (details.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String metode = radioDelivery.isSelected() ? "DELIVERY" : "PICKUP";
        double ongkir = radioDelivery.isSelected() ? 5000.0 : 0.0;

        Pesanan pesananBaru = new Pesanan(metode, ongkir, totalHarga);
        boolean sukses = controller.prosesPesanan(pesananBaru, details, toppingsPerItem);

        if (sukses) {
            JOptionPane.showMessageDialog(this,
                "Pesanan Berhasil Disimpan ke Database!\nID Pesanan: " + pesananBaru.getIdPesanan()
                    + "\nTotal Bayar: " + rp(totalHarga),
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            details.clear();
            toppingsPerItem.clear();
            txtDetailOrder.setText("");
            hitungTotalAkhir();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan pesanan ke database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new MainView().setVisible(true));
    }
}