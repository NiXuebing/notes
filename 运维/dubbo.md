# Dubbo
## Apache Dubbo 服务注册 Nacos

### POM 配置
```
    <properties>
        <dubbo.version>2.7.1</dubbo.version>
        <nacos.client.version>1.0.0</nacos.client.version>
    </properties>

    <dependencies>
        <!-- Apache Dubbo SpringBoot -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
            <version>${dubbo.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>${dubbo.version}</version>
        </dependency>
        <!-- Dubbo Nacos registry dependency -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-registry-nacos</artifactId>
            <version>${dubbo.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba.nacos</groupId>
            <artifactId>nacos-client</artifactId>
            <version>${nacos.client.version}</version>
        </dependency>
    </dependencies>
```

### application.yml 配置
```
# dubbo 配置
dubbo:
  application:
    name: soa-provider
  protocol:
    name: dubbo
    port: 10022
  registry:
    address: nacos://ip1:8848,ip2:8848,ip3:8848
  provider:
    group: local
```

###  BootStrap 配置
```java
@SpringBootApplication
@ComponentScan("com.tv")
@EnableDubbo(scanBasePackages = "com.tv.soa.api.impl")
public class SoaProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoaProviderApplication.class, args);
    }
}
```

## Alibaba Dubbo 服务注册 Nacos
### POM 配置
引入spring-boot-starter-web，没有web环境dubbo在spring启动后自动shutdown
### 
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <exclusions> <!-- 去除默认配置logback -->
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Dubbo Nacos registry dependency -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo-registry-nacos</artifactId>
            <version>0.0.1</version>
        </dependency>
        <!-- Dubbo dependency -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>2.6.5</version>
        </dependency>
        <!-- Alibaba Spring Context extension -->
        <dependency>
            <groupId>com.alibaba.spring</groupId>
            <artifactId>spring-context-support</artifactId>
            <version>1.0.2</version>
        </dependency>
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-all</artifactId>
            <version>4.1.34.Final</version>
        </dependency>
```


### application.yml 配置
多地址第一个后面以?分隔，其他同上
```
# dubbo 配置
dubbo:
  application:
    name: soa-provider
  protocol:
    name: dubbo
    port: 10022
  registry:
    address: nacos://ip1:8848?ip2:8848,ip3:8848
  provider:
    group: local
```