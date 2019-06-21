package com.ice.notes.code.concurrency.tea;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 利用多线程实现泡茶
 *
 * @author : ACE
 * @date : 2019/6/21
 */
@Slf4j
public class ThreadTea {

    void makeTea() {
        Thread t1 = new Thread(() -> {
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

        Thread t2 = new Thread(() -> {
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

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ie) {
            log.error("T1/T2: 异常中断...");
            log.error(ie.getMessage());
        }

        log.info("T1&T2: 泡茶...");
        log.info("上茶：普洱！！！");
    }

    public static void main(String[] args) {
        ThreadTea threadTea = new ThreadTea();

        threadTea.makeTea();
    }
}
