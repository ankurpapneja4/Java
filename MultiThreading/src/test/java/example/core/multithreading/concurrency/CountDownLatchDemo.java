package example.core.multithreading.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static example.core.multithreading.ThreadUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CountDownLatch
 *
 * A single thread can wait for all other threads to perform some task and countdown.
 *
 */
public class CountDownLatchDemo {


    /**
     *
     * Example to demonstrate, main thread waits for 8 threads to complete execution.
     *
     */
    @Test
    void countdownLatchDemo() throws InterruptedException {

        int threadsCount = 8;
        int[] result = new int[ threadsCount ];

        CountDownLatch countDownLatch = new CountDownLatch( threadsCount );

        IntStream.range( 0, threadsCount ).forEach( i -> {
            new Thread( () -> {
                try { sleep(1000); result[i] = 1; }
                finally {
                    // count down.
                    countDownLatch.countDown();
                }
            }).start();
        });

        // Wait until, countdown becomes 0
        countDownLatch.await();


        int expectedSum = threadsCount;
        assertEquals( threadsCount, getSum( result ));
    }

    private int getSum( int ... values){
        int sum = 0;
        for( int v : values) sum += v;

        return sum;
    }

}
