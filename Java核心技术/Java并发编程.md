# 并发Bug的源头
核心矛盾在于CPU、内存、I/O设备的速度差异。  
为了合理利用CPU的高性能，平衡三者的速度差异：  
1. CPU 增加了缓存，以均衡与内存的速度差异；
2. 操作系统增加了进程、线程，以分时复用 CPU，进而均衡 CPU 与 I/O 设备的速度差异；
3. 编译程序优化指令执行次序，使得缓存能够得到更加合理地利用。



## 源头之一：缓存导致的可见性问题
一个线程对共享变量的修改，另外一个线程能够立刻看到，我们称为**可见性**。

![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190613/26wHwGbSupfO.png?imageslim)

线程A对变量V的操作对于线程B而言就不具备可见性。

## 源头之二：线程切换带来的原子性问题
我们把一个或多个操作在CPU执行的过程中不被中断的特性称为**原子性**。

高级语言里的一条语句往往需要多条CPU指令完成，例如 `count += 1`，至少需要三条CPU指令。
- 指令 1：首先，需要把变量 count 从内存加载到 CPU 的寄存器；
- 指令 2：之后，在寄存器中执行 +1 操作；
- 指令 3：最后，将结果写入内存（缓存机制导致可能写入的是 CPU 缓存而不是内存）。
而操作系统做任务切换，可以发生在任何一条**CPU指令**执行完，而不是高级语言里的一条语句。

![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190613/twvANvmYc0FL.png?imageslim)

## 源头之三：编译优化带来的有序性问题

双重检查创建单例对象案例：
```java
public class Singleton {
  static Singleton instance;
  static Singleton getInstance(){
    if (instance == null) {
      synchronized(Singleton.class) {
        if (instance == null)
          instance = new Singleton();
        }
    }
    return instance;
  }
}
```

假设有两个线程 A、B 同时调用 getInstance() 方法，他们会同时发现 `instance == null` ，于是同时对 Singleton.class 加锁，此时 JVM 保证只有一个线程能够加锁成功（假设是线程 A），另外一个线程则会处于等待状态（假设是线程 B）；线程 A 会创建一个 Singleton 实例，之后释放锁，锁释放后，线程 B 被唤醒，线程 B 再次尝试加锁，此时是可以加锁成功的，加锁成功后，线程 B 检查 `instance == null`  时会发现，已经创建过 Singleton 实例了，所以线程 B 不会再创建一个 Singleton 实例。

我们以为的new操作应该是：
1. 分配一块内存 M；
2. 在内存 M 上初始化 Singleton 对象；
3. 然后 M 的地址赋值给 instance 变量。

但是实际上优化后的执行路径却是这样的：
1. 分配一块内存 M；
2. 将 M 的地址赋值给 instance 变量；
3. 最后在内存 M 上初始化 Singleton 对象。

![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190613/ygdirenM1hMT.png?imageslim)

# 解决可见性和有序性问题
导致可见性的原因是缓存，导致有序性的原因是编译优化，所以最直接的办法就是**禁用缓存和编译优化**（程序性能就堪忧了）。  
合理的方案是**按需禁用缓存以及编译优化**。

## volatile
最原始的意义就是禁用CPU缓存。

## Happens-Before 规则
**前面一个操作的结果对后续操作是可见的。**  
1. 在一个线程中，按照程序顺序，前面的操作 Happens-Before 于后续的任意操作。
2. 对于一个 volatile 变量的写操作，Happens-Before 于后续对这个 volatile 变量的读操作。
3. 如果 A Happens-Before B，且 B Happens-Before C，那么 A Happens-Before C。

![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190613/ml8RGWltuC0r.png?imageslim)
“x=42” Happens-Before 写变量 “v=true” ，这是规则 1 的内容；  
写变量“v=true” Happens-Before 读变量 “v=true”，这是规则 2 的内容 。
根据规则 3 的内容，如果线程 B 读到了“v=true”，那么线程 A 设置的“x=42”对线程 B 是可见的。

