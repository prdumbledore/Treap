package ru.spbstu.icc;

import java.util.Random;

public class Node<T extends Comparable<T>> {

    public final T key;
    public Node<T> left, right;
    public Node<T> parent = null;
    private static final Random random = new Random();
    public final int priority;

    public Node(T key) {
        this.key = key;
        priority = random.nextInt();
    }

}
