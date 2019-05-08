package com.ice.notes.code.basic;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy {

    public static void main(String[] args) {
        HelloImpl hello = new HelloImpl();
        MyInvocationHandler handler = new MyInvocationHandler(hello);
        Hello proxyHello = (Hello)Proxy.newProxyInstance(HelloImpl.class.getClassLoader(), HelloImpl.class.getInterfaces(), handler);
        proxyHello.sayHello();

        // CGLIB 动态代理调用
        Panda panda = new Panda();

        CglibProxy proxy = new CglibProxy();
        Panda proxyPanda = (Panda)proxy.getInstance(panda);
        proxyPanda.eat();

        int i = -16;
        System.out.println(i >>> 12);
    }
}

interface Hello {
    void sayHello();
}

class HelloImpl implements Hello {

    @Override
    public void sayHello() {
        System.out.println("Hello World");
    }
}

class MyInvocationHandler implements InvocationHandler {
    private Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Invoking sayHello");
        Object result = method.invoke(target, args);
        return result;
    }
}



class Panda {
    public void eat() {
        System.out.println("The panda is eating");
    }
}

class CglibProxy implements MethodInterceptor {
    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        // 设置父类为实例类
        enhancer.setSuperclass(this.target.getClass());
        // 回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("调用前");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("调用后");
        return result;
    }
}




