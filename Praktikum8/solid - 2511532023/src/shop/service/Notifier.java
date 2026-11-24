package shop.service;

import shop.model.Order;
import shop.notify.NotificationChannel;
import shop.notify.SmsChannel;
import shop.util.Rupiah;

public class Notifier {
    private final NotificationChannel channel;

    public Notifier() {
        this(new SmsChannel());
    }
    public Notifier(NotificationChannel channel) {
        this.channel = channel;
    }
    public void notifyPaid(Order order, long amount) {
        channel.send(order.getAddress().getRecipient(),
                "pesanan " + order.getOrderId() + " sudah dibayar (" + Rupiah.format(amount) + ")");
    }
}