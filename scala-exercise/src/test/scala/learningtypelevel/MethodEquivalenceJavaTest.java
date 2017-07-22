package learningtypelevel;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ikhoon on 2016. 8. 29..
 */
public class MethodEquivalenceJavaTest {
    @Test
    public void testEquivalent() {

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);

        MethodEquivalenceJava.copyToZeroE(list1);

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        list2.add(4);
        list2.add(1);

        Assert.assertEquals(list1, list2);
    }

}