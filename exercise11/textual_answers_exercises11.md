# Assignment 5

## Exercise 11.1

Benchmark results on MacBook Pro M1 Pro - 32 Gb RAM, 10 Cores (8 performance and 2 efficiency):

```bash
Sequential                      2573948.6 ns    9000.34        128
IntStream                       2590105.0 ns    3069.88        128
Parallel                         461166.8 ns    5313.02       1024
ParallelStream                   446384.8 ns    1364.39       1024
```

## Exercise 11.3

### Exercise 11.3.1

Redo of example Java8ParallelStreamMain on MacBook Pro M1 Pro - 32 Gb RAM, 10 Cores (8 performance and 2 efficiency):

```bash
=================================
Using Sequential Stream
=================================
1 main
2 main
3 main
4 main
5 main
6 main
7 main
8 main
9 main
10 main
=================================
Using Parallel Stream
=================================
7 main
6 main
9 main
10 main
8 ForkJoinPool.commonPool-worker-3
1 main
4 ForkJoinPool.commonPool-worker-4
3 ForkJoinPool.commonPool-worker-1
2 ForkJoinPool.commonPool-worker-2
5 ForkJoinPool.commonPool-worker-3
```

The workload is very small, so it is normal that main would still do a big chunk of it. In this example the system surely spends more
resources forking, coordinating and joining the different streams than actually performing the task of printing every number and its thread.

### Exercise 11.3.2

I increased the number of ints in the array to 100 and this resulted in a more predictable outcome: main still did a good part of the work at the beginning, however, it spawned up to 9 workers (as it can be seen below) who did the bulk of the work, and this makes perfect sense since my laptop has 10 cores (9 workers + main).

