
package review.fixed;

import java.util.ArrayList;
import java.util.List;

interface PaymentMethod { boolean pay(long amount); }
interface Refundable { boolean refund(long amount); }
interface Shippable { long ship(ShippingType type, int kilograms); }
interface InvoicePrintable { String printInvoice(); }

class GiftVoucher implements PaymentMethod, InvoicePrintable {
    private long balance = 100_000L;

    @Override
    public boolean pay(long amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    @Override
    public String printInvoice() {
        return "sisa Rp" + balance;
    }
}

enum ShippingType {
    REGULAR,
    EXPRESS,
    SAME_DAY
}

abstract class Courier implements Shippable {

    @Override
    public final long ship(ShippingType type, int kilograms) {
        if (type == null || kilograms <= 0) {
            throw new IllegalArgumentException(
                    "Jenis layanan dan berat harus valid");
        }

        return kilograms * ratePerKilogram(type);
    }

    protected abstract long ratePerKilogram(ShippingType type);
}

class JneCourier extends Courier {

    @Override
    protected long ratePerKilogram(ShippingType type) {
        switch (type) {
            case REGULAR:
                return 10_000L;
            case EXPRESS:
                return 18_000L;
            case SAME_DAY:
                return 30_000L;
            default:
                throw new IllegalArgumentException(
                        "Jenis layanan tidak dikenal");
        }
    }
}

class Cart {
    private final List<Item> items = new ArrayList<>();
    static class Item {
        private final String name;
        private final long price;
        private final int quantity;

        Item(String name, long price, int quantity) {
            if (name == null || name.isBlank()
                    || price < 0 || quantity <= 0) {
                throw new IllegalArgumentException(
                        "Data item tidak valid");
            }
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
        long subtotal() {
            return price * quantity;
        }
        String getName() {
            return name;
        }
    }

    void add(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Item tidak boleh null");
        }
        items.add(item);
    }
    long total() {
        long total = 0;
        for (Item item : items) {
            total += item.subtotal();
        }
        return total;
    }
}

final class Address {
    private final String street;
    private final String city;

    Address(String street, String city) {
        if (street == null || street.isBlank()
                || city == null || city.isBlank()) {
            throw new IllegalArgumentException(
                    "Alamat tidak boleh kosong");
        }
        this.street = street;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return street + ", " + city;
    }
}

class SimpleOrder {
    private final String id;
    private final Address address;

    SimpleOrder(String id, Address address) {
        if (id == null || id.isBlank() || address == null) {
            throw new IllegalArgumentException(
                    "Data pesanan tidak valid");
        }
        this.id = id;
        this.address = address;
    }

    Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return id + " -> " + address;
    }
}

interface Discount {
    long apply(long price);
}

class MemberDiscount implements Discount {
    @Override
    public long apply(long price) {
        return price - price * 5 / 100;
    }
}

class FlashSaleDiscount implements Discount {
    @Override
    public long apply(long price) {
        return price - price * 30 / 100;
    }
}

public class BadShopRefactored {
    public static void main(String[] args) {
        Cart cart = new Cart();
        cart.add(new Cart.Item("Kopi Gayo", 85_000, 2));
        System.out.println("Total cart: " + cart.total());

        Courier courier = new JneCourier();
        System.out.println(
                "Ongkir Ekspres 2 kg: "
                        + courier.ship(ShippingType.EXPRESS, 2));

        Address home = new Address(
                "Jl. Sudirman 10",
                "Padang");

        Address otherAddress = new Address(
                "Jl. Sudirman 10",
                "Bukittinggi");

        SimpleOrder first = new SimpleOrder("ORD-1", home);
        SimpleOrder second = new SimpleOrder(
                "ORD-2", otherAddress);

        System.out.println(first);
        System.out.println(second);

        Discount flashSale = new FlashSaleDiscount();

        System.out.println(
                "Harga flash sale: "
                        + flashSale.apply(100_000));
        GiftVoucher voucher = new GiftVoucher();

        boolean paymentResult = voucher.pay(30_000);
        String invoice = voucher.printInvoice();

        System.out.println(
                paymentResult + " | " + invoice);
    }
}