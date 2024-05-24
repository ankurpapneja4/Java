package example.core.multithreading.parallelalgorithm;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

class MergeSortTest {

    private final int PROCESSORS = Runtime.getRuntime().availableProcessors() - 1;

    private final ThreadPoolExecutor executor
            = new ThreadPoolExecutor( PROCESSORS, PROCESSORS,0L, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());

    @Test
    void sortTest() {

        int[] arr = sampleArray( 300_000_000 );


        long beginTime = System.currentTimeMillis();
        new MergeSort( arr, executor ).parallelSort();
        long endTime = System.currentTimeMillis();

        System.out.println( endTime - beginTime );

    }

    public int[] sampleArray( int length ){
        Random random = new Random();

        int[] arr = new int[ length ];
        IntStream.range( 0, length ).forEach( i -> { arr[i] = random.nextInt( length );  });

        return  arr;
    }
}