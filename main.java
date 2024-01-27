import java.util.concurrent.atomic.AtomicLong;
import java.util.Arrays;

class PrimeCalculator extends Thread {
    private int start;
    private int end;
    private boolean[] primes;
    private AtomicLong count;
    private AtomicLong sum;

    public PrimeCalculator(int start, int end, boolean[] primes, AtomicLong count, AtomicLong sum) {
        this.start = start;
        this.end = end;
        this.primes = primes;
        this.count = count;
        this.sum = sum;
    }

    public void run() {
        for (int i = start; i <= end; i++) {
            if (primes[i]) {
                count.incrementAndGet();
                sum.addAndGet(i);
            }
        }
    }
}

public class main {
    public static void main(String[] args) throws InterruptedException {
        int n = 100000000;
        boolean[] primes = new boolean[n + 1];
        Arrays.fill(primes, true);

        for (int p = 2; p * p <= n; p++) {
            if (primes[p]) {
                for (int i = p * 2; i <= n; i += p) {
                    primes[i] = false;
                }
            }
        }

        AtomicLong count = new AtomicLong(0);
        AtomicLong sum = new AtomicLong(0);

        int numThreads = Runtime.getRuntime().availableProcessors();
        PrimeCalculator[] threads = new PrimeCalculator[numThreads];

        for (int i = 0; i < numThreads; i++) {
            int start = (n / numThreads) * i + 2;
            int end = (n / numThreads) * (i + 1) + 1;

            if (end > n) {
                end = n;
            }

            threads[i] = new PrimeCalculator(start, end, primes, count, sum);
            threads[i].start();
        }

        for (PrimeCalculator thread : threads) {
            thread.join();
        }

        System.out.println("Total Primes: " + count);
        System.out.println("Sum of Primes: " + sum);
    }
}
