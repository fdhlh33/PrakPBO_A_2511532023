package check;

import shop.app.MyNim;

/**
 * Parameter praktikum yang bergantung pada NIM. JANGAN DIUBAH.
 * Dosen dapat menguji NIM lain dengan opsi JVM -Dnim=2401010057.
 */
public final class NimParams {
    private NimParams() { }

    public static String nim() {
        return System.getProperty("nim", MyNim.NIM).trim();
    }

    public static boolean isValid() {
        return nim().matches("\\d{8,15}");
    }

    /** Dua digit terakhir NIM (0 bila NIM belum diisi). */
    public static int lastTwoDigits() {
        if (!isValid()) {
            return 0;
        }
        String s = nim();
        return Integer.parseInt(s.substring(s.length() - 2));
    }

    /** Ongkir per kg yang diset admin: 10.000 + (n mod 3) x 1.000. */
    public static long newShippingPerKg() {
        return 10_000 + (lastTwoDigits() % 3) * 1_000L;
    }

    public static void main(String[] args) {
        System.out.println("NIM                 : " + nim() + (isValid() ? "" : "  <- BELUM DIISI DENGAN BENAR"));
        System.out.println("Ongkir baru (admin) : Rp" + String.format("%,d", newShippingPerKg()).replace(',', '.') + "/kg");
    }
}
