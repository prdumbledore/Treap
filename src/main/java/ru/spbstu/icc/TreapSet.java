package ru.spbstu.icc;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class TreapSet<T extends Comparable<T>> extends Treap<T> {

    final T first;
    final T last;
    Treap<T> treap;

    TreapSet(T first, T last, Treap<T> treap) {
        this.first = first;
        this.last = last;
        this.treap = treap;
    }

    private boolean inside(T key) {
        return (first != null && last != null &&
                key.compareTo(first) >= 0 && key.compareTo(last) < 0) ||
                (first == null && key.compareTo(last) < 0) ||
                (last == null && key.compareTo(first) >= 0);
    }

    @Override
    public int size() {
        if (treap == null) return 0;
        int size = 0;
        for (T key : treap) {
            if (inside(key)) size++;
        }
        return size;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        return inside(t) && treap.contains(t);
    }

    @Override
    public boolean add(T key) {
        if (!inside(key)) throw new IllegalArgumentException();
        return treap.add(key);
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        T key = (T) o;
        if (!inside(key)) throw new IllegalArgumentException();
        return treap.remove(key);
    }

    @Override
    public T first() {
        for (T key : treap)
            if (inside(key)) return key;
        throw new NoSuchElementException();
    }

    @Override
    public T last() {
        T result = null;
        for (T key : treap)
            if (inside(key)) result = key;
        if (result == null) throw new NoSuchElementException();
        return result;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SubTreapIterator();
    }

    public class SubTreapIterator implements Iterator<T> {

        private final Stack<Node<T>> stack = new Stack<>();
        Node<T> currentNode = null;

        private void pushNodes(Node<T> node) {
            if (node.left != null) pushNodes(node.left);
            if (inside(node.key)) stack.push(node);
            if (node.right != null) pushNodes(node.right);
        }

        private SubTreapIterator() {
            if (treap.getRoot() != null) {
                pushNodes(treap.getRoot());
                currentNode = stack.peek();
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node<T> node = stack.pop();
            currentNode = node;
            return node.key;
        }

        @Override
        public void remove() {
            if (currentNode == null) throw new NoSuchElementException();
            treap.remove(currentNode.key);
            currentNode = null;
        }
    }
}
