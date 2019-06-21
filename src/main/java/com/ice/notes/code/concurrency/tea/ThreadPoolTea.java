package com.ice.notes.code.concurrency.tea;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 利用线程池实现泡茶
 *
 * @author : ACE
 * @date : 2019/6/21
 */
@Slf4j
public class ThreadPoolTea {

    // 由于 Executors 默认使用的无边界队列，高负载下容易OOM，不推荐使用
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    void makeTea() {
        Future f1 = executorService.submit(() -> {
            try {
                log.info("T1: 洗水壶...");
                TimeUnit.SECONDS.sleep(1);
                log.info("T1: 烧开水...");
                TimeUnit.SECONDS.sleep(3);
                log.info("开水准备！！！");
            } catch (InterruptedException ie) {
                log.error("T1: 异常中断...");
                log.error(ie.getMessage());
            }
        });

        Future f2 = executorService.submit(() -> {
            try {
                log.info("T2: 洗茶壶");
                TimeUnit.SECONDS.sleep(1);
                log.info("T2: 洗茶杯...");
                TimeUnit.SECONDS.sleep(1);
                log.info("T2: 拿茶叶...");
                TimeUnit.SECONDS.sleep(1);
                log.info("茶叶准备：普洱！！！");
            } catch (InterruptedException ie) {
                log.error("T2: 异常中断...");
                log.error(ie.getMessage());
            }
        });

        try {
            f1.get();
            f2.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("T1/T2: 异常中断...");
            log.error(e.getMessage());
        }

        log.info("T1&T2: 泡茶...");
        log.info("上茶：普洱！！！");
    }


    public static void main(String[] args) {
        ThreadPoolTea threadPoolTea = new ThreadPoolTea();
        threadPoolTea.makeTea();
    }

}
