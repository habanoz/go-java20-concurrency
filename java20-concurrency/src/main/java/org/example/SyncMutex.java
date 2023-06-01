package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;



public class SyncMutex {
    public static void main(String[] args) throws InterruptedException {
        var c = new SafeCounter();
        for (int i = 0; i <1000; i++) {
            Thread.startVirtualThread(()->c.inc("somekey"));
        }

        Thread.sleep(1000);
        System.out.println(c.value("somekey"));
    }
}

class SafeCounter {
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<String, Integer> v = new HashMap<>();

    public void inc(String key) {
        lock.lock();
        try {
            int value = Optional.ofNullable(v.get(key)).orElse(0);
            v.put(key, value + 1);
        } finally {
            lock.unlock();
        }
    }

    public int value(String key) {
        lock.lock();
        try {
            return Optional.ofNullable(v.get(key)).orElse(0);
        } finally {
            lock.unlock();
        }
    }
}