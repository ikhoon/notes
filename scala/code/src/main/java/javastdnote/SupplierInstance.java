package javastdnote;

public class SupplierInstance {

    static SupplierInstance INSTANCE = new SupplierInstance();

    public static SupplierInstance get() {
        return INSTANCE;
    }
}
