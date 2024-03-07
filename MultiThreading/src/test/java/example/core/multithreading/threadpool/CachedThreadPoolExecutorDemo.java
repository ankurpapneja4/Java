package example.core.multithreading.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static example.core.multithreading.ThreadUtils.log;
import static example.core.multithreading.ThreadUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CachedThreadPoolExecutorDemo {


    /**
     *
     * CachedThreadPool:
     *
     * new ThreadPoolExecutor(0, Integer.MAX_VALUE,
     *                              60L, TimeUnit.SECONDS,
     *                                  new SynchronousQueue<Runnable>())
     */
    @Test
    void cachedThreadPoolExecutorDemo() throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        ThreadPoolExecutor pool = (ThreadPoolExecutor) executorService;

        /**
         *
         * corePollSize 0, maxPoolSize=Integer.MAX_VALUE, keepAliveTime = 60Sec, queue = SynchronousQueue
         *
         */
        assertEquals( 0, pool.getCorePoolSize()    );
        assertEquals( Integer.MAX_VALUE, pool.getMaximumPoolSize() );
        assertEquals( 60L, pool.getKeepAliveTime(TimeUnit.SECONDS ));

        // Submit Threads
        executorService.submit( () -> { sleep(200); log("Hello");  });
        executorService.submit( () -> { sleep(200); log("Hello");  });
        executorService.submit( () -> { sleep(200); log("Hello");  });
        executorService.submit( () -> { sleep(200); log("Hello");  });
        executorService.submit( () -> { sleep(200); log("Hello");  });

        assertEquals( 5, pool.getPoolSize()     );
        assertEquals( 0, pool.getQueue().size() );

        sleep( 2000); // Sleep 59
        /**
         *
         * Pool size is not shrunk after thread completion
         *
         */
        assertEquals( 5, pool.getPoolSize()     );
        assertEquals( 0, pool.getQueue().size() );

        sleep( 60000);
        /**
         *
         * Pool size shrunk after being ideal for keepAliveTime
         *
         */
        assertEquals( 0, pool.getPoolSize()     );
        assertEquals( 0, pool.getQueue().size() );



    }
}
