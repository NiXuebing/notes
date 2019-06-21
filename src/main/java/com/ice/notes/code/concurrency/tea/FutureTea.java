package com.ice.notes.code.concurrency.tea;

import java.util.concurrent.*;

/**
 * 利用Future进行主子线程通信
 *
 * @author : ACE
 * @date : 2019/6/21
 */
public class FutureTea {

    private ThreadPoolExecutor tpExecutor;

    FutureTea(){}

    public FutureTea(BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory){
        tpExecutor = new ThreadPoolExecutor(1, 10, 10, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    class MyThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread("FutureTea Thread");
        }
    }


    public static void main(String[] args) {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2);
        MyThreadFactory mt = new FutureTea().new MyThreadFactory();
        FutureTea futureTea = new FutureTea(workQueue, mt);
    }
}


