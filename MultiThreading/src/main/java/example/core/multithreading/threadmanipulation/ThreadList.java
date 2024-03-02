package example.core.multithreading.threadmanipulation;

import java.util.ArrayList;
import java.util.List;

public class ThreadList {

    List<Thread> threads;

    public ThreadList(Runnable runnable, String name){

        int listSize = defaultSize();

        this.threads = new ArrayList<>( listSize );

        for(int i = 0; i < listSize; i++)
            threads.add( new Thread( runnable , name+i));

    }

    public ThreadList(Runnable runnable){
        this(runnable, "Thread");
    }

    public int size(){
        return threads.size();
    }

    public ThreadList start(){
        threads.forEach( Thread::start);
        return this;
    }

    public ThreadList join() {
        threads.forEach(t -> {
            try { t.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        });
        return this;
    }

    public ThreadList priority(int newPriority) {
        threads.forEach(t -> t.setPriority( newPriority ));
        return this;
    }

    public static int defaultSize(){
        int CPU_CORES = Runtime.getRuntime().availableProcessors();;
        return CPU_CORES;
    }

}
