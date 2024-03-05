package example.core.multithreading.concepts;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static example.core.multithreading.ThreadUtils.sleep;

/**
 * Livelock:
 *
 *   However, Threads are not blocked, but they cannot make further progress,
 *   because they become too busy responding to each other.
 *
 */
public class LivelockDemo {

    @Test
    void givenCyclicDependency_whenTwoThreadsDoesNotAcquireLockInSameOrder_thenLivelockOccur() throws InterruptedException {

        CyclicDependency cyclicDependency = new CyclicDependency();

        Thread thread1 = new Thread( () -> {
            try { cyclicDependency.increment();}
            catch (InterruptedException e){ e.printStackTrace(); }

        }, "IncrementWorker");


        Thread thread2 = new Thread( () -> {
            try { cyclicDependency.decrement();}
            catch (InterruptedException e){ e.printStackTrace(); }

        }, "DecrementWorker");

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


        public void increment() throws InterruptedException {

            while(true) {
                lock1.lock();
                System.out.println("Lock 1 Acquired : " + Thread.currentThread().getName());
                sleep(10);

                if( lock2.tryLock(10, TimeUnit.MILLISECONDS) ) break;

                else {
                    sleep(10);
                    lock1.unlock();
                    System.out.println("Lock 1 Released : " + Thread.currentThread().getName());
                }
            }

            System.out.println( "Lock 1 and 2 Acquired : " + Thread.currentThread().getName());

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


        public void decrement() throws InterruptedException {

            while(true) {
                lock2.lock();
                System.out.println("Lock 2 Acquired : " + Thread.currentThread().getName());
                sleep(10);

                if( lock1.tryLock(10, TimeUnit.MILLISECONDS) ) break;
                else {
                    sleep(10);
                    lock2.unlock();
                    System.out.println("Lock 2 Released : " + Thread.currentThread().getName());
                }
            }

            System.out.println( "Lock 2 And 1 Acquired : " + Thread.currentThread().getName());
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
