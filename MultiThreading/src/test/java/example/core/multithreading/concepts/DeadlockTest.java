package example.core.multithreading.concepts;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static example.core.multithreading.ThreadUtils.sleep;

/**
 *  Deadlock:
 *
 *  Deadlock occurs when two or more threads wait forever for a lock
 *  or resource, that is held by the other thread.
 *
 */
public class DeadlockTest {

    @Test
    void givenCyclicDependency_whenTwoThreadsDoesNotAcquireLockInSameOrder_thenDeadlockOccur() throws InterruptedException {

        CyclicDependency cyclicDependency = new CyclicDependency();
        Thread thread1 = new Thread(cyclicDependency::increment, "IncrementWorker");
        Thread thread2 = new Thread(cyclicDependency::decrement, "DecrementWorker");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }


    private class CyclicDependency {

        private final Lock lock1 = new ReentrantLock();
        private final Lock lock2 = new ReentrantLock();

        private int sharedResource1 = 0;
        private int sharedResource2 = 0;


        public void increment() {

            lock1.lock();
            System.out.println( "Lock 1 Acquired : " + Thread.currentThread().getName());
            sleep(200);
            lock2.lock();
            System.out.println( "Lock 2 Acquired : " + Thread.currentThread().getName());

            try {
                ++sharedResource1;
                ++sharedResource2;
            }
            finally {
                lock2.unlock();
                System.out.println( "Lock 2 Released : " + Thread.currentThread().getName());
                lock1.unlock();
                System.out.println( "Lock 2 Released : " + Thread.currentThread().getName());
            }
        }


        public void decrement() {

            lock2.lock();
            System.out.println( "Lock 2 Acquired : " + Thread.currentThread().getName());
            sleep(200);
            lock1.lock();
            System.out.println( "Lock 1 Acquired : " + Thread.currentThread().getName());
            try {
                --sharedResource1;
                --sharedResource2;
            }
            finally {
                lock1.unlock();
                System.out.println( "Lock 1 Released : " + Thread.currentThread().getName());
                lock2.unlock();
                System.out.println( "Lock 2 Released : " + Thread.currentThread().getName());

            }
        }

    }

}
