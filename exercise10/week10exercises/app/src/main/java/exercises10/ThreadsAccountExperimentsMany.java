// jst@itu.dk * 08/20/2025
package exercises10;
import java.util.Random;
import java.util.concurrent.*;

public class ThreadsAccountExperimentsMany {
    static final int N = 10; 
    static final int NO_TRANSACTION = 5;
    static final int NO_THREADS = 10;
    static final Account[] accounts = new Account[N];
    
    // THREAD VERSION:
    // static final Thread[] threads = new Thread[NO_THREADS];
    
    // EXECUTOR VERSION
    static ExecutorService pool;
    static Future<?>[] futures = new Future<?>[NO_THREADS];
    
    static Random rnd = new Random();
    
    public static void main(String[] args) { 
        new ThreadsAccountExperimentsMany(); 
    }
    
    public ThreadsAccountExperimentsMany() {
        for (int i = 0; i < N; i++) {
            accounts[i] = new Account(i);
        }
        
        // EXECUTOR VERSION
        pool = new ForkJoinPool(NO_THREADS);
        System.out.println("Starting with ForkJoinPool (" + NO_THREADS + " threads)\n");
        
        for (int i = 0; i < NO_THREADS; i++) {
            // THREAD VERSION:
            // try { 
            //     (threads[i] = new Thread(() -> doNTransactions(NO_TRANSACTION))).start();
            // } catch (Error ex) {
            //     System.out.println("At i = " + i + " I got error: " + ex);
            //     System.exit(0);
            // }
            
            // EXECUTOR VERSION
            final int taskId = i;
            futures[i] = pool.submit(() -> {
                System.out.println("→ Task " + taskId + " started on " 
                                 + Thread.currentThread().getName());
                doNTransactions(NO_TRANSACTION);
                System.out.println("← Task " + taskId + " completed");
            });
        }
        
        // THREAD VERSION:
        // for (int i = 0; i < NO_THREADS; i++) {
        //     try { threads[i].join(); } catch (Exception dummy) {}
        // }
        System.out.println("✓ All threads completed");
        
        // EXECUTOR VERSION
        System.out.println("\n--- Initiating shutdown ---");
        
        pool.shutdown();
        System.out.println("Executor shutdown initiated");
        
        try {
            System.out.println("Waiting for all tasks to complete...");
            for (int i = 0; i < NO_THREADS; i++) {
                futures[i].get();  // Blocks until task i completes
            }
            System.out.println("✓ All tasks completed via Future.get()");
            
            if (pool.awaitTermination(60, TimeUnit.SECONDS)) {
                System.out.println("✓ Executor terminated successfully");
            } else {
                System.out.println("⚠ Timeout: Forcing shutdown...");
                pool.shutdownNow();
            }
            
        } catch (InterruptedException e) {
            System.err.println("⚠ Main thread interrupted");
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.err.println("⚠ Task execution failed: " + e.getCause());
            pool.shutdownNow();
        }
        
        System.out.println("\n✓ Program completed - main thread exiting");
    }
    
    private static void doNTransactions(int noTransactions) {
        for (int i = 0; i < noTransactions; i++) {
            long amount = rnd.nextInt(5000) + 100;
            int source = rnd.nextInt(N);
            int target = (source + rnd.nextInt(N - 2) + 1) % N; // make sure target <> source
            doTransaction(new Transaction(amount, accounts[source], accounts[target]));
        }
    }
    
    private static void doTransaction(Transaction t) {
        // THREAD VERSION:
        // System.out.println(t);
        
        // EXECUTOR VERSION 
        System.out.println(t + " [" + Thread.currentThread().getName() + "]");
        
        t.transfer();
    }
    
    static class Transaction {
        final Account source, target;
        final long amount;
        
        Transaction(long amount, Account source, Account target) {
            this.amount = amount;
            this.source = source;
            this.target = target;
        }
        
        public void transfer() {
            // SAFE VERSION (prevents deadlock by lock ordering):
            Account min = accounts[Math.min(source.id, target.id)];
            Account max = accounts[Math.max(source.id, target.id)];
            
            // UNSAFE VERSION (can deadlock):
            // Account min = accounts[source.id];
            // Account max = accounts[target.id];
            
            synchronized(min) {
                synchronized(max) {
                    source.withdraw(amount);
                    try { Thread.sleep(50); } catch (Exception e) {}; // Simulate transaction time
                    target.deposit(amount);
                }
            }
        }
        
        public String toString() {
            return "Transfer " + amount + " from " + source.id + " to " + target.id;
        }
    }
    
    static class Account {
        // should have transaction history, owners, account-type, and 100 other real things
        public final int id;
        private long balance = 0;
        
        Account(int id) { this.id = id; }
        
        public void deposit(long sum) { balance += sum; } 
        public void withdraw(long sum) { balance -= sum; }
        public long getBalance() { return balance; }
    }
}