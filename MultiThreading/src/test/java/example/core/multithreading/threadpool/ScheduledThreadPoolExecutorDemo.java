package example.core.multithreading.threadpool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static example.core.multithreading.ThreadUtils.log;
import static example.core.multithreading.ThreadUtils.sleep;

public class ScheduledThreadPoolExecutorDemo {

    private ScheduledExecutorService executorService;

    @BeforeEach
    void initialize()
    {
        int corePoolSize = 1;
        executorService = Executors.newScheduledThreadPool( corePoolSize );
    }

    /**
     *
     * Schedule a task for execution in 2 Seconds
     *
     */
    @Test
    void scheduleDemo(){


        long delaySeconds = 2;
        executorService.schedule( () -> { log("Hello"); },  delaySeconds, TimeUnit.SECONDS );


    }

    /**
     *
     * Schedule a task for repeated executions, at a fixed delay of every 500 milliseconds, after completing previous execution.
     *
     */
    @Test
    void scheduleWithFixedDelayDemo(){

        long initialDelay = 100;
        long delayMillis  = 500;
        executorService.scheduleWithFixedDelay( () -> { log("Hello"); }, initialDelay, delayMillis,  TimeUnit.MILLISECONDS );

        sleep( 2000 );

    }


    /**
     *
     * Schedule a task for repeated executions, at a fixed rate of every 500 milliseconds
     *
     */
    @Test
    void scheduleAtFixedRateDemo(){

        long initialDelay = 100;
        long period       = 500;
        executorService.scheduleAtFixedRate( () -> { log("Hello"); }, initialDelay, period,  TimeUnit.MILLISECONDS );

        sleep( 2000 );

    }

    /**
     *
     * Cancel a scheduled task after executing 4 times.
     *
     */
    @Test
    void cancelScheduledTaskDemo() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch( 4 );

        long initialDelay = 500;
        long period       = 100;
        ScheduledFuture<?> scheduledFuture =
                executorService.scheduleAtFixedRate( () -> {

                           try { log("Hello"); } finally {  latch.countDown(); }

                    }, initialDelay, period,  TimeUnit.MILLISECONDS );

        latch.await( 1L, TimeUnit.SECONDS );
        scheduledFuture.cancel( true );

        Assertions.assertTrue( scheduledFuture.isCancelled() );

    }




    @AfterEach
    void cleanUp() throws InterruptedException {

        // Shutdown
        executorService.shutdown();
        if( ! executorService.awaitTermination( 5l, TimeUnit.SECONDS ) )
            executorService.shutdownNow();

    }



}
