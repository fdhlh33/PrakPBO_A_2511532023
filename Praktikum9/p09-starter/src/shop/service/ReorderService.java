package shop.service;

import shop.model.Customer;
import shop.model.Order;

/** Memproses pesan ulang (reorder) dari pesanan lama. */
public class ReorderService {
    private final OrderNumberGenerator numbers = OrderNumberGenerator.getInstance();
    private final InvoicePrinter printer;

    public ReorderService(InvoicePrinter printer) {
        this.printer = printer;
    }

    public void reorder(Customer customer, Order previousOrder) {
        System.out.println(">> Pesan ulang dari pesanan " + previousOrder.getOrderId());
        printer.print(numbers.next(), customer, previousOrder);
    }
}
