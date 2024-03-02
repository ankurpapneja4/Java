package example.core.multithreading.threadmanipulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JoinTest {


    @Test
    void withoutJoin_DoesNotWait_ForThreadToComplete(){

        final AtomicInteger x = new AtomicInteger(0);
        final AtomicInteger y = new AtomicInteger(0);

        Thread t1 = new Thread( () -> { sleep(50); x.incrementAndGet();  });
        Thread t2 = new Thread( () -> { sleep(50); y.incrementAndGet();  });

        t1.start();
        t2.start();

        // Values not incremented. Threads have not completed execution.
        assertEquals( 0, x.get() );
        assertEquals( 0, y.get() );

    }

    @Test
    void withJoin_Waits_ForThreadToComplete() throws InterruptedException {

        final AtomicInteger x = new AtomicInteger(0);
        final AtomicInteger y = new AtomicInteger(0);

        Thread t1 = new Thread( () -> { sleep(50); x.incrementAndGet();  });
        Thread t2 = new Thread( () -> { sleep(50); y.incrementAndGet();  });

        t1.start();
        t2.start();

        // Join : wait for thread t1 and t2 to complete
        t1.join();
        t2.join();

        // Values should be incremented by Threads.
        assertEquals( 1, x.get() );
        assertEquals( 1, y.get() );

    }


    private void sleep(long millis){
        try { Thread.sleep(millis); } catch (InterruptedException e) {  throw new RuntimeException(e); }
    }
}
