package example.core.multithreading.concurrency;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static example.core.multithreading.ThreadUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 *
 * CyclicBarrier
 *
 * Multiple threads can wait, until barrier is broken.
 *
 */
public class CyclicBarrierDemo {

    @Test
    void cyclicBarrierDemo() {

        int[] result = new int[ 3 ];

        CyclicBarrier barrier = new CyclicBarrier( 3, () -> { log( Arrays.toString(result) ); });

        /**
         *
         * Sum of results
         *
         */
        new Thread( () -> {
            try
            {
                sleepRandom();
                result[ 0 ] = 2;

                // Wait for result from other threads to become available
                barrier.await();

                // Process Result [ 2, 3, 4]
                assertEquals( 9, sum( result ) );
            }
            catch (InterruptedException | BrokenBarrierException e) { throw new RuntimeException(e); }
        }).start();

        /**
         *
         * Product of results
         *
         */
        new Thread( () -> {
            try
            {
                sleepRandom();
                result[ 1 ] = 3;

                // Wait for result from other threads to become available
                barrier.await();

                // Process Result [ 2, 3, 4]
                assertEquals( 24, product( result ) );
            }
            catch (InterruptedException | BrokenBarrierException e) { throw new RuntimeException(e); }
        }).start();

        /**
         *
         * Average of results
         *
         */
        new Thread( () -> {
            try
            {
                sleepRandom();
                result[ 2 ] = 4;

                // Wait for result from other threads to become available
                barrier.await();

                // Process Result [ 2, 3, 4]
                assertEquals( "3.00", average( result ) );
            }
            catch (InterruptedException | BrokenBarrierException e) { throw new RuntimeException(e); }
        }).start();

        sleep( 2000 );
   }




    private int sum( int ... values){ int sum = 0;   for( int v : values) sum += v;   return sum;}

    private int product( int ... values){ int prod = 1;   for( int v : values) prod *= v;   return prod; }

    private String average( int ... values){ int sum = 0;    for( double v : values) sum += v;    return  String.format( "%.2f", (double)sum / values.length);  }



}
