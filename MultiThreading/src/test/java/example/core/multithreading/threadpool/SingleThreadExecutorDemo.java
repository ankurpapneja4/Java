package example.core.multithreading.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleThreadExecutorDemo {


    /**
     *
     * SingleThreadPoolExecutor:
     * corePollSize = 1, maxPoolSize = 1, keepAliveTime = 0, queue = LinkedBlockingQueue
     *
     * new ThreadPoolExecutor(1, 1,
     *                          0L, TimeUnit.MILLISECONDS,
     *                            new LinkedBlockingQueue<Runnable>())
     *
     */
    @Test
    void singleThreadExecutorDemo() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        final int[] arr = { 0, 0, 0};
        executorService.submit( () -> { arr[0] = 5; });
        executorService.submit( () -> { arr[1] = arr[0] + 1; });
        executorService.submit( () -> { arr[2] = arr[1] + 2; });


        /**
         *
         * SingleThreadExecutor:
         * Submitted task are executed sequentially
         *
         */
        assertEquals( 5, arr[0] );
        assertEquals( 6, arr[1] );
        assertEquals( 8, arr[2] );

    }


}
