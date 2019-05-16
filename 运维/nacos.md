# Nacos

## Nacos 集群安装

### Github下载
`wget https://github.com/alibaba/nacos/releases/download/1.0.0/nacos-server-1.0.0.tar.gz`

### 解压安装
```
tar zxvf nacos-server-1.0.0.tar.gz
cp -R nacos /usr/local/server/nacos-server-1.0.0
```

### 配置集群配置文件
```
vim conf/cluster.conf

#it is ip
#example
172.18.1.152:8848
172.18.1.153:8848
172.18.1.154:8848
```

### 配置mysql数据库
```
create database nacos_config default character set utf8 collate utf8_bin;

执行sql脚本

grant all on nacos_config.* to 'nacos'@'%' identified by 'nacos2019';

flush privileges;
```

### application.properties 配置数据库链接
```
vi application.properties

# spring

server.contextPath=/
server.servlet.contextPath=/
server.port=8848

db.num=1
db.url.0=jdbc:mysql://172.18.156.16:4306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=nacos
db.password=nacos2019


nacos.cmdb.dumpTaskInterval=3600
nacos.cmdb.eventTaskInterval=10
nacos.cmdb.labelTaskInterval=300
nacos.cmdb.loadDataAtStart=false


# metrics for prometheus
#management.endpoints.web.exposure.include=*

# metrics for elastic search
management.metrics.export.elastic.enabled=false
#management.metrics.export.elastic.host=http://localhost:9200

# metrics for influx
management.metrics.export.influx.enabled=false
#management.metrics.export.influx.db=springboot
#management.metrics.export.influx.uri=http://localhost:8086
#management.metrics.export.influx.auto-create-db=true
#management.metrics.export.influx.consistency=one
#management.metrics.export.influx.compressed=true

server.tomcat.accesslog.enabled=true
server.tomcat.accesslog.pattern=%h %l %u %t "%r" %s %b %D %{User-Agent}i
# default current work dir
server.tomcat.basedir=

## spring security config
### turn off security
#spring.security.enabled=false
#management.security=false
#security.basic.enabled=false
#nacos.security.ignore.urls=/**

nacos.security.ignore.urls=/,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-fe/public/**,/v1/auth/login,/v1/console/health/**,/v1/cs/**,/v1/ns/**,/v1/cmdb/**,/actuator/**,/v1/con
sole/server/**

nacos.naming.distro.taskDispatchThreadCount=1
nacos.naming.distro.taskDispatchPeriod=200
nacos.naming.distro.batchSyncKeyCount=1000
nacos.naming.distro.initDataRatio=0.9
nacos.naming.distro.syncRetryDelay=5000
nacos.naming.data.warmup=true
nacos.naming.expireInstance=true

```

### 启动服务
`bin/startup.sh`

### 配置nginx
```
    upstream nacos-server {
        server 172.18.1.152:8848;
        server 172.18.1.153:8848;
        server 172.18.1.154:8848;
    }
    server {
        listen    80;
        server_name  nacos.ice-leaf.com;

        location / {
            proxy_pass http://nacos-server;
            proxy_set_header  Host  $host;
            proxy_set_header  X-Real-IP  $remote_addr;
            proxy_set_header  X-Forwared-For $proxy_add_x_forwarded_for;
        }
    }

```
