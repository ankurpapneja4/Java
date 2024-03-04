package example.core.multithreading.threadcommunication.lock;

import example.core.multithreading.ThreadList;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static example.core.multithreading.ThreadUtils.sleep;

public class ReentrantLockTest {

    @Test
    void synchronizedBlock_UsingReentrantLock(){

        Synchronized obj = new Synchronized();

        ThreadList threads1 =new ThreadList( () -> {
            for(int i =0; i<100; i++) obj.updateSharedResource1(); }, "Worker").start().join();

        System.out.println(obj.sharedResource);
    }

    private static class Synchronized {

        public final Lock lock = new ReentrantLock();

        private int sharedResource = 0;


        public void updateSharedResource1() {

           //Acquire The Lock
           lock.lock();

           try {
                int readValue = sharedResource;
                sleep(10);
                sharedResource = readValue + 1;
            }
           finally {
               // Release Lock
               lock.unlock();
           }


        }
    }
}
