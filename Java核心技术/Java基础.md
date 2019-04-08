# Java 基础

## Exception 和 Error 的区别
都继承了 Throwable 类，可以被抛出（throw）和捕获（catch）。  
Exception 是程序正常运行中，可以预料的意外情况，可能并且应该被捕获，进行相应处理。  
Error 是指正常情况下，不大可能出现的情况，所以不便于也不需要被捕获。  

![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190408/DVGUuTweBbS6.png?imageslim)

**尽量不用捕获类似 Exception 这样的通用异常，而是应该捕获特定异常。**  
**不要生吞异常。**

## final、finally、finalize
final 不等同于 immutable，`final List<String> strList = new ArrayList<>()` final 只能约束 strList 这个引用不可以被赋值，但是 strList 对象行为不被 final 影响，添加元素等操作是完全正常的。   

需要关闭的连接等资源，更推荐使用 Java 7 中添加的 try-with-resources 语句，通常 Java 平台能够更好地处理异常情况，编码量也要少很多。

finalize 被设计为在对象被垃圾收集前调用。现在已经不推荐使用，并且在 JDK 9 开始被标记为 deprecated。

## 强引用、软引用、弱引用、幻想引用
不同的引用类型，主要体现的是**对象不同的可达性状态和对垃圾收集的影响**。

强引用：普通对象引用，只有超过了引用的作用域或者显示地将相应的强引用赋值为 null ，就是可以被垃圾收集了。

软引用：可以让对象豁免一些垃圾回收，只有当 JVM 认为内存不足时，才会去试图回收软引用指向的对象。JVM 会确保在抛出 OutOfMemoryError 之前，清理软引用指向的对象。可用于实现内存敏感的缓存。

弱引用：不能使对象豁免垃圾收集，可以用来构建一种没有特定约束的关系。

幻想引用：提供一种确保对象被 finalize 以后，做某些事情的机制。

![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190408/z307wVVfMJP4.png?imageslim)

