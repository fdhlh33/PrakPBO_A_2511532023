
package shop.notification;

public class EmailChannel extends AbstractChannel {

    @Override
    public String channelName() {
        return "Email";
    }

    @Override
    protected String resolveRecipient(CustomerContact contact) {
        String email = contact.getEmail();
        if (email == null || !email.contains("@")) {
            return null;
        }
        return email;
    }

    @Override
    protected boolean deliver(String recipient, String message) {
        System.out.println("[EMAIL] To: " + recipient);
        System.out.println("Message: " + message);
        return true;
    }
}