package shop.demo;

import shop.model.Customer;

/** Bahan Bagian E. Salin ke src/shop/demo/ SETELAH Customer.Builder selesai dibuat. */
public class CustomerCheckDemo {

    public static void main(String[] args) {
        tryBuild("Pelanggan tanpa nama", () -> new Customer.Builder("", "081234567890").build());
        tryBuild("Nomor HP salah", () -> new Customer.Builder("Rina", "12345").build());

        Customer rina = new Customer.Builder("Rina", "081299990000")
                .email("rina@mail.com")
                .build();
        System.out.println("Berhasil: " + rina.getName() + " (" + rina.getMemberLevel() + "), "
                + rina.getEmail());
    }

    private static void tryBuild(String label, Runnable action) {
        try {
            action.run();
            System.out.println(label + ": DITERIMA (seharusnya ditolak!)");
        } catch (IllegalStateException e) {
            System.out.println(label + ": ditolak -> " + e.getMessage());
        }
    }
}