4. 对一个锁的解锁 Happens-Before 于后续对这个锁的加锁。
5. 主线程 A 启动子线程 B 后，子线程 B 能够看到主线程在启动子线程 B 前的操作。或者说，如果线程 A 调用线程 B 的 start() 方法（即在线程 A 中启动线程 B），那么该 start() 操作 Happens-Before 于线程 B 中的任意操作。
6. 主线程 A 等待子线程 B 完成（主线程 A 通过调用子线程 B 的 join() 方法实现），当子线程 B 完成后（主线程 A 中 join() 方法返回），主线程能够看到子线程的操作。当然所谓的“看到”，指的是对**共享变量**的操作。或者说，如果在线程 A 中，调用线程 B 的 join() 并成功返回，那么线程 B 中的任意操作 Happens-Before 于该 join() 操作的返回。

## final
final 修饰变量时，初衷是告诉编译器：这个变量生而不变，可以可劲儿优化。

# 解决原子性问题
原子性问题的源头是**线程切换**。

## synchronize

> 当修饰静态方法时，锁定的是当前类的 Class 对象；
> 当修饰非静态方法的时候，锁定的是当前实例对象 this。

```java
class X {
  // 修饰静态方法
  synchronized(X.class) static void bar() {
    // 临界区
  }
}
```

```java
class X {
  // 修饰非静态方法
  synchronized(this) void foo() {
    // 临界区
  }
}
```

**锁和受保护资源的关系：受保护资源和锁之间的关联关系是 N:1 的关系。**
```java
class SafeCalc {
  static long value = 0L;
  synchronized long get() {
    return value;
  }
  synchronized static void addOne() {
    value += 1;
  }
}
```

![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190617/HEicy1QE06wD.png?imageslim)


```java
class Account {
  private int balance;
  // 转账
  synchronized void transfer(
      Account target, int amt){
    if (this.balance > amt) {
      this.balance -= amt;
      target.balance += amt;
    }
  } 
}
```
看似正确，但是 this 这把锁可以保护自己的余额 this.balance，却保护不了别人的余额 target.balance。

## 死锁
**细粒度锁：用不同的锁对受保护资源进行精细化管理，能够提升性能。**  
但是使用细粒度锁是有代价的，这个代价就是可能会导致死锁。  

**死锁：一组互相竞争资源的线程因互相等待，导致“永久”阻塞的现象。**  
解决死锁最好的办法还是规避死锁。

只有以下四个条件都发生时才会出现死锁：
1. 互斥，共享资源 X 和 Y 只能被一个线程占用；
2. 占用且等待，线程 T1 已经取得共享资源 X，在等待共享资源 Y 的时候，不释放共享资源 X；
3. 不可抢占，其他线程不能强行抢占线程 T1 占用的资源；
4. 循环等待，线程 T1 等待线程 T2 占用的资源，线程 T2 等待线程 T1 占用的资源，就是循环等待。

**只要破坏其中一个，就可以成功避免死锁的发生。**

互斥这个条件我们没法破坏，因为我们用锁为的就是互斥。


### 1.破坏占用且等待条件
理论上要破坏这个条件，可以一次性申请所有资源。  
**等待-通知机制：** 如果线程要求的条件不满足，则线程阻塞自己，进入等待状态；当线程要求的条件满足后，通知等待的线程重新执行。   
wait(), notify(), notifyAll()  方法操作的等待队列是互斥锁的等待队列，所以如果 synchronized 锁定的是 this，那么对应的一定是 this.wait(), this.notify(), this.notifyAll()。    
**尽量使用 notifyAll()，notify() 是会随机地通知等待队列中的一个线程（风险在于可能导致某些线程永远不会被通知到），而 notifyAll() 会通知等待队列中的所有线程。**


### 2.破坏不可抢占条件
核心就是要能够主动释放它占有的资源。这一点 synchronized 是做不到的。原因是 synchronized 申请资源的时候，如果申请不到，线程直接进入阻塞状态了，而线程进入阻塞状态，啥都干不了，也释放不了线程已经占有的资源。

### 3.破坏循环等待条件
破坏这个条件，需要对资源进行排序，然后按序申请资源。