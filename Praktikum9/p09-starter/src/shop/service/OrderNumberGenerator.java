package shop.service;

/** Membuat nomor invoice berurutan: INV-1001, INV-1002, ... */
public class OrderNumberGenerator {
    private static OrderNumberGenerator instance;
    private int counter = 1000;

    private OrderNumberGenerator() { }

    public static OrderNumberGenerator getInstance() {
        if (instance == null) {
            instance = new OrderNumberGenerator();
        }
        return instance;
    }

    public String next() {
        counter++;
        return "INV-" + counter;
    }
}
