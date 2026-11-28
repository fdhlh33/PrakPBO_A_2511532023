package check;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Pemeriksa otomatis LKP P14 (Singleton dan Builder). JANGAN DIUBAH. */
public final class SelfCheck {
    private static int passed = 0;
    private static int total = 0;
    private static String output = "";

    private SelfCheck() { }

    public static void main(String[] args) {
        long perKg = NimParams.newShippingPerKg();
        System.out.println("SelfCheck LKP P14 - Singleton dan Builder");
        System.out.println("NIM: " + NimParams.nim() + " | ongkir admin: " + rp(perKg) + "/kg");
        System.out.println();

        // Persiapan
        boolean ran = runShopApp();
        check("P1", "ShopApp berjalan tanpa error", ran, "Perbaiki error saat ShopApp dijalankan.");
        check("P2", "MyNim berisi NIM yang valid", NimParams.isValid(), "Isi NIM di src/shop/app/MyNim.java.");

        // Bagian B: Singleton StoreConfig
        check("B1", "StoreConfig: constructor private", allConstructorsPrivate("shop.config.StoreConfig"),
                "Ubah constructor StoreConfig menjadi private.");
        check("B2", "StoreConfig.getInstance() selalu mengembalikan objek yang sama",
                sameInstance("shop.config.StoreConfig"), "Tambahkan static getInstance() (resep tiga bahan).");
        check("B3", "OrderCalculator dan InvoicePrinter tidak lagi memakai new StoreConfig()",
                sourceLacks("src/shop/service/OrderCalculator.java", "new StoreConfig(")
                        && sourceLacks("src/shop/service/InvoicePrinter.java", "new StoreConfig("),
                "Ganti new StoreConfig() dengan StoreConfig.getInstance().");
        check("B4", "Invoice memakai ongkir yang diset admin",
                ran && countOf(output, "x " + rp(perKg) + "/kg") >= 4,
                "Ongkir di invoice harus " + rp(perKg) + "/kg.");
        long a1 = 85_000 + perKg * 2;
        long a2 = 195_000 + perKg + 5_000;
        long a3 = 200_000 + perKg * 2;
        check("B5", "Total pesanan A1, A2, A3 benar",
                ran && output.contains("TOTAL       : " + rp(a1))
                        && output.contains("TOTAL       : " + rp(a2))
                        && output.contains("TOTAL       : " + rp(a3)),
                "Total yang benar: A1 " + rp(a1) + ", A2 " + rp(a2) + ", A3 " + rp(a3) + ".");

        // Bagian C: Builder Order
        check("C1", "Order: tidak ada constructor public", noPublicConstructor("shop.model.Order"),
                "Constructor Order harus private Order(Builder b).");
        check("C2", "Order.Builder punya addLine, note, giftWrap, express, build",
                hasMethods("shop.model.Order$Builder", "addLine", "note", "giftWrap", "express", "build"),
                "Tambahkan class Builder di dalam Order (resep lima bahan).");
        check("C3", "Semua field Order bersifat final", allFieldsFinal("shop.model.Order"),
                "Tambahkan final pada setiap field Order.");
        check("C4", "Order.Builder.build() menolak pesanan tanpa item", orderBuildRejectsEmpty(),
                "Di build(), lempar IllegalStateException bila belum ada item.");

        // Bagian D: Singleton OrderNumberGenerator
        check("D1", "OrderNumberGenerator: constructor private",
                allConstructorsPrivate("shop.service.OrderNumberGenerator"),
                "Ubah constructor OrderNumberGenerator menjadi private.");
        check("D2", "OrderNumberGenerator.getInstance() selalu mengembalikan objek yang sama",
                sameInstance("shop.service.OrderNumberGenerator"), "Tambahkan static getInstance().");
        check("D3", "Nomor invoice tidak ada yang ganda", ran && invoiceNumbersUnique(),
                "Pastikan ShopApp dan ReorderService memakai generator yang sama.");

        // Bagian E: Builder Customer
        check("E1", "Customer: tidak ada constructor public", noPublicConstructor("shop.model.Customer"),
                "Constructor Customer harus private Customer(Builder b).");
        check("E2", "Customer.Builder punya email, address, memberLevel, build",
                hasMethods("shop.model.Customer$Builder", "email", "address", "memberLevel", "build"),
                "Tambahkan class Builder di dalam Customer.");
        check("E3", "Semua field Customer bersifat final", allFieldsFinal("shop.model.Customer"),
                "Tambahkan final pada setiap field Customer.");
        check("E4", "build() menolak nama kosong", customerRejects("", "081234567890", "Nama pelanggan wajib diisi"),
                "Lempar IllegalStateException(\"Nama pelanggan wajib diisi\").");
        check("E5", "build() menolak nomor HP yang tidak diawali 08",
                customerRejects("Rina", "12345", "Nomor HP harus diawali 08"),
                "Lempar IllegalStateException(\"Nomor HP harus diawali 08\").");
        check("E6", "memberLevel bawaan adalah REGULAR", customerDefaultLevel(),
                "Beri nilai bawaan \"REGULAR\" pada memberLevel di Builder.");

        System.out.println();
        System.out.println("Skor: " + passed + "/" + total);
        boolean voucher = findClass("shop.model.Voucher") != null && findClass("shop.model.Voucher$Builder") != null;
        System.out.println("Tantangan Voucher: " + (voucher ? "[TERDETEKSI]" : "[belum]"));
    }

