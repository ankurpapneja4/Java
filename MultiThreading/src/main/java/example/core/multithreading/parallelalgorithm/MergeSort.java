package example.core.multithreading.parallelalgorithm;

import java.util.Arrays;
import java.util.concurrent.*;

public class MergeSort {

    private final int[] array;
    private final int[] arrayCopy;
    private final ThreadPoolExecutor executor;

    public MergeSort(int[] array , ThreadPoolExecutor executor ){
        this.array = array;
        this.arrayCopy = Arrays.copyOf( array , array.length);
        this.executor = executor;
    }

    public void parallelSort( ){
        parallelSort( this.arrayCopy, 0, this.arrayCopy.length - 1 , this.array, executor.getCorePoolSize() );
    }

    private void parallelSort( int[] array, int begin, int end , int[] result , int avaiableThreads){

        if( avaiableThreads <= 1 ) { sequentialSort( array, begin, end, result); return;}

        if( begin == end ) return;
        int mid =  mid( begin, end );

        Future f1 = this.executor.submit( () -> parallelSort( result, begin, mid, array, avaiableThreads / 2 ));
        Future f2 = this.executor.submit( () -> parallelSort( result, mid + 1, end, array, avaiableThreads / 2));

        await( f1 );
        await( f2 );

        merge( array, begin, mid, end, result);

    }

    public void sequentialSort( ){
        sequentialSort( this.arrayCopy, 0, this.arrayCopy.length - 1 , this.array );
    }


    private void sequentialSort( int[] array, int begin, int end , int[] result ){

        if( begin == end ) return;
        int mid =  mid( begin, end );

        sequentialSort( result, begin, mid, array );
        sequentialSort( result, mid + 1, end, array );

        merge( array, begin, mid, end, result);

    }



    private void merge( int[] array, int begin, int mid, int end, int[] result)  {
         int i = begin;   // First Sub-Array Index
         int j = mid + 1; // Second Sub-Array Index
         int k = begin;   // Result Index

        while( i<=mid && j<=end)
            if( array[i] <= array[j] )
                 result[k++] = array[i++];
            else result[k++] = array[j++];

        while( i<=mid ) result[k++] = array[i++];
        while( j<=end ) result[k++] = array[j++];
    }


    private int mid( int x, int y ){
        return ( x + y ) / 2;
    }

    private void await( Future future){
        try { future.get(); } catch (InterruptedException | ExecutionException e) { e.printStackTrace(); }
    }
}
