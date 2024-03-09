package example.core.multithreading.collections;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentMapDemo {

    /**
     *
     * ConcurrentHashMap
     *
     *   Concurrency Level and Segmentation:
     *     - The object is divided into segments based on the concurrency level.
     *     - By default, ConcurrentHashMap has 16 segments.
     *     - Retrieval operations can be performed concurrently without locking the entire map.
     *     - For updates, the thread must lock the specific segment where it wants to operate (known as segment locking or bucket locking).
     *     - This design allows up to 16 update operations to occur simultaneously.
     *     - Unlike HashMap, ConcurrentHashMap does not allow inserting null keys or values.
     *
     */
    @Test
    void concurrentHashMapDemo() {

        ConcurrentMap<String, String> map  = new ConcurrentHashMap<>();

        // Insert
        map.put("k1","v1");
        map.put("k2","v2");
        map.put("k3","v3");

        // Get
        map.get( "k1" );

        // Remove
        map.remove( "k2");

        // Null Cannot Be Inserted
        try { map.put("k4", null); } catch ( RuntimeException e) { e.printStackTrace(); }

    }
}
