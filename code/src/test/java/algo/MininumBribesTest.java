package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class MininumBribesTest {


    @Test
    public void test2() {
        int[] arr = {2, 1, 5, 3, 4};
        int i = MininumBribes.minimumBribes0(arr);
        assertEquals(3, i);
        MininumBribes.minimumBribes(arr);

    }

    @Test
    public void test3() {
        int[] arr = {2,5,1,3,4};
        int i = MininumBribes.minimumBribes0(arr);
        assertEquals(-1, i);
    }


    @Test
    public void test4() {
        int[] arr = {1,2,5,3,7,8,6,4};
        int i = MininumBribes.minimumBribes0(arr);
        assertEquals(7, i);

    }
}