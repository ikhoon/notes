package playground;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ikhoon on 2018-09-30.
 */
public class BinaryTreeTest {

    @Test
    public void testInsert() {
        BinaryTree<Integer> binaryTree = new BinaryTree<>();
        BinaryTree<Integer> tree = binaryTree.insert(10).insert(4).insert(20);
        System.out.println(tree);
    }
}