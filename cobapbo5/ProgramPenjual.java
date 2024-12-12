import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class ProgramPenjual {

    private static final String DB_URL = "jdbc:mysql://localhost:3307/supermarket";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        Random random = new Random();
        int captcha = random.nextInt(9000) + 1000;

        // Bagian Login
        while (true) {
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JTextField captchaField = new JTextField();

            loginPanel.add(new JLabel("Username:"));
            loginPanel.add(usernameField);
            loginPanel.add(new JLabel("Password:"));
            loginPanel.add(passwordField);
            loginPanel.add(new JLabel("Captcha: (" + captcha + ")"));
            loginPanel.add(captchaField);

            int result = JOptionPane.showConfirmDialog(null, loginPanel, "Login Admin", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String userCaptcha = captchaField.getText();

                if (username.equals("kwnhoon") && password.equals("160198") && userCaptcha.equals(String.valueOf(captcha))) {
                    JOptionPane.showMessageDialog(null, "Login berhasil!");
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Login gagal, silakan coba lagi!");
                    captcha = random.nextInt(9000) + 1000;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Login dibatalkan.");
                System.exit(0);
            }
        }

        // Ubah warna background popup
        UIManager.put("Panel.background", new Color(230, 240, 255)); // Warna biru muda
        UIManager.put("OptionPane.background", new Color(230, 240, 255));
        UIManager.put("OptionPane.messageForeground", Color.BLACK);

        // Koneksi ke Database
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            JOptionPane.showMessageDialog(null, "Koneksi ke database berhasil!", "Info", JOptionPane.INFORMATION_MESSAGE);

            while (true) {
                // Menu vertikal menggunakan JList
                String[] options = {"Tambah Transaksi", "Lihat Semua Transaksi", "Perbarui Transaksi", "Hapus Transaksi", "Keluar"};
                JList<String> menuList = new JList<>(options);
                menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                menuList.setVisibleRowCount(options.length);
                menuList.setBackground(new Color(210, 230, 250)); // Warna menu
                menuList.setForeground(Color.BLACK);

                JPanel menuPanel = new JPanel(new BorderLayout());
                menuPanel.add(new JLabel("Pilih Menu:"), BorderLayout.NORTH);
                menuPanel.add(new JScrollPane(menuList), BorderLayout.CENTER);

                int result = JOptionPane.showConfirmDialog(null, menuPanel, "Menu Transaksi Supermarket",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    int choice = menuList.getSelectedIndex();

                    switch (choice) {
                        case 0:
                            createTransaction(connection);
                            break;
                        case 1:
                            readTransactions(connection);
                            break;
                        case 2:
                            updateTransaction(connection);
                            break;
                        case 3:
                            deleteTransaction(connection);
                            break;
                        case 4:
                            JOptionPane.showMessageDialog(null, "Keluar dari program.", "Info", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Pilihan tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Program ditutup.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Koneksi ke database gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createTransaction(Connection connection) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField noFakturField = new JTextField();
        JTextField kodeBarangField = new JTextField();
        JTextField namaBarangField = new JTextField();
        JTextField hargaBarangField = new JTextField();
        JTextField jumlahBeliField = new JTextField();
        JTextField namaKasirField = new JTextField();

        panel.add(new JLabel("No. Faktur:"));
        panel.add(noFakturField);
        panel.add(new JLabel("Kode Barang:"));
        panel.add(kodeBarangField);
        panel.add(new JLabel("Nama Barang:"));
        panel.add(namaBarangField);
        panel.add(new JLabel("Harga Barang:"));
        panel.add(hargaBarangField);
        panel.add(new JLabel("Jumlah Beli:"));
        panel.add(jumlahBeliField);
        panel.add(new JLabel("Nama Kasir:"));
        panel.add(namaKasirField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Tambah Transaksi", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String noFaktur = noFakturField.getText();
                String kodeBarang = kodeBarangField.getText();
                String namaBarang = namaBarangField.getText();
                double hargaBarang = Double.parseDouble(hargaBarangField.getText());
                int jumlahBeli = Integer.parseInt(jumlahBeliField.getText());
                String namaKasir = namaKasirField.getText();

                double total = hargaBarang * jumlahBeli;
                LocalDateTime currentTime = LocalDateTime.now();
                String formattedDate = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                String sql = "INSERT INTO transactions (no_faktur, kode_barang, nama_barang, harga_barang, jumlah_beli, total, tanggal_waktu, nama_kasir) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, noFaktur);
                    stmt.setString(2, kodeBarang);
                    stmt.setString(3, namaBarang);
                    stmt.setDouble(4, hargaBarang);
                    stmt.setInt(5, jumlahBeli);
                    stmt.setDouble(6, total);
                    stmt.setString(7, formattedDate);
                    stmt.setString(8, namaKasir);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Transaksi berhasil ditambahkan!", "Info", JOptionPane.INFORMATION_MESSAGE);

                    // Menampilkan struk setelah transaksi berhasil
                    Struk struk = new Struk(noFaktur, kodeBarang, namaBarang, hargaBarang, jumlahBeli, namaKasir);
                    struk.tampilkanData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal menambahkan transaksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input tidak valid. Harap masukkan angka untuk harga dan jumlah beli.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void readTransactions(Connection connection) {
        String sql = "SELECT * FROM transactions";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            StringBuilder result = new StringBuilder("=== Daftar Transaksi ===\n");
            while (rs.next()) {
                result.append(String.format("ID: %d | No. Faktur: %s | Kode Barang: %s | Nama Barang: %s | Harga: %.2f | Jumlah: %d | Total: %.2f | Tanggal: %s | Kasir: %s\n",
                        rs.getInt("id"), rs.getString("no_faktur"), rs.getString("kode_barang"),
                        rs.getString("nama_barang"), rs.getDouble("harga_barang"), rs.getInt("jumlah_beli"),
                        rs.getDouble("total"), rs.getTimestamp("tanggal_waktu"), rs.getString("nama_kasir")));
            }
            JOptionPane.showMessageDialog(null, result.toString(), "Daftar Transaksi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal membaca transaksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateTransaction(Connection connection) {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Masukkan ID transaksi yang ingin diperbarui:", "Input", JOptionPane.QUESTION_MESSAGE));
            String namaBarang = JOptionPane.showInputDialog(null, "Masukkan Nama Barang baru:", "Input", JOptionPane.QUESTION_MESSAGE);
            double hargaBarang = Double.parseDouble(JOptionPane.showInputDialog(null, "Masukkan Harga Barang baru:", "Input", JOptionPane.QUESTION_MESSAGE));
            int jumlahBeli = Integer.parseInt(JOptionPane.showInputDialog(null, "Masukkan Jumlah Beli baru:", "Input", JOptionPane.QUESTION_MESSAGE));

            double total = hargaBarang * jumlahBeli;

            String sql = "UPDATE transactions SET nama_barang = ?, harga_barang = ?, jumlah_beli = ?, total = ? WHERE id = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, namaBarang);
                stmt.setDouble(2, hargaBarang);
                stmt.setInt(3, jumlahBeli);
                stmt.setDouble(4, total);
                stmt.setInt(5, id);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Transaksi berhasil diperbarui!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui transaksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Input tidak valid. Harap masukkan angka untuk harga dan jumlah beli.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void deleteTransaction(Connection connection) {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Masukkan ID transaksi yang ingin dihapus:", "Input", JOptionPane.QUESTION_MESSAGE));

            String sql = "DELETE FROM transactions WHERE id = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Transaksi berhasil dihapus!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus transaksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Input tidak valid. Harap masukkan angka untuk ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
