package org.example;

public class Goroutines {
    private static void say(String s) {
        for (var i = 0; i < 5; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore
            }
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        Thread.startVirtualThread(() -> say("world"));
        say("hello");
    }
}
