public class Diskon extends MenuItem {

    private double persentaseDiskon;

    public Diskon(String nama, double harga, double persentaseDiskon) {
        super(nama, harga, "Diskon");
        this.persentaseDiskon = persentaseDiskon;
    }

    public double getPersentaseDiskon()                      { return persentaseDiskon; }
    public void setPersentaseDiskon(double persentaseDiskon) { this.persentaseDiskon = persentaseDiskon; }

    public double getHargaSetelahDiskon() {
        return getHarga() * (1 - persentaseDiskon / 100);
    }

    @Override
    public void tampilMenu() {
        System.out.printf("  [DISKON]  %-25s | Diskon: %5.1f%% | Harga: Rp%,.0f -> Rp%,.0f%n",
                nama, persentaseDiskon, getHarga(), getHargaSetelahDiskon());
    }

    @Override
    public String toFileString() {
        return "Diskon|" + nama + "|" + getHarga() + "|" + persentaseDiskon;
    }
}
