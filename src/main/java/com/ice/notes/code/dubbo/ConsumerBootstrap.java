package com.ice.notes.code.dubbo;

import com.ice.notes.code.dubbo.action.GreetingServiceConsumer;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

public class ConsumerBootstrap {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        GreetingServiceConsumer greetingServiceConsumer = context.getBean(GreetingServiceConsumer.class);
        String hello = greetingServiceConsumer.doSayHello();
        System.out.println("result: " + hello);
    }

    @Configuration
    @ComponentScan(value = {"com.ice.notes.code.dubbo.action"})
    @EnableDubbo(scanBasePackages = "com.ice.notes.code.dubbo.action")
    @PropertySource("classpath:/dubbo/dubbo-consumer.properties")
    static class ConsumerConfiguration {

    }
}
