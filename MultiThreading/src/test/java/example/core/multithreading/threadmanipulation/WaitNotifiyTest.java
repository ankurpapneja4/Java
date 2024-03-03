package example.core.multithreading.threadmanipulation;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static example.core.multithreading.threadmanipulation.ThreadUtils.sleep;

public class WaitNotifiyTest {

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


        sleep( 5000 );

    }

    public static class BlockingQueueImpl<T>{

        private final int capacity;
        private final List<T> queue;

        private final Object lock = new Object();

        public BlockingQueueImpl(int capacity ) {
            this.capacity = capacity;
            this.queue = new LinkedList<>();
        }

        public void add(T elem) {

            synchronized ( lock ) {

                    while( queue.size() == capacity )
                        // Release Intrinsic Lock
                        // And Wait Till Any Item Is Released, And Gets Notified
                        try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }

                    queue.add(elem);
                    lock.notifyAll(); // Notify Other Thread, Waiting For Intrinsic-Lock on 'lock' object; Completed This Synchronized Block

                    System.out.println( String.format( "Thread: %s  Element Produced: %s",
                                Thread.currentThread().getName(),
                                elem
                            ) );

                    sleep(200);

                    System.out.println( "Size : " + queue.size() );
            }

        }


        public T poll() {

            T elem;

            synchronized ( lock ) {

                while( queue.size() == 0 )
                    // Release Intrinsic Lock,
                    // And Wait Till New Item Is Added, And Gets Notified
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }

                elem = queue.remove( 0 );
                lock.notifyAll(); // Notify Other Thread, Waiting For Intrinsic-Lock on 'lock' object; Completed This Synchronized Block

                System.out.println( String.format( "Thread: %s  Polled Element: %s",
                        Thread.currentThread().getName(),
                        elem
                ) );

                sleep(200);
            }

            return elem;
        }

    }
}
