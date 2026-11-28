package shop.service;

import shop.config.StoreConfig;
import shop.model.Order;

/** Menghitung ongkir dan total pesanan. */
public class OrderCalculator {
    private final StoreConfig config = StoreConfig.getInstance();

    public long subtotal(Order order) {
        long sum = 0;
        for (Order.Line line : order.getLines()) {
            sum += line.product().price() * line.quantity();
        }
        return sum;
    }

    /** Berat dibulatkan ke atas per kg, minimal 1 kg. */
    public int weightKg(Order order) {
        int grams = 0;
        for (Order.Line line : order.getLines()) {
            grams += line.product().weightGram() * line.quantity();
        }
        return Math.max(1, (grams + 999) / 1000);
    }

    /** Ongkir = berat x tarif per kg; pengiriman ekspres dikali 2. */
    public long shipping(Order order) {
        long cost = weightKg(order) * config.getShippingPerKg();
        return order.isExpress() ? cost * 2 : cost;
    }

    public long total(Order order) {
        long total = subtotal(order) + shipping(order);
        if (order.isGiftWrap()) {
            total += config.getGiftWrapFee();
        }
        return total;
    }
}
