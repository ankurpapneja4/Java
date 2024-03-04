package example.core.multithreading.concepts;

import example.core.multithreading.threadmanipulation.ThreadList;
import org.junit.jupiter.api.Test;

import static example.core.multithreading.threadmanipulation.ThreadUtils.sleep;

/**
 *
 * volatile variable:
 *
 * - Unlike regular variables, which might be cached locally by threads,
 *   a volatile variable is always read from the main memory.
 *
 * - It ensures that multiple threads see the same value for the variable.
 *
 */
public class VolatileTest {

    @Test
    void volatileVariableTest() throws InterruptedException {


        // Listener Threads
        new ThreadList( new Counter()::updateListener ).start();

        // Update Thread - Update Shared Resource 5 Times
        Thread updateThread = new Thread( () -> {
                Counter counter = new Counter();
                for( int i = 0; i < 5; i++) {
                    counter.updatedSharedValue();
                    sleep(100);
                }
        });

        updateThread.start();
        updateThread.join();

    }

    private class Counter {

        private static volatile int sharedValue = 0;

        public void updatedSharedValue(){
            ++sharedValue;
        }

        public void updateListener(){
            int cachedValue = sharedValue;

            while(true)
                if (cachedValue != sharedValue) {

                    System.out.println("Shared Value Updated : " + sharedValue);
                    cachedValue = sharedValue; // Updated Cached Value
                }
        }

    }
}
