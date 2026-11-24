package shop.payment;

import shop.exception.PaymentFailedException;

public class QrisPayment implements PaymentMethod {
    @Override
    public String name() { return "QRIS"; }
    @Override
    public void pay(long amount) throws PaymentFailedException {
        if (amount > 10_000_000) {
            throw new PaymentFailedException("transaksi QRIS maksimal Rp10.000.000");
        }
    }
}