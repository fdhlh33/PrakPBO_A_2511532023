package shop.model;

import java.util.ArrayList;
import java.util.List;

/** Pesanan. Versi awal: dibuat dengan constructor lima parameter. */
public class Order {
    private final String orderId;
    private final Address address;
    private final List<Line> lines;
    private final String note;
    private final boolean giftWrap;
    private final boolean express;

    private Order(Builder b) {
        this.orderId = b.orderId;
        this.address = b.address;
        this.lines = List.copyOf(b.lines);
        this.note = b.note;
        this.giftWrap = b.giftWrap;
        this.express = b.express;
    }

    public String getOrderId() {
        return orderId;
    }

    public Address getAddress() {
        return address;
    }

    public List<Line> getLines() {
        return lines;
    }

    public String getNote() {
        return note;
    }

    public boolean isGiftWrap() {
        return giftWrap;
    }

    public boolean isExpress() {
        return express;
    }

    /** Satu baris pesanan: produk dan jumlahnya. */
    public record Line(Product product, int quantity) {
    }

    public static class Builder {
        private final String orderId;
        private final Address address;
        private final List<Line> lines = new ArrayList<>();
        private String note = "";
        private boolean giftWrap = false;
        private boolean express = false;

        public Builder(String orderId, Address address) {
            this.orderId = orderId;
            this.address = address;
        }

        public Builder addLine(Product product, int quantity) {
            lines.add(new Line(product, quantity));
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public Builder giftWrap() {
            this.giftWrap = true;
            return this;
        }

        public Builder express() {
            this.express = true;
            return this;
        }

        public Order build() {
            if (lines.isEmpty()) {
                throw new IllegalStateException("Pesanan harus berisi minimal satu item");
            }
            return new Order(this);
        }
    }
}