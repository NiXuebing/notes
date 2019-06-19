package com.ice.notes.code.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.*;

public class BlockedQueue<T> {

    private final Lock lock = new ReentrantLock();

    private final Condition notFull = lock.newCondition();

    private final Condition notEmpty = lock.newCondition();

    private List<T> list = new ArrayList<>(10);

    final StampedLock sl = new StampedLock();


    public void enq(T x) {
        lock.lock();
        try {
            while (list.size() == 10) {
                notFull.await();
            }
            notEmpty.signalAll();
        } catch (InterruptedException e) {

        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        try {
            final StampedLock lock = new StampedLock();

            Thread T1 = new Thread(() -> {
                // 获取写锁
                lock.writeLock();
                // 永远阻塞在此处，不释放写锁
                LockSupport.park();
            });
            T1.start();
            // 保证 T1 获取写锁
            Thread.sleep(100);
            Thread T2 = new Thread(() ->
                    // 阻塞在悲观读锁
                    lock.readLock()
            );
            T2.start();
            // 保证 T2 阻塞在读锁
            Thread.sleep(100);
            // 中断线程 T2
            // 会导致线程 T2 所在 CPU 飙升
            T2.interrupt();
            T2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
