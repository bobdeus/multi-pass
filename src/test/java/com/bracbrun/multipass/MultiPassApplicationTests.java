package com.bracbrun.multipass;

import com.bracbrun.multipass.services.ProcessingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class MultiPassApplicationTests {

    @Autowired
    ProcessingService processingService;

    @Test
    void testThreadingSynced() {
        List<List<Integer>> testValue = Arrays.stream(IntStream.rangeClosed(1, 20).toArray()).boxed()
                .map(value -> Arrays.stream(IntStream.rangeClosed(1, 20).toArray()).boxed().toList()).toList();
        int resultSynced = processingService.syncedComputeValue(testValue);
        Assertions.assertEquals(4200, resultSynced);
    }

    @Test
    void testThreadingNonSynced() {
        List<List<Integer>> testValue = Arrays.stream(IntStream.rangeClosed(1, 20).toArray()).boxed()
                .map(value -> Arrays.stream(IntStream.rangeClosed(1, 20).toArray()).boxed().toList()).toList();
        int resultNonSynced = processingService.nonSyncedComputeValue(testValue);
        Assertions.assertTrue(resultNonSynced <= 4200);
    }

}
