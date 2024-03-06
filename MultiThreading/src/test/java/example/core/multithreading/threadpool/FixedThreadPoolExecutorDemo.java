package example.core.multithreading.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static example.core.multithreading.ThreadUtils.log;
import static example.core.multithreading.ThreadUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FixedThreadPoolExecutorDemo {

    /**
     *
     * FixedThreadPool:
     *
     * new ThreadPoolExecutor( nThreads, nThreads,
     *                              0L, TimeUnit.MILLISECONDS,
     *                                  new LinkedBlockingQueue<Runnable>());
     */
    @Test
    void fixedThreadPoolExecutorDemo() {

        ExecutorService executorService = Executors.newFixedThreadPool( 2 );

        ThreadPoolExecutor pool = (ThreadPoolExecutor) executorService;

        /**
         *
         * corePollSize 2, maxPoolSize=2, keepAliveTime = 0, queue = LinkedBlockingQueue
         *
         */
        assertEquals( 2, pool.getCorePoolSize()    );
        assertEquals( 2, pool.getMaximumPoolSize() );
        assertEquals( 0, pool.getKeepAliveTime(TimeUnit.MILLISECONDS ));

        // Submit Threads
        executorService.submit( () -> { sleep(200); log("Hello");  });
        executorService.submit( () -> { sleep(200); log("Hello");  });
        executorService.submit( () -> { sleep(200); log("Hello");  });
        executorService.submit( () -> { sleep(200); log("Hello");  });
        executorService.submit( () -> { sleep(200); log("Hello");  });

        assertEquals( 2, pool.getPoolSize()     );
        assertEquals( 3, pool.getQueue().size() );

        sleep( 1000 );


    }
}
