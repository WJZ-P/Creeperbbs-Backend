package me.wjz.creeperhub;

import me.wjz.creeperhub.utils.SnowFlake;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class SnowFlakeTest {
    private ExecutorService executorService = Executors.newFixedThreadPool(500);//新建500线程
    @Autowired
    private SnowFlake snowFlake;

    @Test
    public void testNextId() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(300);
        //创建线程任务
        Runnable task = () -> {
            for (int i = 0; i < 2000; i++) {
                long id = snowFlake.nextId();
//                System.out.println("id = " + id);
            }
            countDownLatch.countDown();
        };
        long begin = System.currentTimeMillis();
        //创建500个线程，每个线程2000个id，总共就是一百万个ID。
        for (int i = 0; i < 500; i++) executorService.submit(task);
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("生成百万个id耗费时间：" + (end - begin)+"ms");
    }
}
