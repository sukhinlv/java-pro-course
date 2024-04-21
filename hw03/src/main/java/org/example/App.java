package org.example;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class App {

    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool pool = new SimpleThreadPool(3);
        AtomicInteger counter = new AtomicInteger(0);
        final Random rnd = new Random();
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < countDownLatch.getCount(); i++) {
            pool.execute(() -> {
                try {
                    int taskNumber = counter.incrementAndGet();
                    long waitSeconds = rnd.nextInt(5);
                    System.out.println("Started task %d for %d seconds".formatted(taskNumber, waitSeconds));
                    LockSupport.parkNanos(waitSeconds * 1_000_000_000);
                    System.out.println("Finished task %d".formatted(taskNumber));
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        pool.shutdown();
        pool.awaitTermination();
    }
}
