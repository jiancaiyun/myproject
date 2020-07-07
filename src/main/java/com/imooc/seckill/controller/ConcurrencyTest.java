package com.imooc.seckill.controller;

import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.NotThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@NotThreadSafe
public class ConcurrencyTest {
    public static int clientTotal=5000;
    public static int threadTotal=200;
    public static int count=0;

    public static void main(String[] args)throws  Exception {
        ExecutorService executorService= Executors.newCachedThreadPool();
        final Semaphore semaphore= new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for(int i =0;i< clientTotal;i++){
            executorService.execute(() ->{
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.info("count:",count);
                }
                countDownLatch.countDown();

            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("count",count);
    }

    public static void add(){
        count++;
    }
}
