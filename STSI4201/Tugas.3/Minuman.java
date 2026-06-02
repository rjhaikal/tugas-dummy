public class Minuman extends MenuItem {

    private String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        super(nama, harga, "Minuman");
        this.jenisMinuman = jenisMinuman;
    }

    public String getJenisMinuman()                  { return jenisMinuman; }
    public void setJenisMinuman(String jenisMinuman) { this.jenisMinuman = jenisMinuman; }

    @Override
    public void tampilMenu() {
        System.out.printf("  [MINUMAN] %-25s | Jenis: %-12s | Harga: Rp%,.0f%n",
                getNama(), jenisMinuman, getHarga());
    }

    @Override
    public String toFileString() {
        return "Minuman|" + getNama() + "|" + getHarga() + "|" + jenisMinuman;
    }
}
