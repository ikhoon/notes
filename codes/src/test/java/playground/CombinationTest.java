package playground;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ikhoon on 2018-10-02.
 */
public class CombinationTest {

    @Test
    public void testCombination() {
        Combination combination = new Combination();
        List<String> abc = combination.combination("abc", 2);
        System.out.println(abc);
    }

}