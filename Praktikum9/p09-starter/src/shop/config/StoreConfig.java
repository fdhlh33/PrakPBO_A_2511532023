package shop.config;

public class StoreConfig {
    private static StoreConfig instance;

    private String storeName = "Toko Kopi Andalas";
    private long shippingPerKg = 8_000;
    private long giftWrapFee = 5_000;

    private StoreConfig() {
    }
    public static StoreConfig getInstance() {
        if (instance == null) {
            instance = new StoreConfig();
        }
        return instance;
    }

    public String getStoreName() {
        return storeName;
    }
    public long getShippingPerKg() {
        return shippingPerKg;
    }
    public long getGiftWrapFee() {
        return giftWrapFee;
    }
    public void setShippingPerKg(long value) {
        shippingPerKg = value;
    }
}