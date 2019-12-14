package algo;

import algo.AddTwoNumbers.ListNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddTwoNumbersTest {


    @Test
    public void test1() {
        AddTwoNumbers addTwoNumbers = new AddTwoNumbers();
        ListNode l1 = new ListNode(2);
        l1.next =  new ListNode(4);
        l1.next.next =  new ListNode(3);

        ListNode l2 = new ListNode(5);
        l2.next =  new ListNode(6);
        l2.next.next =  new ListNode(4);

        ListNode listNode = addTwoNumbers.addTwoNumbers(l1, l2);
        System.out.println("listNode = " + listNode);
    }
}