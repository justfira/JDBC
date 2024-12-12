class Barang {
    String kodeBarang;
    String namaBarang;
    double hargaBarang;

    public Barang(String kodeBarang, String namaBarang, double hargaBarang) {
        if (hargaBarang < 0) {
            throw new IllegalArgumentException("Harga barang tidak boleh negatif!");
        }
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.hargaBarang = hargaBarang;
    }
}

