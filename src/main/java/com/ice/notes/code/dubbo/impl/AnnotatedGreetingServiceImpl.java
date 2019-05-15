/*
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.ice.notes.code.dubbo.impl;

import com.ice.notes.code.dubbo.api.CallbackListener;
import com.ice.notes.code.dubbo.api.GreetingService;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service(version = "1.0.0")
//@Service(version = "1.0.0", cache = "lru")
public class AnnotatedGreetingServiceImpl implements GreetingService {

    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    public AnnotatedGreetingServiceImpl() {
        Thread t = new Thread(() -> {
            while(true) {
                try {
                    for(Map.Entry<String, CallbackListener> entry : listeners.entrySet()){
                        try {
                            entry.getValue().changed(getChanged(entry.getKey()));
                        } catch (Throwable t1) {
                            listeners.remove(entry.getKey());
                        }
                    }
                    // 定时触发变更通知
                    Thread.sleep(2000);
                } catch (Throwable t1) {
                    // 防御容错
                    t1.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }


    @Override
    public String sayHello(String name) {

        String key = RpcContext.getContext().getAttachment("sync-consumer-key");
        System.out.println("RpcContext getAttachment: " + key);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello, " + name;
    }

    @Override
    public CompletableFuture<String> aSyncSayHello(String name) {
        // 建议为supplyAsync提供自定义线程池，避免使用JDK公用线程池
        return CompletableFuture.supplyAsync(() -> {
            String key = RpcContext.getContext().getAttachment("async-consumer-key");
            System.out.println("RpcContext getAttachment: " + key);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello, " + name;
        });
    }

    @Override
    public void addListener(String key, CallbackListener listener) {
        // 发送变更通知
        listeners.put(key, listener);
        //listener.changed(getChanged(key));
    }

    private String getChanged(String key) {
        return "Changed: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
