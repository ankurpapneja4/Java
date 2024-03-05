package example.core.multithreading;

public class ThreadUtils {

    public static void sleep(long millis){
        try { Thread.sleep(millis); } catch (InterruptedException e) {  throw new RuntimeException(e); }
    }


    public static void log(String message){

        System.out.println( String.format("[%s] : %s",
                Thread.currentThread().getName(),
                message ));
    }
}
