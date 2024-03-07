package example.core.multithreading.collections;

import example.core.multithreading.ThreadList;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SynchronizedCollectionsDemo {


    @Test
    void whenCollectionsAreNotSynchronized_thenConcurrentOperationsCanCauseInconsistency(){

        List<Integer> list = new LinkedList<>();

        new ThreadList( () -> { for(int i=0; i<5000; i++) list.add(i); }).start().join();

        /**
         *
         * If Inconsistent State, Then Test Will Fail
         */
        int expectedValue = ThreadList.defaultSize() * 5000; // 8
        assertEquals( expectedValue, list.size() );

    }

    @Test
    void whenCollectionsAreSynchronized_thenConcurrencyOperationsWillNotCauseInconsistency(){

        // Get Synchronized List
        // Uses Intrinsic Lock - Hence It Is Inefficient
        List<Integer> list = Collections.synchronizedList( new LinkedList<>() );

        new ThreadList( () -> { for(int i=0; i<5000; i++) list.add(i); }).start().join();

        int expectedValue = ThreadList.defaultSize() * 5000; // 8
        assertEquals( expectedValue, list.size() );

    }
}
