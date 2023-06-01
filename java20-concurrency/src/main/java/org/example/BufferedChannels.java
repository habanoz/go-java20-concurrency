package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BufferedChannels {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> ch = new ArrayBlockingQueue<>(2);

        ch.put(1);
        ch.put(2);

        System.out.println(ch.take());
        System.out.println(ch.take());
    }
}
