
package shop.notification;

public class SmsChannel extends AbstractChannel {
    private int maxLength;

    public SmsChannel(int maxLength) {
        if (maxLength < 3) {
            throw new IllegalArgumentException("maxLength minimal 3");
        }

        this.maxLength = maxLength;
    }

    @Override
    public String channelName() {
        return "SMS";
    }

    @Override
    protected String resolveRecipient(CustomerContact contact) {
        String phone = contact.getPhone();
        if (phone == null || !phone.startsWith("08")) {
            return null;
        }
        return phone;
    }

    @Override
    protected String prepare(String message) {
        String prepared = message.trim();
        if (prepared.length() > maxLength) {
            prepared = prepared.substring(0, maxLength - 3) + "...";
        }
        return prepared;
    }

    @Override
    protected boolean deliver(String recipient, String message) {
        System.out.println("[SMS] To: " + recipient);
        System.out.println("Message: " + message);
        return true;
    }
}
