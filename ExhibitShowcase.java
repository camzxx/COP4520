import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

class ExhibitVisitor extends Thread {
    static AtomicInteger totalVisits = new AtomicInteger(0);
    private static final Semaphore exhibitEntry = new Semaphore(1);
    private final int visitorId;

    public ExhibitVisitor(int visitorId) {
        this.visitorId = visitorId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                exhibitEntry.acquire();
                System.out.println("Visitor " + visitorId + " is now admiring the exhibit. Entry is now restricted.");
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
                System.out.println("Visitor " + visitorId + " has exited. The next visitor may proceed.");
                exhibitEntry.release();

                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ExhibitShowcase {
    public static void main(String[] args) throws InterruptedException {
        int numberOfVisitors = 10;
        ExhibitVisitor[] visitors = new ExhibitVisitor[numberOfVisitors];

        for (int i = 0; i < numberOfVisitors; i++) {
            visitors[i] = new ExhibitVisitor(i + 1);
            visitors[i].start();

            Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));
        }

        for (int i = 0; i < numberOfVisitors; i++) {
            visitors[i].join();
        }
        System.out.println("The exhibit is now closed. Thank you for visiting.");
    }
}
