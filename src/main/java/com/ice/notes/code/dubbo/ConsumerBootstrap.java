package com.ice.notes.code.dubbo;

import com.ice.notes.code.dubbo.action.GreetingServiceConsumer;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StopWatch;

public class ConsumerBootstrap {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        GreetingServiceConsumer greetingServiceConsumer = context.getBean(GreetingServiceConsumer.class);
        StopWatch sw = new StopWatch();
//        for (int i = 0; i < 10; i++) {
//            sw.start();
//            greetingServiceConsumer.doSayHello();
//            sw.stop();
//        }

        greetingServiceConsumer.addListener();
        //System.out.println(sw.getTotalTimeMillis());
    }

    @Configuration
    @ComponentScan(value = {"com.ice.notes.code.dubbo.action"})
    @EnableDubbo(scanBasePackages = "com.ice.notes.code.dubbo.action")
    @PropertySource("classpath:/dubbo/dubbo-consumer.properties")
    static class ConsumerConfiguration {

    }
}
