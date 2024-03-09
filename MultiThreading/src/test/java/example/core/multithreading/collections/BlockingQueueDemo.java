package example.core.multithreading.collections;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static example.core.multithreading.ThreadUtils.log;
import static example.core.multithreading.ThreadUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockingQueueDemo {


    /**
     *
     * Synchronous Queue
     *
     *   Implementation: It does not store elements internally.
     *
     *   Blocking Behavior:
     *     A put() call will not return until there is a corresponding take() call.
     *     Useful for direct handoff between producer and consumer threads.
     *
     *   Use Case: Ideal for scenarios where immediate exchange of data is required.
     *
     */
    @Test
    void synchronousQueueDemo() throws InterruptedException {

        BlockingQueue<Integer> queue = new SynchronousQueue<>();

        // SynchronousQueue have 0 capacity
        assertEquals( 0, queue.remainingCapacity());

        CountDownLatch latch = new CountDownLatch( 3 );

        // Producer Thread
        new Thread( () -> {
            int i = 0;
            while(true)
                try
                {
                    // Blocked until a consumer takes the value
                    queue.put( ++i );
                    log( "Put Successful");

                }
                catch (InterruptedException e) { throw new RuntimeException(e); }

        }, "Producer").start();


        // Consumer Thread
        new Thread( () -> {
            while(true)
                try
                {
                    sleep( 1000 );
                    // Blocked until a producer puts the value
                    int element = queue.take( );
                    log( "Take Successful " + element );

                }
                catch (InterruptedException e) { throw new RuntimeException(e); }
                finally { latch.countDown(); }

        }, "Consumer").start();

        latch.await(5, TimeUnit.SECONDS);

    }

    /**
     *
     * ArrayBlockingQueue:
     *
     *   Implementation: It is backed by an array.
     *
     *   Bounded: ArrayBlockingQueue has a fixed size (bounded). Once created, its size cannot be changed.
     *
     *   Blocking Behavior:
     *     If you try to insert an element into a full queue, the operation will block until space becomes available.
     *     Similarly, if you attempt to take an element from an empty queue, the operation will also block.
     *
     *   Use Case: Suitable for scenarios where a fixed-size queue is needed.
     *
     */
    @Test
    void arrayBlockingQueueDemo() {

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);

        // SynchronousQueue have 0 capacity
        assertEquals( 3, queue.remainingCapacity());


        // Producer Thread
        new Thread( () -> {
            int i = 0;
            while(true)
                try
                {
                    // Blocked until queue is full
                    queue.put( ++i );
                    log( "Put Successful : " + i);

                    if( i == 6 ) break; // Terminate after inserting 1 - 6

                }
                catch (InterruptedException e) { throw new RuntimeException(e); }

        }, "Producer").start();


        // Consumer Thread
        new Thread( () -> {
            while(true)
                try
                {
                    sleep( 1000 );
                    // Blocked until queue is empty
                    int element = queue.take( );
                    log( "Take Successful " + element );

                }
                catch (InterruptedException e) { throw new RuntimeException(e); }
        }, "Consumer").start();


        sleep( 15000);

    }

    /**
     *
     * LinkedBlockingQueue:
     *
     *   Implementation: It is backed by linked nodes.
     *
     *   Bounded: Optionally bounded (capacity can be specified during construction).
     *
     *   Blocking Behavior:
     *     Similar to ArrayBlockingQueue, it blocks when inserting into a full queue or taking from an empty queue.
     *     If queue capacity is unbounded insert in non-blocking, but taking an element is blocking until queue is empty.
     *
     *   Order: Elements are stored in FIFO order.
     *
     */
    @Test
    void linkedBlockingQueueDemo() {

        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

        // SynchronousQueue is unbounded
        assertEquals( Integer.MAX_VALUE, queue.remainingCapacity());

        // Consumer Thread
        new Thread( () -> {
            while(true)
                try
                {
                    sleep( 100 );
                    // Blocked until queue is empty
                    int element = queue.take( );
                    log( "Take Successful " + element );

                }
                catch (InterruptedException e) { throw new RuntimeException(e); }
        }, "Consumer").start();


        // Producer Thread
        new Thread( () -> {
            int i = 0;
            while(true)
                try
                {
                    // Put Operation Is Non-Blocking
                    queue.put( ++i );
                    log( "Put Successful : " + i);

                    if( i == 9 ) break; // Terminate after inserting 1 - 9

                }
                catch (InterruptedException e) { throw new RuntimeException(e); }

        }, "Producer").start();


        sleep( 5000);

    }

}
