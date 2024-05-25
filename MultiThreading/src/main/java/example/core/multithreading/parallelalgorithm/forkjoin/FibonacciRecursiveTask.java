package example.core.multithreading.parallelalgorithm.forkjoin;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class FibonacciRecursiveTask extends RecursiveTask<BigInteger> {

    private final long number;
    private final Map< Long, FibonacciRecursiveTask> queuedTasks;

    public FibonacciRecursiveTask( long number ){
        this( number, new HashMap<>() );
    }

    private FibonacciRecursiveTask(long number , Map<Long, FibonacciRecursiveTask> queuedTasks){
        this.number = number;
        this.queuedTasks = queuedTasks;
    }

    @Override
    protected BigInteger compute() {

        if( number <= 1 ) return  BigInteger.valueOf( number );

        FibonacciRecursiveTask task1 = getFromQueue( number - 1 );
        FibonacciRecursiveTask task2 = getFromQueue( number - 2 );


        // fork() : submit task To pool
        task1.fork();
        task2.fork();

        // join(): wait for task to complete
        BigInteger Sn_1 = task1.join();
        BigInteger Sn_2 = task2.join();

        return Sn_1.add( Sn_2 );
    }

    public FibonacciRecursiveTask getFromQueue( long number ) {
        FibonacciRecursiveTask task;
        synchronized ( queuedTasks ) {
            task = queuedTasks.get(number);

            // If no task mapped with given number, create task and add to map
            if (task == null) {
                task = new FibonacciRecursiveTask(number, this.queuedTasks);
                this.queuedTasks.put(number, task);
            }
        }
        return  task;
    }
}
