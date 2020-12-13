package ru.spbstu.icc;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Treap<T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {

    private Node<T> root = null;
    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    public Node<T> getRoot() {
        return root;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private Node<T> find(T key) {
        if (root == null) return null;
        return find(root, key);
    }

    private Node<T> find(Node<T> start, T key) {

        int comparison = key.compareTo(start.key);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, key);
         }
        else {
            if (start.right == null) return start;
            return find(start.right, key);
        }

    }

    @Override
    public boolean contains(Object o) {

        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.key) == 0;

    }

    private Node<T> merge(Node<T> first, Node<T> second) {

        if (first == null) return second;
        if (second == null) return first;

        if (first.priority > second.priority) {
            first.right = merge(first.right, second);
            first.right.parent = first;
            return first;
        } else  {
            second.left = merge(first, second.left);
            second.left.parent = second;
            return second;
        }
    }

    private Pair<Node<T>, Node<T>> split(Node<T> start, T key) {

        if (start == null) return new Pair<>(null, null);

        if (key.compareTo(start.key) > 0) {
            Pair<Node<T>, Node<T>> result = split(start.right, key);
            start.right = result.getFirst();

            if (start.right != null) start.right.parent = start;
            if (result.getSecond() != null) result.getSecond().parent = null;
            return new Pair<>(start, result.getSecond());
        } else {
            Pair<Node<T>, Node<T>> result = split(start.left, key);
            start.left = result.getSecond();

            if (start.left != null) start.left.parent = start;
            if (result.getFirst() != null) result.getFirst().parent = null;
            return new Pair<>(result.getFirst(), start );

        }
    }

    @Override
    public boolean add(T key) {

        Node<T> closest = find(key);

        if (closest != null && closest.key.equals(key)) return false;

        Pair<Node<T>, Node<T>> treaps = split(root, key);
        Node<T> merge = merge(treaps.getFirst(), new Node<>(key));
        root = merge(merge, treaps.getSecond());

        size++;
        root.parent = null;

        return true;
    }

    @Override
    public boolean remove(Object o) {

        @SuppressWarnings("unchecked")
        T key = (T) o;
        Node<T> closest = find(key);

        if (closest != null && closest.key.equals(key))
            return removeN(closest);

        return false;
    }

    public boolean removeN(Node<T> closest) {

        Node<T> parent = closest.parent;

        if (parent != null) {
            if (parent.left != null && parent.left.key.equals(closest.key)) {
                parent.left = merge(closest.left, closest.right);
                if (parent.left != null) parent.left.parent = parent;
            } else {
                parent.right = merge(closest.left, closest.right);
                if (parent.right != null) parent.right.parent = parent;
            }
        } else {
            root = merge(closest.left, closest.right);
            if (root != null)
                root.parent = null;
        }

        size--;
        return true;
    }

    @Override
    public Comparator<? super T> comparator() {
        return Comparable::compareTo;
    }

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {

        if (fromElement == toElement && toElement == null)
            throw new IllegalArgumentException();
        if (toElement != null && fromElement.compareTo(toElement) >=0)
            throw new IllegalArgumentException();

        return new SubTreap<>(fromElement, toElement, this);
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        if (toElement == null) throw new IllegalArgumentException();
        return new SubTreap<>(null, toElement, this);
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        if (fromElement == null) throw new IllegalArgumentException();
        return new SubTreap<>(fromElement, null, this);
    }

    @Override
    public T first() {

        if (root == null) throw new NoSuchElementException();

        Node<T> node = root;
        while (node.left != null) node = node.left;

        return node.key;
    }

    @Override
    public T last() {

        if (root == null) throw new NoSuchElementException();

        Node<T> node = root;
        while (node.right != null) node = node.right;

        return node.key;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o: c)
            if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T t: c)
            if (!add(t)) return false;
        return true;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        if (this.containsAll(c)) {
            for (Object t : this) {
                if (!c.contains(t)) remove(t);
            }
            return true;
        } else return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        if (this.containsAll(c)) {
            for (Object t: this) remove(t);
            return true;
        } else return false;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.root = null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new TreapIterator();
    }

    public class TreapIterator implements Iterator<T> {

        private final Stack<Node<T>> stack = new Stack<>();
        Node<T> currentNode = null;

        private void pushNodes(Node<T> node) {
            if (node != null) {
                stack.push(node);
                pushNodes(node.left);
            }
        }

        private TreapIterator() {
            pushNodes(root);
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
            pushNodes(node.right);

            return node.key;
        }

        @Override
        public void remove() {
            if (currentNode == null) throw new NoSuchElementException();
            removeN(currentNode);
            currentNode = null;
        }
    }
}
