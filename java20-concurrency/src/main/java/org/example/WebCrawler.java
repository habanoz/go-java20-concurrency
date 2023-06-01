package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

record Result(String body, String[] urls) {
}

class SafeUrlCounter {
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


public class WebCrawler {

    private static Result fetch(Map<String, Result> fetcher, String url) {
        if (fetcher.containsKey(url))
            return new Result(fetcher.get(url).body(), fetcher.get(url).urls());
        return null;
    }

    private static void crawl(String url, int depth, Map<String, Result> fetcher, SafeUrlCounter c) {
        if (depth <= 0) {
            return;
        }

        if (c.value(url) > 0) {
            return;
        }

        Result result = fetch(fetcher, url);
        c.inc(url);

        if (result == null) {
            System.out.println("not found: " + url);
            return;
        }

        System.out.printf("found: %s %s\n", url, result.body());
        for (String u : result.urls()) {
            Thread.startVirtualThread(() -> crawl(u, depth - 1, fetcher, c));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SafeUrlCounter c = new SafeUrlCounter();
        Thread.startVirtualThread(()->crawl("https://golang.org/", 4, fetcher, c));
        Thread.sleep(5000);
        System.out.println("Done");
    }

    private static final Map<String, Result> fetcher = Map.of(
            "https://golang.org/", new Result("The Go Programming Language",
                    new String[]{"https://golang.org/",
                            "https://golang.org/cmd/",
                            "https://golang.org/pkg/fmt/",
                            "https://golang.org/pkg/os/",}),
            "https://golang.org/pkg/", new Result("Packages",
                    new String[]{"https://golang.org/",
                            "https://golang.org/cmd/",
                            "https://golang.org/pkg/fmt/",
                            "https://golang.org/pkg/os/",}),
            "https://golang.org/pkg/fmt/", new Result("Package fmt",
                    new String[]{"https://golang.org/",
                            "https://golang.org/pkg/"}),

            "https://golang.org/pkg/os/", new Result("Package os",
                    new String[]{"https://golang.org/",
                            "https://golang.org/pkg/",})
    );
}
