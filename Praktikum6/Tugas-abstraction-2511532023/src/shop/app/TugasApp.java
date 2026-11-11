
package shop.app;

import java.util.List;

import shop.StudentInfo;
import shop.model.Product;
import shop.model.PhysicalProduct;
import shop.model.DigitalProduct;
import shop.shipping.ExpressShipping;
import shop.payment.PaymentMethod;
import shop.payment.EWalletPayment;
import shop.payment.BankTransferPayment;
import shop.payment.CreditCardPayment;
import shop.service.CheckoutService;
import shop.notification.CustomerContact;
import shop.notification.NotificationChannel;
import shop.notification.NotificationService;
import shop.notification.EmailChannel;
import shop.notification.SmsChannel;
import shop.notification.WhatsAppChannel;

public class TugasApp {
    public static void main(String[] args) {
        PhysicalProduct coffee = new PhysicalProduct(
                "KOPI-001", "Kopi Arabika Gayo 250g", 85_000, 300);
        PhysicalProduct rendang = new PhysicalProduct(
                "RDG-002", "Rendang Kemasan 250g", 120_000, 400);
        DigitalProduct ebook = new DigitalProduct(
                "EBK-003", "E-book Resep Kue Nusantara", 50_000,
                "https://toko-umkm.example/unduh/ebk-003");
        List<Product> cart = List.of(coffee, rendang, ebook);

        CheckoutService checkout = new CheckoutService();
        ExpressShipping express = new ExpressShipping();

        EWalletPayment wallet = new EWalletPayment("DompetKu", 300_000);
        BankTransferPayment transfer = new BankTransferPayment("Bank Nagari");
        CreditCardPayment card = new CreditCardPayment("Siti Rahma", 1_000_000);

        System.out.println("Tugas oleh " + StudentInfo.NAME
                + " (" + StudentInfo.NIM + ")");

        System.out.println("\n== T1: Total dengan biaya admin (Ekspres) ==");

        long baseTotal = checkout.total(cart, express);

        PaymentMethod[] payments = {wallet, transfer, card};

        for (PaymentMethod payment : payments) {
            long admin = payment.adminFee(baseTotal);
            long grandTotal = checkout.grandTotal(cart, express, payment);

            System.out.println(payment.getName() + " : Rp" + grandTotal
                    + " (admin Rp" + admin + ")");
        }

        System.out.println("\nKebijakan kartu kredit : " + card.policy());
        System.out.println("Kebijakan e-wallet     : " + wallet.policy());

        System.out.println("\n== T2: Notifikasi status pesanan ==");

        NotificationService notificationService = new NotificationService();

        notificationService.register(new EmailChannel());
        notificationService.register(
                new SmsChannel(StudentInfo.SMS_MAX_LENGTH));
        notificationService.register(new WhatsAppChannel());

        NotificationChannel logChannel = new NotificationChannel() {

            @Override
            public String channelName() {
                return "Console Log";
            }

            @Override
            public boolean send(CustomerContact contact, String message) {
                if (contact == null || message == null || message.isBlank()) {
                    return false;
                }

                System.out.println("[LOG] " + contact.getName()
                        + ": " + message);
                return true;
            }
        };

        notificationService.register(logChannel);

        String message = "Pesanan ORD-0001 sudah dikirim dengan kurir Ekspres. "
                + "Nomor resi akan muncul di aplikasi dalam 1x24 jam. "
                + "Terima kasih telah berbelanja produk UMKM Sumatera Barat "
                + "di toko kami! Kami berharap produk yang Anda pesan "
                + "dapat diterima dengan baik dan memberikan kepuasan "
                + "dalam berbelanja.";

        CustomerContact siti = new CustomerContact(
                "Siti Rahma", "siti@example.com", "081234567890");

        CustomerContact budi = new CustomerContact(
                "Budi", "", "+6281298765432");

        int sentToSiti = notificationService.broadcast(siti, message);
        System.out.println("Terkirim ke Siti lewat "
                + sentToSiti + " kanal");

        int sentToBudi = notificationService.broadcast(budi, message);
        System.out.println("Terkirim ke Budi lewat "
                + sentToBudi + " kanal");
    }
}
