
package shop.model;

/** Pelanggan dengan Builder Pattern. */
public class Customer {
    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String memberLevel;

    public static class Builder {
        private final String name;
        private final String phone;

        private String email = "";
        private String address = "";
        private String memberLevel = "REGULAR";

        public Builder(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder address(String address) {
            this.address = address;
            return this;
        }
        public Builder memberLevel(String memberLevel) {
            this.memberLevel = memberLevel;
            return this;
        }
        public Customer build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException(
                        "Nama pelanggan wajib diisi");
            }
            if (phone == null || !phone.startsWith("08")) {
                throw new IllegalStateException(
                        "Nomor HP harus diawali 08");
            }
            return new Customer(this);
        }
    }

    private Customer(Builder b) {
        this.name = b.name;
        this.phone = b.phone;
        this.email = b.email;
        this.address = b.address;
        this.memberLevel = b.memberLevel;
    }

    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public String getAddress() {
        return address;
    }
    public String getMemberLevel() {
        return memberLevel;
    }
}