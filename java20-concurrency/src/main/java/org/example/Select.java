package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Select {
    private static void fibonacci(BlockingQueue<Integer> c, BlockingQueue<Integer> quit, BlockingQueue<Integer> signal) {
        final AtomicBoolean run = new AtomicBoolean(true);
        final AtomicInteger x = new AtomicInteger(0);
        final AtomicInteger y = new AtomicInteger(1);

        final Runnable runFib = () -> {
            try {
                c.put(x.get());

                int xx = x.get();
                x.set(y.get());
                y.set(y.get() + xx);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        final Runnable runQuit = () -> {
            run.set(false);
            System.out.println("quit");
        };

        select(signal, run, runFib, runQuit);
    }

    private static void select(BlockingQueue<Integer> signal, AtomicBoolean run, Runnable... tasks) {
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            executorService.submit(() -> {
                while (run.get()) {
                    try {
                        switch (signal.take()) {
                            case 0 -> tasks[0].run();
                            case 1 -> tasks[1].run();
                            default -> throw new IllegalStateException("Unexpected value from signal");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private static void doForSelect(BlockingQueue<Integer> signal, int caseId, Runnable task) {
        try {
            signal.put(caseId);

            task.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> c = new ArrayBlockingQueue<>(1);
        BlockingQueue<Integer> quit = new ArrayBlockingQueue<>(1);
        BlockingQueue<Integer> signal = new ArrayBlockingQueue<>(1);

        Thread.startVirtualThread(() -> {
            for (int i = 0; i < 10; i++) {
                doForSelect(signal, 0, () -> {
                    try {
                        System.out.println(c.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }

            doForSelect(signal, 1, () -> {
                try {
                    quit.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        fibonacci(c, quit, signal);
    }
}
