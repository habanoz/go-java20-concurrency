package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RangeAndClose {
    private static void fibonacci(int n, BlockingQueue<Object> c) {
        int x = 0;
        int y = 1;

        for (int i = 0; i < n; i++) {
            try {
                c.put(x);
            } catch (InterruptedException e) {
                //ignore
            }
            int xx = x;
            x = y;
            y = xx + y;
        }

        try {
            c.put("Done");
        } catch (InterruptedException e) {
            //ignore
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Object> c = new ArrayBlockingQueue<>(10);
        Thread.startVirtualThread(() -> fibonacci(c.remainingCapacity(), c));

        while (c.take() instanceof Integer i) {
            System.out.println(i);
        }
    }
}
