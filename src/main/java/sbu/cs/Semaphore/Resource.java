package sbu.cs.Semaphore;

import java.time.LocalTime;
import java.util.concurrent.Semaphore;

public class Resource {

    public static Semaphore sem = new Semaphore(2);

    public static void accessResource(String threadName) {
        try {
            sem.acquire();
            LocalTime currentTime = LocalTime.now();
            System.out.println(threadName + " accessed resource on " + currentTime);
            Thread.sleep(100);
            sem.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
