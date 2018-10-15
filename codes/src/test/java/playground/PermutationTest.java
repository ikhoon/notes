package playground;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ikhoon on 2018-10-02.
 */
public class PermutationTest {

    // abc
    // acb
    // bac
    // bca
    // cab
    // cba

    @Test
    public void testPermutation() {
        Permutation permutation = new Permutation();
        List<String> abc = permutation.permute("abc");
        ArrayList<String> strings = new ArrayList<>();
        strings.add("abc");
        strings.add("acb");
        strings.add("bac");
        strings.add("bca");
        strings.add("cab");
        strings.add("cba");
        assertEquals(strings, abc);

    }
}