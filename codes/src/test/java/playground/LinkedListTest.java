package playground;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ikhoon on 2018-09-30.
 */
public class LinkedListTest {

    @Test
    public void testAdd() {
        LinkedList<Integer> ints = LinkedList.empty();
        LinkedList<Integer> inserted = ints.insert(10);
        assertEquals(new Cons<Integer>(10, LinkedList.empty()), inserted);
    }

    @Test
    public void testRemove() {
        LinkedList<Integer> ints = LinkedList.empty();
        LinkedList<Integer> inserted = ints.insert(10).insert(20).insert(30);
        LinkedList<Integer> removed = inserted.remove(20);
        LinkedList<Integer> expected = new Cons<>(30, new Cons<>(10, LinkedList.empty()));
        assertEquals(expected, removed);

        StringBuilder builder = new StringBuilder();
        builder.append("hello");
        builder.append(10);
        System.out.println(builder.toString());
    }
}