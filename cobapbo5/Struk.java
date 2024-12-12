import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

class Struk extends Barang {
    String noFaktur;
    int jumlahBeli;
    double total;
    String namaKasir;

    public Struk(String noFaktur, String kodeBarang, String namaBarang, double hargaBarang, int jumlahBeli, String namaKasir) {
        super(kodeBarang, namaBarang, hargaBarang);
        if (jumlahBeli < 0) {
            throw new IllegalArgumentException("Jumlah beli tidak boleh nol!");
        }
        this.noFaktur = noFaktur;
        this.jumlahBeli = jumlahBeli;
        this.total = hargaBarang * jumlahBeli;
        this.namaKasir = namaKasir;
    }

    public void tampilkanData() {
        String waktu = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        JOptionPane.showMessageDialog(null,
                "Selamat Datang di PizzaBoy\n" +
                        "Tanggal dan Waktu : " + waktu + "\n" +
                        "+----------------------------------------------------+\n" +
                        "No. Faktur      : " + noFaktur + "\n" +
                        "Kode Barang  : " + kodeBarang + "\n" +
                        "Nama Barang : " + namaBarang + "\n" +
                        "Harga Barang : " + hargaBarang + "\n" +
                        "Jumlah Beli     : " + jumlahBeli + "\n" +
                        "TOTAL             : " + total + "\n" +
                        "+----------------------------------------------------+\n" +
                        "Kasir : " + namaKasir + "\n" +
                        "+----------------------------------------------------+"
        );
    }
}