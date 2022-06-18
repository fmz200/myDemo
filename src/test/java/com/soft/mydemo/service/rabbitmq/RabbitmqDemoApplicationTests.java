package com.soft.mydemo.service.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqDemoApplicationTests {
    @Autowired
    private MsgProducer msgProducer;

    @Test
    public void test() {
        msgProducer.sendMsg("hello,this is my msg");
    }
}