```bash
=================================
Using Sequential Stream
=================================
0 main
1 main
2 main
3 main
4 main
5 main
6 main
7 main
8 main
9 main
10 main
11 main
12 main
13 main
14 main
15 main
16 main
17 main
18 main
19 main
20 main
21 main
22 main
23 main
24 main
25 main
26 main
27 main
28 main
29 main
30 main
31 main
32 main
33 main
34 main
35 main
36 main
37 main
38 main
39 main
40 main
41 main
42 main
43 main
44 main
45 main
46 main
47 main
48 main
49 main
50 main
51 main
52 main
53 main
54 main
55 main
56 main
57 main
58 main
59 main
60 main
61 main
62 main
63 main
64 main
65 main
66 main
67 main
68 main
69 main
70 main
71 main
72 main
73 main
74 main
75 main
76 main
77 main
78 main
79 main
80 main
81 main
82 main
83 main
84 main
85 main
86 main
87 main
88 main
89 main
90 main
91 main
92 main
93 main
94 main
95 main
96 main
97 main
98 main
99 main
=================================
Using Parallel Stream
=================================
65 main
66 main
67 main
63 main
64 main
62 main
71 main
72 main
73 main
74 main
69 main
70 main
68 main
57 main
15 ForkJoinPool.commonPool-worker-2
58 main
56 main
16 ForkJoinPool.commonPool-worker-2
32 ForkJoinPool.commonPool-worker-1
90 ForkJoinPool.commonPool-worker-4
44 ForkJoinPool.commonPool-worker-3
45 ForkJoinPool.commonPool-worker-3
91 ForkJoinPool.commonPool-worker-4
7 ForkJoinPool.commonPool-worker-9
8 ForkJoinPool.commonPool-worker-9
33 ForkJoinPool.commonPool-worker-1
17 ForkJoinPool.commonPool-worker-2
60 main
13 ForkJoinPool.commonPool-worker-2
14 ForkJoinPool.commonPool-worker-2
31 ForkJoinPool.commonPool-worker-1
6 ForkJoinPool.commonPool-worker-9
35 ForkJoinPool.commonPool-worker-1
92 ForkJoinPool.commonPool-worker-4
28 ForkJoinPool.commonPool-worker-8
88 ForkJoinPool.commonPool-worker-4
29 ForkJoinPool.commonPool-worker-8
30 ForkJoinPool.commonPool-worker-8
38 ForkJoinPool.commonPool-worker-6
40 ForkJoinPool.commonPool-worker-5
43 ForkJoinPool.commonPool-worker-3
48 ForkJoinPool.commonPool-worker-7
49 ForkJoinPool.commonPool-worker-7
41 ForkJoinPool.commonPool-worker-5
39 ForkJoinPool.commonPool-worker-6
26 ForkJoinPool.commonPool-worker-8
27 ForkJoinPool.commonPool-worker-8
89 ForkJoinPool.commonPool-worker-4
36 ForkJoinPool.commonPool-worker-1
10 ForkJoinPool.commonPool-worker-9
12 ForkJoinPool.commonPool-worker-2
61 main
21 ForkJoinPool.commonPool-worker-2
11 ForkJoinPool.commonPool-worker-9
53 ForkJoinPool.commonPool-worker-1
54 ForkJoinPool.commonPool-worker-1
55 ForkJoinPool.commonPool-worker-1
87 ForkJoinPool.commonPool-worker-4
51 ForkJoinPool.commonPool-worker-1
34 ForkJoinPool.commonPool-worker-3
96 ForkJoinPool.commonPool-worker-4
97 ForkJoinPool.commonPool-worker-4
98 ForkJoinPool.commonPool-worker-4
25 ForkJoinPool.commonPool-worker-8
37 ForkJoinPool.commonPool-worker-6
42 ForkJoinPool.commonPool-worker-5
3 ForkJoinPool.commonPool-worker-8
46 ForkJoinPool.commonPool-worker-7
47 ForkJoinPool.commonPool-worker-7
94 ForkJoinPool.commonPool-worker-5
93 ForkJoinPool.commonPool-worker-7
4 ForkJoinPool.commonPool-worker-8
1 ForkJoinPool.commonPool-worker-7
2 ForkJoinPool.commonPool-worker-7
82 ForkJoinPool.commonPool-worker-6
99 ForkJoinPool.commonPool-worker-4
50 ForkJoinPool.commonPool-worker-3
76 ForkJoinPool.commonPool-worker-3
52 ForkJoinPool.commonPool-worker-1
9 ForkJoinPool.commonPool-worker-9
22 ForkJoinPool.commonPool-worker-2
59 main
18 ForkJoinPool.commonPool-worker-2
23 ForkJoinPool.commonPool-worker-9
24 ForkJoinPool.commonPool-worker-9
84 ForkJoinPool.commonPool-worker-9
75 ForkJoinPool.commonPool-worker-9
19 ForkJoinPool.commonPool-worker-1
77 ForkJoinPool.commonPool-worker-3
78 ForkJoinPool.commonPool-worker-4
83 ForkJoinPool.commonPool-worker-6
0 ForkJoinPool.commonPool-worker-7
5 ForkJoinPool.commonPool-worker-8
95 ForkJoinPool.commonPool-worker-5
20 ForkJoinPool.commonPool-worker-1
81 ForkJoinPool.commonPool-worker-9
85 ForkJoinPool.commonPool-worker-2
79 main
80 main
86 ForkJoinPool.commonPool-worker-2
```

### Exercise 11.3.3

The output of `Java8ParallelStreamMain.java` on a more time-consuming task is saved in this same folder on `Java8ParallelStreamMainPrimes.txt`

I modified `Java8ParallelStreamMain.java`, so that it would take an array of 500 integers, check every single one and print if it is a primer or not. I tried the same with up to 100,000 integers and the result was the same as described below.

In this case, as in 11.3.2 main does a tiny fraction of the workload, plus spawning and scheduling while the bulk of it is performed by all the other threads (9). Worker 8 and 9 do a smaller part of the workload - processing about 30 integers each instead of around 60 as the other cores do, probably because these are the workers corresponding to the smaller efficiency cores of my laptop.

Also tried running `PerformanceComparisonMain.java` (as per the article "Introduction to Java 8 Parallel Stream — Java2Blog") and while its sequential version took 1 minute, 3 seconds it only used a fraction of the total CPU capacity, however, its parallel version, finished in only 9 seconds (7 times faster) while using a staggering 97% of CPU capacity.

## Exercise 11.4

### Exercise 11.4.2

When the below java stream code runs `source()` would retrieve the data from the file and filter every word 
based on its length, if if the length is larger than 5 it will be absorbed.

```JavaStream
source().filter(w -> w.length() > 5).sink();
```

However, in the case of RxJava below, the Observable will push the data over to the Observers (subscribers) which will then all words they have capacity for (since some may be disposed/ lost in the case of backpressure - when the Observable pushes more data than the consumer can consume), filter them (length larger than 10) and absorb them.

```RxJava
source().filter(w -> w.length() > 10).sink()
```
