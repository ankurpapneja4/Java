package example.core.multithreading.collections;

import example.core.multithreading.ThreadList;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static example.core.multithreading.ThreadUtils.log;
import static example.core.multithreading.ThreadUtils.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CopyOnWriteArrayDemo {

    @Test
    void givenCopyOnWriteArray_whenAddOperation_thenNewArrayIsCreated() {

        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>( List.of( 1, 2,3 ));

        Object[] arr1 = getInternalArray( list );

        // When:  Add New Item
        list.add( 4 ); //  Synchronized Operation
        Object[] arr2 = getInternalArray( list );

        // Then:  New Array Will Be Created Internally
        assertNotEquals( arr1, arr2 );

        // When:  Again Add New Item
        list.add( 0, 5 ); //  Synchronized Operation
        Object[] arr3 = getInternalArray( list );

        // Then: Again New Array Will Be Created Internally
        assertNotEquals( arr2, arr3 );

    }

    @Test
    void givenCopyOnWriteArray_whenGetOperation_thenNewArrayIsNotCreated() {

        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(List.of( 4, 5, 6));

        Object[] arr1 = getInternalArray( list );

        // When Get Values From List
        new ThreadList( () -> {
            IntStream.range( 0, list.size() ).forEach( i -> {
                    log( i + " : " + list.get(i) );  // Non - Synchronous Operation I.e. No Locking
            });
        }).start();

        sleep(1000);
        Object[] arr2 = getInternalArray( list );


        // Then: New Array Is Not Created
        assertEquals( arr1, arr2 );

    }


    @Test
    void givenArrayList_whenAddOperation_thenNewArrayIsNotCreated() {

        ArrayList<Integer> list = new ArrayList<>( 5 );

        Object[] arr1 = getInternalArray( list );

        // When:  Add New Item
        list.add( 1 );
        list.add( 2 );
        list.add( 3 );
        list.add( 4 ); //  Non - Synchronized Operation
        Object[] arr2 = getInternalArray( list );

        // Then:  New Array Will Not Be Created Internally ( Until List Is Full )
        assertEquals( arr1, arr2 );

    }



    /**
     *
     * VM Option:
     * --add-opens java.base/java.util.concurrent=ALL-UNNAMED
     *
     */
    private Object[] getInternalArray( CopyOnWriteArrayList<?> copyOnWriteArrayList) {

        try
        {
            Field array = CopyOnWriteArrayList.class.getDeclaredField( "array");
            array.setAccessible( true );
            return (Object[])array.get( copyOnWriteArrayList );

        }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    /**
     *
     * VM Option:
     * --add-opens java.base/java.util=ALL-UNNAMED
     *
     */
    private Object[] getInternalArray( ArrayList<?> arrayList) {

        try
        {
            Field array = ArrayList.class.getDeclaredField( "elementData");
            array.setAccessible( true );
            return (Object[])array.get( arrayList );

        }
        catch (Exception e) { throw new RuntimeException(e); }
    }

}
