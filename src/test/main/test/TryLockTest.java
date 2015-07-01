package test;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Test tryLock.
 */
public class TryLockTest {
    //http://robaustin.wikidot.com/reentrantlock
    final Lock lock = new ReentrantLock();

    @Test
    public void testTryLock() throws InterruptedException {
        Thread t1 = new Thread(newRunnable(), "thread1");
        Thread t2 = new Thread(newRunnable(), "thread2");
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private Runnable newRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        //尝试获得锁，最多等500ms。没得得到就直接返回false.
                        if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                            try {
                                System.out.println("locked thread "
                                        + Thread.currentThread().getName());

                                Thread.sleep(1000);

                            } finally {
                                lock.unlock();
                                System.out.println("unlocked locked thread " + Thread.currentThread().getName());
                            }
                            break;
                        } else {
                            System.out.println("cannot get lock after 500ms: " + Thread.currentThread().getName() + ", will retry again.");
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } while (true);
            }
        };
    }
}
