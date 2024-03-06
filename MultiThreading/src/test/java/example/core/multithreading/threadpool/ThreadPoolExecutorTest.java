package example.core.multithreading.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static example.core.multithreading.ThreadUtils.log;
import static example.core.multithreading.ThreadUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadPoolExecutorTest {

    @Test
    void threadPoolExecutorDemo(){

        // corePoolSize: parameter is the number of core threads that will be instantiated and kept in the pool.
        int  corePoolSize = 2;

        // maximumPoolSize:  if all core threads are busy and the internal queue is full, the pool is allowed to grow up to maximumPoolSize.
        int  maximumPoolSize = Integer.MAX_VALUE;

        // keepAliveTime: is the interval of time for which the excessive threads (instantiated in excess of the corePoolSize) are allowed to exist in the idle state.
        // By default, the ThreadPoolExecutor only considers non-core threads for removal.
        long keepAliveTime = 30; // Seconds

        // queueCapacity: Number of threads than can be queued. If queue capacity is full, pool is allowed to grow up-to maximumPoolSize
        int queueCapacity = 4;

        ExecutorService executorService = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>( 4 ) );


        ThreadPoolExecutor pool = (ThreadPoolExecutor)executorService;

        executorService.submit( () -> { sleep( 1000 ); log("Hello"); });
        assertEquals( 1, pool.getPoolSize()     );
        assertEquals( 0, pool.getQueue().size() );


        executorService.submit( () -> { sleep( 1000 ); log("Hello"); });
        assertEquals( 2, pool.getPoolSize()     );
        assertEquals( 0, pool.getQueue().size() );


        executorService.submit( () -> { sleep( 1000 ); log("Hello"); });
        /**
         *
         *  Pool size is equal to corePoolSize. New threads will be added to queue.
         *
         */
        assertEquals( 2, pool.getPoolSize()     );
        assertEquals( 1, pool.getQueue().size() );


        executorService.submit( () -> { sleep( 1000 ); log("Hello"); });
        assertEquals( 2, pool.getPoolSize()     );
        assertEquals( 2, pool.getQueue().size() );


        executorService.submit( () -> { sleep( 1000 ); log("Hello"); });
        assertEquals( 2, pool.getPoolSize()     );
        assertEquals( 3, pool.getQueue().size() );


        executorService.submit( () -> { sleep( 1000 ); log("Hello"); });
        assertEquals( 2, pool.getPoolSize()     );
        assertEquals( 4, pool.getQueue().size() );

        executorService.submit( () -> { sleep( 1000 ); log("Hello"); });
        /**
         *
         * Queue is full, pool is allowed to grow up-to maximumPoolSize
         *
         */
        assertEquals( 3, pool.getPoolSize()     );
        assertEquals( 4, pool.getQueue().size() );

        executorService.submit( () -> { sleep( 1000 ); log("Hello"); });
        assertEquals( 4, pool.getPoolSize()     );
        assertEquals( 4, pool.getQueue().size() );

        sleep( 3000);

        /**
         *
         * Queue is empty now. No further threads to execute
         *
         */
        assertEquals( 4, pool.getPoolSize()     );
        assertEquals( 0, pool.getQueue().size() );


        sleep( 30000); // keepAliveTime = 30 Sec
        /**
         *
         * Pool shrank to corePoolSize after being ideal for keepAliveTime
         *
         */
        assertEquals( 2, pool.getPoolSize()     );
        assertEquals( 0, pool.getQueue().size() );

    }

}
