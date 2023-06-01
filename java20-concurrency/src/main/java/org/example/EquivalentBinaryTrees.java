package org.example;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Tree {
    protected Tree left;
    protected final int value;
    protected Tree right;

    Tree(int Value) {
        value = Value;
    }

    public static Tree New(int i) {
        var values = IntStream.rangeClosed(1, 10).map(n -> i * n).boxed().collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(values);

        Tree root = new Tree(values.pop());

        for (int value : values) {
            insert(root, value);
        }

        return root;
    }

    public static void insert(Tree node, int value) {
        if (node.value > value) {
            if (node.left == null) {
                node.left = new Tree(value);
            } else {
                insert(node.left, value);
            }
        } else {
            if (node.right == null) {
                node.right = new Tree(value);
            } else {
                insert(node.right, value);
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        buildString(this, builder);
        return builder.toString();
    }

    private void buildString(Tree node, StringBuilder builder) {
        if (node == null) return;

        builder.append("(");
        buildString(node.left, builder);
        builder.append(node.value);
        buildString(node.right, builder);
        builder.append(")");
    }
}

public class EquivalentBinaryTrees {
    private static void walk(Tree t, BlockingQueue<Integer> ch) {
        if (t.left != null) {
            walk(t.left, ch);
        }

        try {
            ch.put(t.value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (t.right != null) {
            walk(t.right, ch);
        }
    }

    private static boolean same(Tree t1, Tree t2) throws InterruptedException {
        BlockingQueue<Integer> c1 = new ArrayBlockingQueue<>(1);
        BlockingQueue<Integer> c2 = new ArrayBlockingQueue<>(1);

        Thread.startVirtualThread(() -> walk(t1, c1));
        Thread.startVirtualThread(() -> walk(t2, c2));

        for (int i = 0; i < 10; i++) {
            Integer take = c1.take();
            Integer take1 = c2.take();
            if (!take.equals(take1)){
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Tree.New(1));
        System.out.println(Tree.New(1));
        System.out.println(Tree.New(2));

        System.out.println(same(Tree.New(1), Tree.New(1)));
        System.out.println(same(Tree.New(1), Tree.New(2)));

    }
}
