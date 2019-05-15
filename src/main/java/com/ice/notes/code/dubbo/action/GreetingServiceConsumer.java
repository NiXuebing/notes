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

package com.ice.notes.code.dubbo.action;

import com.ice.notes.code.dubbo.api.CallbackListener;
import com.ice.notes.code.dubbo.api.GreetingService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class GreetingServiceConsumer {

    @Value("${dubbo.application.name}")
    private String applicationName;

    @Reference(version = "1.0.0", url = "dubbo://localhost:20880", timeout = 5000, lazy = true)
    private GreetingService greetingService;

    public String doSayHello() {
//        EchoService echoService = (EchoService) greetingService;
//        String status = (String) echoService.$echo("OK");
//        assert (status.equals("OK"));
        RpcContext rpcContext = RpcContext.getContext();
        rpcContext.setAttachment("sync-consumer-key", "Sync Attachment");
        greetingService.sayHello("sync call request");


        //boolean isConsumerSide = rpcContext.isConsumerSide();
        // 获取最后一次调用的提供方IP地址
        //String serverIP = rpcContext.getRemoteHost();
        // 获取当前服务配置信息，所有配置信息都将转换为URL的参数
        //String application = rpcContext.getUrl().getParameter("application");


        // 调用直接返回CompletableFuture
        rpcContext.setAttachment("async-consumer-key", "aSync Attachment");
        CompletableFuture<String> completableFuture = greetingService.aSyncSayHello("async call request");

        // 增加回调
        completableFuture.whenComplete((retValue, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("completableFuture Response: " + retValue);
            }
        });

        CompletableFuture<String> future = RpcContext.getContext().asyncCall(
                () -> greetingService.sayHello("async call request")
        );

        future.whenComplete((retValue, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("future Response: " + retValue);
            }
        });

        // 早于结果输出
        System.out.println("Executed before response return.");
        return "Success";
    }

    public String addListener() {
        greetingService.addListener("foo.bar", new CallbackListener(){
            @Override
            public void changed(String msg) {
                System.out.println("callback1:" + msg);
            }
        });

        return "Success";
    }

}
