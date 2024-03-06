package example.core.multithreading;

public class ThreadUtils {

    public static void sleep(long millis){
        try { Thread.sleep(millis); } catch (InterruptedException e) {  throw new RuntimeException(e); }
    }


    public static void log(String message){

        System.out.println( String.format("[ %d - %s ] : %s",
                Thread.currentThread().getId(),
                Thread.currentThread().getName(),
                message ));
    }
}
