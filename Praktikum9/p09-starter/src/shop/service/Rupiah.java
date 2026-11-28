package shop.service;

/** Format angka rupiah, mis. 85000 -> "Rp85.000". */
public final class Rupiah {
    private Rupiah() { }

    public static String format(long amount) {
        return "Rp" + String.format("%,d", amount).replace(',', '.');
    }
}
