# 基础架构

![img](https://static001.geekbang.org/resource/image/0d/d9/0d2070e8f84c4801adbfa03bda1f98d9.png)

## 连接器

```
mysql -h$ip -P$port -u$user -p
```

MySQL 在执行过程中临时使用的内存是管理在连接对象里面的。这些资源会在连接断开的时候才释放。如果长连接积累下来，可能导致内存占用过大，被系统强行杀掉(OOM)，表现为 MySQL 异常重启。



## 查询缓存

之前执行过的语句及其结果可能会以 key - value 对的形式，被缓存在内存中。  

**但大多数情况下，不建议使用查询缓存，因为查询缓存的失效非常频繁。只有有对一个表的更新，这个表上的所有查询缓存都会被清空。**  

*MySQL 8.0 版本直接将查询缓存的整块功能删除了。*



## 分析器

词法分析 （识别 select，表名，列名等） - 语法分析（判断 SQL 是否满足 MySQL 语法）。



## 优化器

优化器是在表里有多个索引的时候，决定用哪个索引；或者在一个语句有多表关联（join）的时候，决定各个表的连接顺序。



## 执行器

根据表的引擎定义，选择对应的引擎提供的接口打开表进行查询。



# 日志模块

## redo log 重做日志

当有一条记录需要更新的时候，InnoDB 引擎就会先把这条记录写到 redo log 里面，并更新内存，这个时候更新就算完成了。同时，InnoDB 引擎会在系统比较空闲的时候，将这个操作记录更新到磁盘里面。

![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190627/Rilf0qvrBYO8.png?imageslim)

**redo log 是 InnoDB 引擎特有的循环记录的物理日志，存储引擎层的日志。保证了即使数据库异常重启，之前提交的记录也不会丢失，即 crash-safe。**



## binlog

binlog 是 MySQL 的 Serve 层实现的，所有引擎都可以使用。  

binlog 是逻辑日志，记录的是语句的原始逻辑，并追加写入，不会覆盖以前的日志。



![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190627/QEMuxolF71ga.png?imageslim)



redo log 有 prepare 和 commit 两个步骤，进行了两阶段提交，可以保证两份日志的一致性。

