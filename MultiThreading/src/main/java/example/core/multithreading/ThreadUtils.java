package example.core.multithreading;

import java.util.Random;

public class ThreadUtils {

    private static final Random randomNumGenerator = new Random();
    public static void sleep(long millis){
        try { Thread.sleep(millis); } catch (InterruptedException e) {  throw new RuntimeException(e); }
    }


    public static void sleepRandom() {
        long sleepMillis = randomNumGenerator.nextLong( 200, 1000);
        try { Thread.sleep(sleepMillis); } catch (InterruptedException e) {  throw new RuntimeException(e); }
    }


    public static void log(String message){

        System.out.println( String.format("[ %d - %s ] : %s",
                Thread.currentThread().getId(),
                Thread.currentThread().getName(),
                message ));
    }
}
