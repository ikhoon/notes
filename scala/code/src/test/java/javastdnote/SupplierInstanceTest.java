package javastdnote;

import static org.junit.Assert.*;

import java.util.function.Supplier;

import org.junit.Test;

public class SupplierInstanceTest {

    @Test
    public void test() {
        Supplier<SupplierInstance> supplierInstanceSupplier1 = SupplierInstance::get;
        Supplier<SupplierInstance> supplierInstanceSupplier2 = SupplierInstance::get;
    }
}