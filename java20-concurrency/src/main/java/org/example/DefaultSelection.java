package org.example;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultSelection {

    private static void doForSelect(BlockingQueue<Integer> signal, int caseId, Runnable task) {
        try {
            signal.put(caseId);

            task.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void select(BlockingQueue<Integer> signal, AtomicBoolean run, Runnable defaultRunnable, Runnable... tasks) {
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            executorService.submit(() -> {
                while (run.get()) {

                    switch (signal.poll()) {
                        case null -> defaultRunnable.run();
                        case 0 -> tasks[0].run();
                        case 1 -> tasks[1].run();
                        default -> throw new IllegalStateException("Unexpected value from signal");
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        BlockingQueue<Integer> tick = new ArrayBlockingQueue<>(1);
        BlockingQueue<Integer> boom = new ArrayBlockingQueue<>(1);
        BlockingQueue<Integer> signal = new ArrayBlockingQueue<>(1);

        final var f0 = scheduler.scheduleAtFixedRate(() -> doForSelect(signal, 0, () -> {
            try {
                tick.put(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }), 100, 100, TimeUnit.MILLISECONDS);

        final var f1 = scheduler.schedule(() -> doForSelect(signal, 1, () -> {
            try {
                boom.put(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }), 500, TimeUnit.MILLISECONDS);


        AtomicBoolean run = new AtomicBoolean(true);

        Runnable defaultRunnable = () -> {
            System.out.println("    .");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable runnTick = () -> {
            try {
                tick.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("tick.");
        };

        Runnable runnBoom = () -> {
            try {
                boom.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("BOOM!");
            run.set(false);
            f0.cancel(true);
            f1.cancel(true);
            scheduler.close();
        };

        select(signal, run, defaultRunnable, runnTick, runnBoom);
    }
}
