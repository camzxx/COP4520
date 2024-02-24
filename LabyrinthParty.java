import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LabyrinthParty {

    public static void main(String[] args) {
        final int partyGuests = 10; // Renamed for clarity
        int designatedLeader = 0; // Changed variable name for clarity
        ReentrantLock partyLock = new ReentrantLock(); // Renamed to fit the theme
        ExecutorService service = Executors.newFixedThreadPool(partyGuests);
        AtomicInteger treats = new AtomicInteger(1); // Renamed to 'treats' for variety
        AtomicInteger visitorsCount = new AtomicInteger(0); // Changed variable name for clarity

        Future<?>[] guestFutures = new Future<?>[partyGuests]; // Renamed for clarity
        for (int i = 0; i < partyGuests; i++) {
            PartyGuest guest = new PartyGuest(i, designatedLeader, partyLock, treats, visitorsCount, partyGuests);
            guestFutures[i] = service.submit(guest);
        }

        service.shutdown();
        for (Future<?> future : guestFutures) {
            try {
                future.get(); // No change needed here, essential for future task completion
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace(); // No change needed here, standard error handling
            }
        }
    }
}

class PartyGuest implements Runnable {

    private final int guestID;
    private final int guestsTotal;
    private final ReentrantLock partyLock;
    private final AtomicInteger treats;
    private final AtomicInteger visitorsCounter;
    private final boolean isPartyLeader;
    private boolean firstVisitComplete = false;
    private boolean treatConsumed = false;

    public PartyGuest(int id, int leaderID, ReentrantLock lock, AtomicInteger treats,
                      AtomicInteger visitorsCount, int totalGuests) {
        this.guestID = id;
        this.isPartyLeader = (id == leaderID);
        this.partyLock = lock;
        this.treats = treats;
        this.visitorsCounter = visitorsCount;
        this.guestsTotal = totalGuests;
    }

    @Override
    public void run() {
        while (true) {
            partyLock.lock();
            try {
                if (!firstVisitComplete && treats.get() == 1) {
                    if (isPartyLeader) {
                        treats.decrementAndGet();
                        System.out.printf("Leader Guest #%d discovered and enjoyed the treat on their initial visit.%n", guestID);
                        treatConsumed = true;
                    } else {
                        System.out.printf("Guest #%d passed on the treat during their initial visit.%n", guestID);
                    }
                    firstVisitComplete = true;
                } else if (firstVisitComplete && treats.get() == 1) {
                    if (!isPartyLeader && !treatConsumed) {
                        treats.decrementAndGet();
                        System.out.printf("Guest #%d decided to enjoy the treat on a later visit.%n", guestID);
                        treatConsumed = true;
                    }
                } else if (isPartyLeader && treats.get() == 0) {
                    treats.incrementAndGet();
                    visitorsCounter.incrementAndGet();
                    System.out.printf("Leader Guest #%d noticed the missing treat and replenished it.%n", guestID);
                    if (visitorsCounter.get() == guestsTotal) {
                        System.out.println("Every guest has now visited the labyrinth.");
                        break;
                    }
                }
            } finally {
                partyLock.unlock();
            }
        }
    }
}