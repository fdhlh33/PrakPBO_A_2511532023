package shop.model;

/** Produk toko: nama, harga (rupiah), dan berat (gram). */
public record Product(String name, long price, int weightGram) { }
