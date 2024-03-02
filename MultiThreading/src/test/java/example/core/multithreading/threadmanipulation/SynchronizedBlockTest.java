package example.core.multithreading.threadmanipulation;

import org.junit.jupiter.api.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static example.core.multithreading.threadmanipulation.ThreadUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SynchronizedBlockTest {

    @Test
    void synchronizedBlock_LockingWithCustomObjectDemo(){

        Synchronized obj = new Synchronized( ThreadList.defaultSize() * 2 );

        ThreadList threads1 =new ThreadList( () -> {
            for(int i =0; i<100; i++) obj.updateSharedResource1(); }, "Resource1_Worker").start();


        ThreadList threads2  =new ThreadList( () -> {
            for(int i =0; i<100; i++) obj.updateSharedResource2(); }, "Resource2_Worker").start();

        threads1.join();
        threads2.join();

        int idealResourceValueAfterUpdate = ThreadList.defaultSize() * 100;

        // Then : Value is CONSISTENT
        assertEquals( idealResourceValueAfterUpdate, obj.getSharedResource1() );
        assertEquals( idealResourceValueAfterUpdate, obj.getSharedResource2() );

    }

    private static class Synchronized {

        private final int parallelizationLevel;
        private final CyclicBarrier barrier;
        private final AtomicInteger parallelThreadCounter;


        public final Object lock1 = new Object();
        public final Object lock2 = new Object();

        private int sharedResource1 = 0;
        private int sharedResource2 = 0;


        private Synchronized(int parallelizationLevel){

            this.parallelizationLevel = parallelizationLevel;
            this.barrier = new CyclicBarrier( this.parallelizationLevel );
            this.parallelThreadCounter = new AtomicInteger( 0 );
        }

        public void updateSharedResource1() {

            doSomeIndependentTask1();

            synchronized ( lock1 )
            {
                int readValue = sharedResource1;
                sleep(10);
                sharedResource1 = readValue + 1;
            }
            doSomeIndependentTask2();
        }

        public void updateSharedResource2() {

            doSomeIndependentTask1();

            synchronized (lock2)
            {
                int readValue = sharedResource2;
                sleep(10);
                sharedResource2 = readValue + 1;
            }

            doSomeIndependentTask2();
        }

        public int getSharedResource1() { return sharedResource1;}

        public int getSharedResource2() { return sharedResource2; }

        public void doSomeIndependentTask2() {

            // All Threads Can Enter This Block In Parallel
            System.out.println("Current Thread " + Thread.currentThread().getName());

            parallelThreadCounter.incrementAndGet();

            try { barrier.await(); } catch (InterruptedException | BrokenBarrierException e) { throw new RuntimeException(e); }


            if( parallelThreadCounter.get() == parallelizationLevel )
            {
                barrier.reset();
                parallelThreadCounter.set(0);
                System.out.println("---------------------");
            }
        }

        public void doSomeIndependentTask1() {
             // All Threads Can Enter This Block And Undergo Sleep Time In Parallel
            sleep(200);
        }



    }
}
