package shop.notify;

public class SmsChannel implements NotificationChannel {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[SMS] " + recipient + ": " + message);
    }
}