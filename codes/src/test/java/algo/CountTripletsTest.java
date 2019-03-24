package algo;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CountTripletsTest {

    @Test
    public void test1() {
        List<Long> integers = Arrays.asList(1L, 2L, 2L, 4L);
        int r = 2;

        long countTriplets = CountTriplets.countTriplets(integers, r);
        assertEquals(2, countTriplets);
    }


    @Test
    public void test2() {
        List<Long> xs = Arrays.asList(1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L,1L);
        int r = 1;
        long l = CountTriplets.countTriplets(xs, r);
        assertEquals(161700, l);

    }

}