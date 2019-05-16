# Dubbo
## Dubbo 注册 Nacos
```
    <properties>
        <dubbo.version>2.7.1</dubbo.version>
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
    </dependencies>
```
