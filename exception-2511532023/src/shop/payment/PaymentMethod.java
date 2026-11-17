package shop.payment;

import shop.exception.PaymentFailedException;

public interface PaymentMethod {
    String getName();

    void pay(long amount) throws PaymentFailedException;
}
