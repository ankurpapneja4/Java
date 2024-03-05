package example.core.multithreading.concurrency;

import example.core.multithreading.ThreadList;
import example.core.multithreading.ThreadUtils;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;

import static example.core.multithreading.ThreadUtils.log;
import static example.core.multithreading.ThreadUtils.sleep;

/**
 *
 * Semaphore:
 *
 * It is a record of how many units of a particular resource are available.
 * We have to wait until a unit of the resource becomes available again
 *
 */
public class SemaphoreTest {

    @Test
    void concurrencyTest() {

        FileProcessor fileProcessor = new FileProcessor();
        ThreadList threads1 = new ThreadList( fileProcessor::processFile, "FileWorker-Batch1-").start();
        ThreadList threads2 = new ThreadList( fileProcessor::processFile, "FileWorker-Batch2-").start();
        ThreadList threads3 = new ThreadList( fileProcessor::processFile, "FileWorker-Batch3-").start();

        threads1.join();
        threads2.join();
        threads3.join();
    }



    /**
     *
     * Use case:
     *
     * Limit, max 3 files can be processed concurrently
     *
     */
    private class FileProcessor {

        private Semaphore semaphore = new Semaphore(3);

        public void processFile(){

            try {

                // Acquire Permit
                // Wait, If no permit available.
                semaphore.acquire();

                log( "Acquired 1 Permit. Available Permits : " + semaphore.availablePermits() );
                log( "Number Of Threads Waiting : " + semaphore.getQueueLength() );

                // Do Some Task
                tokenize();

            }
            catch( InterruptedException e){ e.printStackTrace();}

            finally {

                // Release Permit
                semaphore.release();

            }
        }

        private void tokenize(){

            sleep( 1000 );
            log( "Preparing Index " );

        }

    }
}
