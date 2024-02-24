import java.util.concurrent.atomic.AtomicLong;
import java.util.Arrays;

class PrimeFinder extends Thread {
    private int lowerBound;
    private int upperBound;
    private boolean[] isPrime;
    private AtomicLong primeCount;
    private AtomicLong primeSum;

    public PrimeFinder(int lowerBound, int upperBound, boolean[] isPrime, AtomicLong primeCount, AtomicLong primeSum) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.isPrime = isPrime;
        this.primeCount = primeCount;
        this.primeSum = primeSum;
    }

    public void run() {
        for (int i = lowerBound; i <= upperBound; i++) {
            if (isPrime[i]) {
                primeCount.incrementAndGet();
                primeSum.addAndGet(i);
            }
        }
    }
}

public class PrimeCounter {
    public static void main(String[] args) throws InterruptedException {
        final int MAX_LIMIT = 100000000;
        boolean[] isPrime = new boolean[MAX_LIMIT + 1];
        Arrays.fill(isPrime, true);

        for (int num = 2; num * num <= MAX_LIMIT; num++) {
            if (isPrime[num]) {
                for (int multiple = num * 2; multiple <= MAX_LIMIT; multiple += num) {
                    isPrime[multiple] = false;
                }
            }
        }

        AtomicLong primeCount = new AtomicLong(0);
        AtomicLong primeSum = new AtomicLong(0);

        int threadCount = Runtime.getRuntime().availableProcessors();
        PrimeFinder[] workers = new PrimeFinder[threadCount];

        for (int i = 0; i < threadCount; i++) {
            int start = (MAX_LIMIT / threadCount) * i + 2;
            int end = (MAX_LIMIT / threadCount) * (i + 1) + 1;

            if (end > MAX_LIMIT) {
                end = MAX_LIMIT;
            }

            workers[i] = new PrimeFinder(start, end, isPrime, primeCount, primeSum);
            workers[i].start();
        }

        for (PrimeFinder worker : workers) {
            worker.join();
        }

        System.out.println("Count of Prime Numbers: " + primeCount);
        System.out.println("Sum of Prime Numbers: " + primeSum);
    }
}
