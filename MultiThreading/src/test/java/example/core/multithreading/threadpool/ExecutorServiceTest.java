package example.core.multithreading.threadpool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static example.core.multithreading.ThreadUtils.log;
import static example.core.multithreading.ThreadUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExecutorServiceTest {

    /**
     *
     * Executor Interface:
     * The Executor interface has a single execute method to submit Runnable instances for execution.
     *
     */
    @Test
    void executorDemo(){

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute( () -> { log("Hello World"); });
    }

    /**
     *
     * ExecutorService Interface:
     * It contains large number of methods to control progress of the task,
     * get future result from asynchronous task/threads , manage termination of service
     *
     */
    @Test
    void executorServiceDemo() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // execute( Runnable )
        executorService.execute( () -> { log("Hello World 1"); });

        // submit( Runnable )
        executorService.submit( () -> { log( "Hello World 2" ); });

        // submit( Callable ) : Returning Value
        Future<String> future = executorService.submit( () -> { sleep(1000); return "Future Result"; });
        log( future.get() );

        // shutdown() : Wait for threads to complete execution. New threads cannot be submitted after shutdown()
        executorService.shutdown();

        // Forcefully terminate threads and shutdown service, after waiting for 2 seconds.
        if( ! executorService.awaitTermination(2, TimeUnit.SECONDS) )
                executorService.shutdownNow();


    }

}
