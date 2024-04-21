package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class SimpleThreadPool {

    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final List<Thread> threads;
    private volatile boolean isShutdown;

    public SimpleThreadPool(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        threads = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            Thread thread = new Thread(this::processTask);
            threads.add(thread);
            thread.start();
        }
    }

    private void processTask() {
        while (!isShutdown) {
            Runnable task = null;
            synchronized (taskQueue) {
                while (taskQueue.isEmpty() && !isShutdown) {
                    try {
                        taskQueue.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (!taskQueue.isEmpty()) {
                    task = taskQueue.poll();
                }
            }
            if (nonNull(task)) {
                task.run();
            }
        }
    }

    public void execute(Runnable task) {
        requireNonNull(task, "Task must not be null");
        if (isShutdown) {
            throw new IllegalStateException("Pool is shutting down");
        }
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }
    }

    public void shutdown() {
        isShutdown = true;
        synchronized (taskQueue) {
            taskQueue.clear();
            taskQueue.notifyAll();
        }
    }

    public void awaitTermination() {
        for (Thread worker : threads) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
