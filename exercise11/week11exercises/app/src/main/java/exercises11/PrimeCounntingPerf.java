package exercises11;
//Exercise 11.1
//JSt vers Aug 20, 2025

import java.util.*;
import java.util.stream.*;
import benchmarking.Benchmark;

class PrimeCountingPerf { 
  public static void main(String[] args) {
    new PrimeCountingPerf(); 

    //System.out.println(countIntStream(range));
    //System.out.println(countSequential(range));
    //System.out.println(countParallel(range));

    //List<Integer> list = new ArrayList<Integer>();
    //for (int i= 2; i< range; i++){ list.add(i); }

    //System.out.println(countparallelStream(list));
    //printPrimeStream(range);
  
  }

  static final int range= 100000;

  //Test whether n is a prime number
  public static boolean isPrime(int n) {
    int k= 2;
    while (k * k <= n && n % k != 0)
      k++;
    return n >= 2 && k * k > n;
  }

// Sequential solution
  private static long countSequential(int range) {
    long count = 0;
    final int from = 0, to = range;
    for (int i=from; i<to; i++)
      if (isPrime(i)) count++;
    return count;
  }

  //Exercise 11.1.2
  private static long countIntStream(int range) {
    
    return IntStream.range(2, range)
      .filter(e -> isPrime(e))
      .count();
  }

  //Exercise 11.1.3
  private static void printPrimeStream(int range) {
    IntStream.range(2, range)
      .filter(e -> isPrime(e))
      .forEach(e-> System.out.println(e));
    
    return;
  }

  //Exercise 11.1.4
  private static long countParallel(int range) {
    return IntStream.range(2, range)
      .parallel()
      .filter(e -> isPrime(e))
      .count();
  }

  //Exercise 11.1.5
  private static long countparallelStream(List<Integer> list) {
    Stream<Integer> stream = list.parallelStream();
    Long count = stream
      .filter(e -> isPrime(e))
      .count();
    return count;
  }

  public PrimeCountingPerf() {
    Benchmark.Mark7("Sequential", i -> countSequential(range));

    Benchmark.Mark7("IntStream", i -> countIntStream(range));
    
    Benchmark.Mark7("Parallel", i -> countParallel(range));

    List<Integer> list = new ArrayList<Integer>();
    for (int i= 2; i< range; i++){ list.add(i); }
    Benchmark.Mark7("ParallelStream", i -> countparallelStream(list));
  }
}
