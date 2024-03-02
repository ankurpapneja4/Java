package example.core.multithreading.threadmanipulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPriorityTest {

    @Test
    void priorityTest() throws InterruptedException, BrokenBarrierException, TimeoutException {


        final int CPU_CORES = Runtime.getRuntime().availableProcessors();

        List<Thread> lowPriorityThreads = new ArrayList<>(CPU_CORES);
        List<Thread> highPriorityThreads = new ArrayList<>(CPU_CORES);

        CountDownLatch latch = new CountDownLatch( CPU_CORES );
        AtomicInteger counter = new AtomicInteger(0);

        // Create Low Priority Threads
        for(int i =0; i< CPU_CORES; i++)
                lowPriorityThreads.add( new Thread( () -> {

                        doSomeComplexTask();
                        counter.incrementAndGet(); // Increment Counter When A Low Priority Thread Is Completed

                }, "LowPriority"+i));

        // Create High Priority Threads
        for(int i =0; i< CPU_CORES; i++)
                highPriorityThreads.add( new Thread(() -> {

                    try {  doSomeComplexTask(); }
                    finally { latch.countDown(); /* Signal High Priority Thread Completed*/ }

                }, "HighPriority"+i ));

        // When: Set Priority ( Between 0-MIN And 10-MAX )
        highPriorityThreads.forEach( t -> t.setPriority( Thread.MAX_PRIORITY ));


        // Start Threads
        lowPriorityThreads.stream().forEach( Thread::start );
        highPriorityThreads.stream().forEach( Thread::start );


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
