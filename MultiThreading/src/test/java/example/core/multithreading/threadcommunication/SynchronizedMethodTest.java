package example.core.multithreading.threadcommunication;

import example.core.multithreading.ThreadList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static example.core.multithreading.ThreadUtils.sleep;

public class SynchronizedMethodTest {

    @Test
    void whenNotSynchronized_thenCounterIsInconsistent(){

        NonSynchronized obj = new NonSynchronized();

        // When : increment() is Not-Synchronized
        int threadCount = new ThreadList( () -> { for(int i = 0; i<100; i++) obj.increment(); })
                                .start()
                                .join()
                                .size();

        int idealCounterValueAfterIncrement = threadCount * 100;

        // Then : Value is INCONSISTENT
        Assertions.assertNotEquals( idealCounterValueAfterIncrement, obj.getCounter() );

    }

    @Test
    void whenSynchronized_thenCounterIsConsistent(){

        Synchronized obj = new Synchronized();

        // When : increment() is Synchronized
        int threadCount = new ThreadList( () -> { for(int i =0; i<100; i++) obj.increment(); })
                .start()
                .join()
                .size();

        int idealCounterValueAfterIncrement = threadCount * 100;

        // Then : Value is CONSISTENT
        Assertions.assertEquals( idealCounterValueAfterIncrement, obj.getCounter() );

    }


    private static class NonSynchronized{

        private int counter = 0;

        public void increment() {
            int save = counter;
            sleep(10);
            counter = save + 1;
        }

        public int getCounter() { return counter;}
    }

    private static class Synchronized{

        private int counter = 0;

        // Only one thread can execute this method at a time.
        public synchronized void increment() {
            int save = counter;
            sleep(10);
            counter = save + 1;
        }

        public int getCounter() { return counter;}
    }
}
