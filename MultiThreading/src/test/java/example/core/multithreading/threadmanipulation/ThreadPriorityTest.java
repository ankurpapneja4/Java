package example.core.multithreading.threadmanipulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPriorityTest {

    @Test
    void priorityTest() throws InterruptedException, BrokenBarrierException, TimeoutException {

        CountDownLatch latch = new CountDownLatch( ThreadList.defaultSize() );
        AtomicInteger counter = new AtomicInteger(0);


        // Create Low Priority Threads
        new ThreadList( () -> {

            doSomeComplexTask();
            counter.incrementAndGet(); // Increment Counter When A Low Priority Thread Is Completed

        }, "LowPriority").start();


        // When: Create High Priority Threads With Priority = 10 ( Max )
        new ThreadList( () -> {

                    try {  doSomeComplexTask(); }
                    finally { latch.countDown(); /* Signal High Priority Thread Completed*/ }

        }, "HighPriority" ).priority( Thread.MAX_PRIORITY).start();


        // Wait For All High Priority Threads To Complete
        latch.await();

        // Get Number Of Low Priority Threads Completed, During Time Taken By High Priority Threads To Complete
        int numberOfLowPrioryThreadsCompleted = counter.get();

        // Then
        Assertions.assertEquals( 0, numberOfLowPrioryThreadsCompleted);
    }

    public static void doSomeComplexTask(){
        long start = System.currentTimeMillis();

            PowerCalculator.calculate( new BigInteger("4435347"), new BigInteger("92539"));

        long executionTime = ( System.currentTimeMillis() - start );

        System.out.println( String.format( "Thread: %s    Priority: %s    Exec Time: %d",
                Thread.currentThread().getName(),
                Thread.currentThread().getPriority(),
                executionTime
        ));

    }

}
