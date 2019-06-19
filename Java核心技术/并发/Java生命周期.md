Java语言中线程共用六种状态，分别是：
1. NEW（初始化状态）
2. RUNNABLE （可运行/运行状态）
3. BLOCKED （阻塞状态）
4. WAITING （无时限等待）
5. TIMED_WAITING （有时限等待）
6. TERMINATED （终止状态）



![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190617/LJLS86afRhQp.png?imageslim)

## 1、RUNNABLE 与 BLOCKED 的状态转换
只有一种情况会触发，就是线程等待和获取 synchronized 的隐式锁。

## 2、RUNNABLE 与 WAITING 的状态转换
1. 获得 synchronized 隐式锁的线程，调用无参数的 Object.wait()
2. 调用无参数的 Thread.join() 方法，等待中的线程其状态会从 RUNNABLE 转换到 WAITING
3. 调用 LockSupport.park() 

## 3、RUNNABLE 与 TIMED_WAITING 的状态转换
五种场景方法：
Thread.sleep(long millis)  
Object.wait(long timeout)  
Thread.join(long millis)  
LockSupport.parkNanos(Object blocker, long deadline)  
LockSupport.parkUntil(long deadline)

## 4、从 NEW 到 RUNNABLE
通过 Thread.start() 实现