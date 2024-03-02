package example.core.multithreading.threadmanipulation;

import static example.core.multithreading.threadmanipulation.ThreadUtils.sleep;

public class DaemonThreadDemo {

    public static void main(String[] args) {

        Thread normalWorker = new Thread( () -> {
            sleep( 200 );
            System.out.println("Normal Worker Completed ");
        });

        Thread daemonWorker = new Thread( () -> {
            while( true) {  sleep( 20 ); System.out.println("Daemon Running"); }
        });

        // Set Daemon
        daemonWorker.setDaemon( true );

        // Start Thread
        normalWorker.start();
        daemonWorker.start();

        // Main Thread Will Only Wait For NormalWorker To Complete.
        // Main Thread Does Not  Wait For Daemon Thread To Complete.


    }


}
