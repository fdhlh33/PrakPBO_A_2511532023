package shop.service;

public record Bill(long subtotal, long discount, long shipping, long total) {
}