## Exercise 10.1

## 10.1.1

The benchmark results show that execution time is directly proportional to NO_TRANSACTION. With 1 transaction taking ~54ms, we see 2 transactions at ~108ms (2x), 4 at ~216ms (4x), 8 at ~432ms (8x), and 16 at ~860ms (16x). This linear relationship happens because each transaction executes sequentially with a 50ms sleep, giving us total time = NO_TRANSACTION × 50ms.

## 10.1.2

The min/max calculation is necessary to prevent deadlock. Without it two threads could acquire locks in opposite orders: Thread A transferring 5->3 locks account 5 then waits for 3, while Thread B transferring 3->5 locks account 3 then waits for 5, creating circular wait and deadlock. By always locking accounts in ascending ID order (min first, then max), both threads acquire locks in the same sequence, eliminating the circular wait. Running the unsafe version (without min/max) with increased thread count caused the program to hang indefinitely due to deadlock, while the safe version always completes successfully.

# Exercise 10.2

## 10.2.1

R(n) = 1.75n + (n/8)log₂(n/8), for n = 100,000:

- R(100,000) = 175,000 + 12,500 × 13.61 ≈ 345,125
- Sequential part: 1.75n = 175,000
- Parallel part: (n/8)log₂(n/8) = 170,125

Maximum speed-up = R(n) / (Sequential + Parallel/N):

- **16 cores**: 345,125 / (175,000 + 170,125/16) = 345,125 / 185,633 ≈ **1.86x**
- **32 cores**: 345,125 / (175,000 + 170,125/32) = 345,125 / 180,316 ≈ **1.91x**

The improvement from 16 to 32 cores is minimal (~0.05x) because the sequential fraction (1.75n) dominates, limiting scalability per Amdahl's law.

## 10.2.2

**For n = 1,000,000:**

- R(1,000,000) = 1,750,000 + 125,000 × 16.93 ≈ 3,866,250
- **16 cores**: 3,866,250 / (1,750,000 + 132,266) ≈ **2.05x**
- **32 cores**: 3,866,250 / (1,750,000 + 66,133) ≈ **2.13x**

**For n = 10,000,000:**

- R(10,000,000) = 17,500,000 + 1,250,000 × 20.25 ≈ 42,812,500
- **16 cores**: 42,812,500 / (17,500,000 + 1,582,031) ≈ **2.24x**
- **32 cores**: 42,812,500 / (17,500,000 + 791,016) ≈ **2.34x**

Larger arrays achieve slightly better speed-ups because the parallelizable portion grows faster (n log n) than the sequential portion (1.75n), but even with 32 cores and 10 million elements, maximum speed-up is only ~2.3x due to Amdahl's law, the sequential fraction fundamentally limits scalability regardless of available cores.

# Exercise 10.3

## 10.3.1

countSequential 1923746.2 ns 8064.95 256
countParallelN 1 1930545.2 ns 27631.38 256
countParallelNLocal 1 1926377.0 ns 6762.68 256
countParallelN 2 1251518.0 ns 3086.91 256
countParallelNLocal 2 1236670.7 ns 2874.51 256
countParallelN 4 743292.1 ns 6762.70 512
countParallelNLocal 4 714565.6 ns 8694.10 512
countParallelN 8 707537.5 ns 4948.79 512
countParallelNLocal 8 678432.4 ns 9934.70 512
countParallelN 16 773406.6 ns 6779.07 512
countParallelNLocal 16 745930.6 ns 21024.04 512
blankapadar@Blankas-MacBook-Air exercise10 %

The benchmark shows good scalability with countParallelNLocal going from 1.93ms (1 thread) down to 0.68ms (8 threads), a 2.84x speedup. Performance hits a wall at 16 threads (0.75ms), which makes sense given my M4's 10-core limit. The countParallelNLocal version consistently outperforms countParallelN by around 5% because it avoids contention on the shared AtomicLong by using local variables instead and only combining results once at the end. This is a textbook example of saturation loss, the AtomicLong becomes a bottleneck when everyone is fighting over it.

## 10.3.2

countSequential 2031872.8 ns 57929.11 128
countExecutorAtomic 1 1956658.5 ns 42691.87 256
countExecutorCallable 1 1976982.6 ns 14222.59 128
countExecutorAtomic 2 1265377.9 ns 7473.62 256
countExecutorCallable 2 1246854.0 ns 4434.77 256
countExecutorAtomic 4 754156.8 ns 9748.37 512
countExecutorCallable 4 762579.8 ns 12780.82 512
countExecutorAtomic 8 727112.7 ns 11020.53 512
countExecutorCallable 8 742033.0 ns 8415.75 512
countExecutorAtomic 16 835272.2 ns 15145.99 512
countExecutorCallable 16 904909.2 ns 10865.13 512
blankapadar@Blankas-MacBook-Air exercise10 %

The executor version performs similarly to raw threads but is slightly slower. At 8 threads executors take 0.73ms vs threads' 0.68ms. This differs from what we would expect because the implementation uses coarse-grained parallelism, just N large tasks (each processing 100,000/N numbers) instead of fine-grained recursive splitting that creates thousands of small tasks. With so few large tasks, thread pool reuse provides minimal benefit, while executor overhead (wrapping tasks, managing queues, signaling threads) becomes noticeable. The results demonstrate that executors excel with many small dynamic tasks rather than a few large static ones, and task granularity is crucial for determining when executors provide performance advantages.

# Exercise 10.4

**What implementation performs better?**

CAS (CasHistogram) performs significantly better than the monitor version (HistogramLocks). At 4 threads CAS is 3.0x faster and beats the monitor at every thread count. More importantly, the monitor actually gets slower as we add threads, showing it completely fails to parallelize.

**Is the result expected? Explain why**

Yes. The monitor uses a single synchronized lock for the whole histogram, so all 100,000 increment operations get serialized through one bottleneck. Threads spend more time waiting for the lock than actually working, causing saturation loss.
CAS works better because it uses 30 independent AtomicIntegers. Different threads can increment different bins in parallel without blocking each other, which is why we get 2.1x speedup at 4 threads. Performance drops at 8+ threads because we hit hardware limits (my M4 has 10 cores) plus the fact that most numbers have 1-3 factors means multiple threads often fight over the same hot bins, causing CAS retries.