    // ---------- alat bantu ----------

    private static void check(String code, String name, boolean ok, String hint) {
        total++;
        if (ok) {
            passed++;
            System.out.println("[LULUS] " + code + " " + name);
        } else {
            System.out.println("[BELUM] " + code + " " + name + "  -> " + hint);
        }
    }

    private static boolean runShopApp() {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            Class<?> app = Class.forName("shop.app.ShopApp");
            Method main = app.getMethod("main", String[].class);
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            main.invoke(null, (Object) new String[0]);
            return true;
        } catch (ReflectiveOperationException | RuntimeException e) {
            return false;
        } finally {
            System.setOut(original);
            output = buffer.toString(StandardCharsets.UTF_8);
        }
    }

    private static Class<?> findClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static boolean allConstructorsPrivate(String className) {
        Class<?> c = findClass(className);
        if (c == null) {
            return false;
        }
        for (Constructor<?> k : c.getDeclaredConstructors()) {
            if (!Modifier.isPrivate(k.getModifiers())) {
                return false;
            }
        }
        return true;
    }

    private static boolean noPublicConstructor(String className) {
        Class<?> c = findClass(className);
        if (c == null) {
            return false;
        }
        for (Constructor<?> k : c.getDeclaredConstructors()) {
            if (Modifier.isPublic(k.getModifiers())) {
                return false;
            }
        }
        return true;
    }

    private static boolean sameInstance(String className) {
        Class<?> c = findClass(className);
        if (c == null) {
            return false;
        }
        try {
            Method m = c.getMethod("getInstance");
            if (!Modifier.isStatic(m.getModifiers())) {
                return false;
            }
            Object a = m.invoke(null);
            Object b = m.invoke(null);
            return a != null && a == b;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    private static boolean hasMethods(String className, String... names) {
        Class<?> c = findClass(className);
        if (c == null) {
            return false;
        }
        Set<String> found = new HashSet<>();
        for (Method m : c.getMethods()) {
            found.add(m.getName());
        }
        for (String n : names) {
            if (!found.contains(n)) {
                return false;
            }
        }
        return true;
    }

    private static boolean allFieldsFinal(String className) {
        Class<?> c = findClass(className);
        if (c == null) {
            return false;
        }
        boolean any = false;
        for (Field f : c.getDeclaredFields()) {
            if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic()) {
                continue;
            }
            any = true;
            if (!Modifier.isFinal(f.getModifiers())) {
                return false;
            }
        }
        return any;
    }

    private static boolean orderBuildRejectsEmpty() {
        Class<?> builder = findClass("shop.model.Order$Builder");
        Class<?> address = findClass("shop.model.Address");
        if (builder == null || address == null) {
            return false;
        }
        try {
            Object addr = address.getConstructor(String.class, String.class).newInstance("Tes", "Padang");
            Object b = builder.getConstructor(String.class, address).newInstance("X1", addr);
            builder.getMethod("build").invoke(b);
            return false;
        } catch (InvocationTargetException e) {
            return e.getCause() instanceof IllegalStateException;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    private static Object newCustomerBuilder(String name, String phone) throws ReflectiveOperationException {
        Class<?> builder = findClass("shop.model.Customer$Builder");
        if (builder == null) {
            throw new ClassNotFoundException("shop.model.Customer$Builder");
        }
        return builder.getConstructor(String.class, String.class).newInstance(name, phone);
    }

    private static boolean customerRejects(String name, String phone, String message) {
        try {
            Object b = newCustomerBuilder(name, phone);
            b.getClass().getMethod("build").invoke(b);
            return false;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            return cause instanceof IllegalStateException && message.equals(cause.getMessage());
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    private static boolean customerDefaultLevel() {
        try {
            Object b = newCustomerBuilder("Tes", "081200000000");
            Object customer = b.getClass().getMethod("build").invoke(b);
            Object level = customer.getClass().getMethod("getMemberLevel").invoke(customer);
            return "REGULAR".equals(level);
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    private static boolean sourceLacks(String path, String text) {
        try {
            return !Files.readString(Path.of(path)).contains(text);
        } catch (java.io.IOException e) {
            System.out.println("        (tidak dapat membaca " + path + "; jalankan SelfCheck dari folder proyek)");
            return false;
        }
    }

    private static boolean invoiceNumbersUnique() {
        Matcher m = Pattern.compile("Invoice\\s+: (INV-\\d+)").matcher(output);
        List<String> numbers = new ArrayList<>();
        while (m.find()) {
            numbers.add(m.group(1));
        }
        return numbers.size() >= 4 && new HashSet<>(numbers).size() == numbers.size();
    }

    private static int countOf(String text, String part) {
        int count = 0;
        int i = text.indexOf(part);
        while (i >= 0) {
            count++;
            i = text.indexOf(part, i + part.length());
        }
        return count;
    }

    private static String rp(long amount) {
        return "Rp" + String.format("%,d", amount).replace(',', '.');
    }
}
