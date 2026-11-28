package shop.service;

import shop.config.StoreConfig;
import shop.model.Customer;
import shop.model.Order;

/** Mencetak invoice ke layar. */
public class InvoicePrinter {
    private final StoreConfig config = StoreConfig.getInstance();
    private final OrderCalculator calculator;

    public InvoicePrinter(OrderCalculator calculator) {
        this.calculator = calculator;
    }

    public void print(String invoiceNumber, Customer customer, Order order) {
        System.out.println("=== " + config.getStoreName() + " ===");
        System.out.println("Invoice     : " + invoiceNumber + " (pesanan " + order.getOrderId() + ")");
        System.out.println("Pelanggan   : " + customer.getName() + " (" + customer.getMemberLevel() + ")");
        System.out.println("Kirim ke    : " + order.getAddress().recipient() + ", " + order.getAddress().city());
        for (Order.Line line : order.getLines()) {
            long amount = line.product().price() * line.quantity();
            System.out.println("  - " + line.product().name() + " x" + line.quantity()
                    + " = " + Rupiah.format(amount));
        }
        String note = order.getNote();
        if (note != null && !note.isEmpty()) {
            System.out.println("Catatan     : " + note);
        }
        System.out.println("Bungkus kado: " + (order.isGiftWrap() ? "ya" : "tidak"));
        System.out.println("Pengiriman  : " + (order.isExpress() ? "EKSPRES" : "REGULER"));
        System.out.println("Ongkir      : " + calculator.weightKg(order) + " kg x "
                + Rupiah.format(config.getShippingPerKg()) + "/kg"
                + (order.isExpress() ? " x2" : "")
                + " = " + Rupiah.format(calculator.shipping(order)));
        System.out.println("TOTAL       : " + Rupiah.format(calculator.total(order)));
        System.out.println();
    }
}
