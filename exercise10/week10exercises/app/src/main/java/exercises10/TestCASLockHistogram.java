package exercises10;
// raup@itu.dk * 10/10/2021
// jst@itu.dk * 08/20/2025
import benchmarking.Benchmark;

class TestCASLockHistogram {
    public static void main(String[] args) {
        new TestCASLockHistogram();
    }
    
    public TestCASLockHistogram() {
        int noThreads = 16;
        int range = 100_000;
        
        System.out.println("=== Benchmarking Histogram Implementations ===");
        System.out.println("Range: " + range + " numbers");
        System.out.println("Max threads: " + noThreads + "\n");
        
        // Benchmark.Mark7( ... using a monitor
        System.out.println("=== MONITOR IMPLEMENTATION (synchronized) ===\n");
        for (int i = 1; i <= noThreads; i *= 2) {  
            int threadCount = i;
            Benchmark.Mark7(
                String.format("HistogramLocks   %2d", threadCount),
                iter -> {
                    Histogram histogramLock = new HistogramLocks(30);
                    countParallel(range, threadCount, histogramLock);
                    return 0.0;
                }
            );
        }
        
        // Benchmark.Mark7( ... using CAS
        System.out.println("\n=== CAS IMPLEMENTATION (AtomicInteger) ===\n");
        for (int i = 1; i <= noThreads; i *= 2) {  
            int threadCount = i;
            Benchmark.Mark7(
                String.format("CasHistogram     %2d", threadCount),
                iter -> {
                    Histogram histogramCAS = new CasHistogram();
                    countParallel(range, threadCount, histogramCAS);
                    return 0.0;
                }
            );
        }
    }
    
    // Function to count the prime factors of a number `p`
    private static int countFactors(int p) {
        if (p < 2) return 0;
        int factorCount = 1, k = 2;
        while (p >= k * k) {
            if (p % k == 0) {
                factorCount++;
                p = p / k;
            } else 
                k = k + 1;
        }
        return factorCount;
    }
    
    // Parallel execution of counting the number of primes for numbers in `range`
    private static void countParallel(int range, int threadCount, Histogram h) {
        final int perThread = range / threadCount;
        Thread[] threads = new Thread[threadCount];
        for (int t = 0; t < threadCount; t = t + 1) {
            final int from = perThread * t, 
                        to = (t + 1 == threadCount) ? range : perThread * (t + 1); 
            threads[t] = new Thread(() -> {
                for (int i = from; i < to; i++) h.increment(countFactors(i));
            });
        } 
        for (int t = 0; t < threadCount; t = t + 1) 
            threads[t].start();
        try {
            for (int t = 0; t < threadCount; t = t + 1) 
                threads[t].join();
        } catch (InterruptedException exn) { }
    }
    
    // Auxiliary method to print the histogram data
    public static void dump(Histogram histogram) {
        for (int bin = 0; bin < histogram.getSpan(); bin = bin + 1) {
            System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
        }
    }
}