# Spring Boot Admin
## SpringBoot Admin Server
### 新建项目
![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190516/LLf8nFdg1HNh.png?imageslim)
![mark](http://pic-cloud.ice-leaf.top/pic-cloud/20190516/IbIWMhjsm7P5.png?imageslim)

### 修正pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.ice-leaf</groupId>
    <artifactId>springboot-admin-server</artifactId>
    <version>1.0.0</version>
    <name>spring-boot-admin-server</name>
    <description>Spring Boot Admin Server Project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-boot-admin.version>2.1.4</spring-boot-admin.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-dependencies</artifactId>
                <version>${spring-boot-admin.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 添加@EnableAdminServer
```java
@EnableAdminServer
@SpringBootApplication
public class SpringbootAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAdminApplication.class, args);
    }
}
```