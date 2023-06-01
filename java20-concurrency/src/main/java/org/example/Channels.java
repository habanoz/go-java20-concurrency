package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class Channels {
    private static int sum(List<Integer> s) {
        int sum = 0;
        for (int v : s) {
            sum += v;
        }
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var numbers = Arrays.asList(7, 2, 8, -9, 4, 0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var f1 = executor.submit(() -> sum(numbers.subList(0, numbers.size() / 2)));
            var f2 = executor.submit(() -> sum(numbers.subList(numbers.size() / 2, numbers.size())));
            int x = f1.get();
            int y = f2.get();

            System.out.printf("%d %d %d \n", x, y, x + y);
        }
    }
}
