package algo;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReverseShuttleMergeTest {

    @Test
    public void testReverseShuffleMerge() {
        String eggegg = ReverseShuttleMerge.reverseShuffleMerge("eggegg");
        assertEquals("egg", eggegg);
    }
    @Test
    public void testShuffle() {
        System.out.println(ReverseShuttleMerge.shuffle("god"));
    }
}