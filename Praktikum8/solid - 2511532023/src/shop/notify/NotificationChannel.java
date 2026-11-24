package shop.notify;

public interface NotificationChannel {
    void send(String recipient, String message);
}