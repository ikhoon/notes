package playground;

/**
 * Created by ikhoon on 2018-09-30.
 */

class Node<T> {
    public Node(T elem, Node<T> left, Node<T> right) {
        this.elem = elem;
        this.left = left;
        this.right = right;
    }

    private T elem;
    private Node<T> left;
    private Node<T> right;

    public T getElem() {  return elem; }
    public Node<T> getLeft() { return left; }
    public Node<T> getRight() { return right; }

    public void setLeft(Node<T> left) {
       this.left = left;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }
    public String toString() {
        if(this == BinaryTree.Nil)
            return "Nil";
        else
            return "Node(" + elem + ", left=" + left + ", right=" + right + ")";
    }
}



public class BinaryTree<T extends Comparable<T>> {
    private Node<T> root;

    static final Node Nil = new Node<>(null, null, null);

    public BinaryTree() {
        this.root = Nil;
    }

    public BinaryTree<T> insert(T elem) {
        if(root == Nil) {
            root = new Node<T>(elem, Nil, Nil);
            return this;
        } else {
            insert0(root, elem);
            return this;
        }
    }

    private BinaryTree<T> insert0(Node<T> node, T elem) {
        if(elem.compareTo(node.getElem()) < 0) {
            if(node.getLeft() == Nil) {
                node.setLeft(new Node<T>(elem, Nil, Nil));
                return this;
            } else {
                return insert0(node.getLeft(), elem);
            }
        }
        else {
            if(node.getRight() == Nil) {
                node.setRight(new Node<T>(elem, Nil, Nil));
                return this;
            } else {
                return insert0(node.getRight(), elem);
            }
        }
    }

    public String toString() {
        return "Tree(" + root.toString() + ")";
    }
}
