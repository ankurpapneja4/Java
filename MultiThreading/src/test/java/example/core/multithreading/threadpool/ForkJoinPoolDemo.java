package example.core.multithreading.threadpool;

import example.core.multithreading.parallelalgorithm.forkjoin.FibonacciRecursiveTask;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinPoolDemo {

    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    @Test
    public  void findFibonacciNumberTest() {

        BigInteger Sn = forkJoinPool.invoke( new FibonacciRecursiveTask(1000) );

        System.out.println( Sn );

    }
}
