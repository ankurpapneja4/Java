package example.core.multithreading.algorithm;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import static example.core.multithreading.ThreadUtils.*;

public class DiningPhilosophersProblem {

    @Test
    void diningPhilosopherProblemDemo() {

        final int numberOfPhilosophers = 5;

        // Get Forks Ready
        Lock[] forks = new Lock[ numberOfPhilosophers ];
        IntStream.range( 0, forks.length ).forEach( i -> forks[i] = new ReentrantLock( true ) );


        // Get Philosophers Ready
        Philosopher[] philosophers = new Philosopher[ numberOfPhilosophers ];
        IntStream.range( 0, philosophers.length ) .forEach( i -> {
                    philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % forks.length]);
                    philosophers[i].start();
                });

        sleep( 10000 );

        // Interrupt
        IntStream.range( 0, philosophers.length ) .forEach( i -> philosophers[i].interrupt());



    }

    public static class  Philosopher extends Thread{

        private final int position;
        private final Lock leftFork;

        private final Lock rightFork;

        private int eatingCount = 0;
        private int thinkingCount = 0;



        public Philosopher(int position, Lock leftFork, Lock rightFork) {
            this.position = position;
            this.leftFork = leftFork;
            this.rightFork = rightFork;

            setName( "Philosopher-" + position);
        }

        @Override
        public void interrupt() {
            summary();
        }

        public void think() {

            log( "Thinking" );
            ++thinkingCount;
            sleepRandom();
        }

        public void eat(){

            try
            {
                lock();

                log( "Eating" );
                ++eatingCount;
                sleepRandom();
            }
            catch (InterruptedException exp) { this.interrupt(); }
            finally
            {
                unlock();
            }

        }

        private void lock() throws InterruptedException {

            while( true ){

                rightFork.lock();

                if( leftFork.tryLock(500, TimeUnit.MILLISECONDS) ) break;
                else
                {
                    log( "Could Not Get Left Fork ");
                    rightFork.unlock();
                }
                // Retry after 10ms
                sleep( 10 );
            }
        }

        private void unlock() {
            leftFork.unlock();
            rightFork.unlock();
        }


        public void summary() {
            System.out.println(
                    this.getName() + " : Eating Count: " + eatingCount + " Thinking Count: " + thinkingCount);
        }


        @Override
        public void run() {
            while ( true ){ think(); eat();  }
        }
    }

}
