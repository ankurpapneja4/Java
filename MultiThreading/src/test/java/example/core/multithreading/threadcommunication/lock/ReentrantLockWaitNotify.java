package example.core.multithreading.threadcommunication.lock;

import example.core.multithreading.threadcommunication.WaitNotifiyTest;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static example.core.multithreading.ThreadUtils.sleep;

public class ReentrantLockWaitNotify {

    @Test
    void blockingQueueTest(){

        BlockingQueueImpl<Integer> queue = new BlockingQueueImpl<>( 5 );

        AtomicInteger i = new AtomicInteger(0);

        // Producers
        new Thread( () -> { while(true) queue.add(  i.incrementAndGet() ); }, "Producer1").start();
        new Thread( () -> { while(true) queue.add(  i.incrementAndGet() ); }, "Producer2").start();
        new Thread( () -> { while(true) queue.add(  i.incrementAndGet() ); }, "Producer3").start();

        // Consumers
        new Thread( () -> { while(true) queue.poll();    }, "Consumer1").start();
        new Thread( () -> { while(true) queue.poll();    }, "Consumer2").start();
        new Thread( () -> { while(true) queue.poll();    }, "Consumer3").start();


        sleep( 5000 );

    }

    private static class BlockingQueueImpl<T>{

        private final int capacity;
        private final List<T> queue;

        private final Lock lock = new ReentrantLock( true );
        private final Condition condition = lock.newCondition();

        public BlockingQueueImpl(int capacity ) {
            this.capacity = capacity;
            this.queue = new LinkedList<>();
        }

        public void add(T elem) {

            lock.lock();
            try {

                while( queue.size() == capacity )
                    // Release Lock
                    // And Wait Till Any Item Is Polled, And Gets Signaled
                    try { condition.await(); } catch (InterruptedException e) { e.printStackTrace(); }

                queue.add(elem);
                condition.signalAll(); // Notify Other Thread, Waiting For Lock. Completed This Synchronized Block

                System.out.println( String.format( "Thread: %s  Element Produced: %s",
                        Thread.currentThread().getName(),
                        elem
                ) );

                sleep(200);

                System.out.println( "Size : " + queue.size() );
            }
            finally {
                lock.unlock();
            }

        }


        public T poll() {

            T elem;

            lock.lock();
            try {

                while( queue.size() == 0 )
                    // Release Lock,
                    // And Wait Till New Item Is Added, And Gets Signaled
                    try { condition.await(); } catch (InterruptedException e) { e.printStackTrace(); }

                elem = queue.remove( 0 );
                condition.signalAll(); // Notify Other Thread, Waiting For Lock. Completed This Synchronized Block

                System.out.println( String.format( "Thread: %s  Polled Element: %s",
                        Thread.currentThread().getName(),
                        elem
                ) );

                sleep(200);
            }
            finally {
                lock.unlock();
            }

            return elem;
        }

    }
}
