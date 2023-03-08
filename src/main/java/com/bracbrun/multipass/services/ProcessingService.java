package com.bracbrun.multipass.services;

import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessingService {

    private static int theSum = 0;

    private static void doWorkSynced(List<List<Integer>> allValues) {
        List<Thread> threads = new ArrayList<>();
        createThreadForEachRowAndStartSynced(allValues, threads);
        joinAllThreads(threads);
    }

    private static void doWorkNonSynced(List<List<Integer>> allValues) {
        List<Thread> threads = new ArrayList<>();
        createThreadForEachRowAndStartNonSynced(allValues, threads);
        joinAllThreads(threads);
    }

    private static void createThreadForEachRowAndStartSynced(List<List<Integer>> allValues, List<Thread> threads) {
        allValues.forEach(valueList -> {
            val threadName = String.format("Thread %s", threads.size());
            Runnable myRunnable = () -> computeTheSumOfValuesSynced(valueList);
            val thread = new Thread(myRunnable, String.format("Thread %s", threadName));
            threads.add(thread);
            thread.start();
        });
    }

    private static void createThreadForEachRowAndStartNonSynced(List<List<Integer>> allValues, List<Thread> threads) {
        allValues.forEach(valueList -> {
            val threadName = String.format("Thread %s", threads.size());
            Runnable myRunnable = () -> computeTheSumOfValuesNonSynced(valueList);
            val thread = new Thread(myRunnable, String.format("Thread %s", threadName));
            threads.add(thread);
            thread.start();
        });
    }

    private static void joinAllThreads(List<Thread> threads) {
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void computeTheSumOfValuesSynced(List<Integer> values) {
        synchronized (ProcessingService.class) {
            theSum = values.stream().reduce(theSum, (value, memo) -> {
                memo = value + memo;
                return memo;
            });
            System.out.println(Thread.currentThread().getName() + " is trying to compute the value. Which is now: " + theSum);
        }
    }

    private static void computeTheSumOfValuesNonSynced(List<Integer> values) {
        theSum = values.stream().reduce(theSum, (value, memo) -> {
            memo = value + memo;
            return memo;
        });
        System.out.println(Thread.currentThread().getName() + " is trying to compute the value. Which is now: " + theSum);
    }

    public int syncedComputeValue(List<List<Integer>> allValues) {
        theSum = 0;
        doWorkSynced(allValues);

        return theSum;
    }

    public int nonSyncedComputeValue(List<List<Integer>> allValues) {
        theSum = 0;
        doWorkNonSynced(allValues);

        return theSum;
    }
}
