package com.ice.notes.code.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockedQueue<T> {

    private final Lock lock = new ReentrantLock();

    private final Condition notFull = lock.newCondition();

    private final Condition notEmpty = lock.newCondition();

    private List<T> list = new ArrayList<>(10);

    public void enq(T x) {
        lock.lock();
        try {
            while (list.size() == 10) {
                notFull.await();
            }
            notEmpty.signalAll();
        }
        catch (InterruptedException e){

        }finally {
            lock.unlock();
        }
    }
}
