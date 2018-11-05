package playground;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Created by ikhoon on 2018-09-30.
 */



public abstract class LinkedList<T> {
    public LinkedList<T> insert(T element) {
        return new Cons<>(element, this);
    }
    public abstract LinkedList<T> remove(T element);
    public abstract T head();
    public abstract LinkedList<T> tail();

    private static final LinkedList EMPTY_LIST = new Nil<>();

    @SuppressWarnings("unchecked")
    public static <T> LinkedList<T> empty() {
        return (LinkedList<T>) EMPTY_LIST;
    }
}

class Cons<T> extends LinkedList<T> {
    T element;
    LinkedList<T> next;

    Cons(T element, LinkedList<T> next) {
        this.element = element;
        this.next = next;
    }

    public LinkedList<T> remove(T element) {
        if (element == null) {
            return this;
        }
        if(element.equals(this.element)) {
            return next;
        } else {
            this.next = next.remove(element);
            return this;
        }
    }

    public String toString() {
        return "Cons(" + element + "," + next + ")";
    }
    public boolean equals(Object other) {
        if(!(other instanceof Cons)) {
           return false;
        } else {
            Cons<T> cons = (Cons<T>) other;
            return Objects.equals(element, cons.element)
                && Objects.equals(next, cons.next);
        }
    }

    public T head() {
        return element;
    }
    public LinkedList<T> tail() {
        return next;
    }

}

class Nil<T> extends LinkedList<T> {

    public LinkedList<T> remove(T element) {
        return this;
    }


    public T head() {
        throw new NoSuchElementException();
    }
    public LinkedList<T> tail() {
        throw new NoSuchElementException();
    }
    public String toString() {
        return "Nil";
    }
    public boolean equals(Object other) {
        return other instanceof Nil;
    }
}

