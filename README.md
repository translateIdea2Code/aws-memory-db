Setting up Amazon MemoryDB with Spring Boot: A Complete Guide
==========================================================
https://medium.com/@vinodbokare0588/ultra-fast-db-operation-complete-guide-with-spring-boot-5c38e4f6ebdd
Amazon MemoryDB for Redis is a durable, in-memory database service that delivers ultra-fast performance. In this guide, I'll walk you through setting up MemoryDB with a Spring Boot application, including security configurations and sample data loading.

Prerequisites
------------
Before we begin, make sure you have:
* AWS Account
* Basic knowledge of Spring Boot
* Java 11 or higher
* Maven or Gradle
* AWS CLI configured locally

1. Setting up Amazon MemoryDB
----------------------------

First, let's create and configure our MemoryDB cluster.

### Creating a MemoryDB Cluster

1. Navigate to AWS Console → Amazon MemoryDB
2. Click "Create cluster"
3. Configure the following basic settings:

```
Cluster name: hellomemorydb
Node type: db.t4g.small
Number of shards: 1
Engine version: 7.3
```

### Security Group Configuration

Security configuration is crucial for connectivity. Here's how to set it up:

1. Go to EC2 → Security Groups → Create Security Group
```
Name: memorydb-sg
Description: Security group for MemoryDB access
VPC: Select your VPC
```

2. Add Inbound Rules:
```
Type: Custom TCP
Port: 6379
Source: Your application's security group ID or IP range
Description: MemoryDB access
```

3. Attach to MemoryDB:
* Select your MemoryDB cluster
* Modify security groups
* Add the newly created security group

2. Spring Boot Configuration
--------------------------

### Adding Dependencies

Add these to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### Application Properties

Create or update your `application.properties`:

```properties
spring.redis.host=your-memorydb-endpoint
spring.redis.port=6379
spring.redis.ssl=true
```

### Redis Configuration

Create a new configuration class:

```java
@Configuration
public class MemoryDBConfig {
    
    @Value("${spring.redis.host}")
    private String host;
    
    @Value("${spring.redis.port}")
    private int port;
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = 
            new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        
        LettuceClientConfiguration clientConfig = 
            LettuceClientConfiguration.builder()
                .useSsl()
                .commandTimeout(Duration.ofSeconds(20))
                .build();
            
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(
            new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
```

3. Loading Sample Data
--------------------

Here's a utility class to load sample data:

```java
@Service
@Slf4j
public class DataLoader {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void loadUserData(int numberOfRecords) {
        for (int i = 0; i < numberOfRecords; i++) {
            String userId = "user:" + i;
            Map<String, String> userMap = new HashMap<>();
            userMap.put("name", "User " + i);
            userMap.put("email", "user" + i + "@example.com");
            userMap.put("age", String.valueOf(18 + (i % 50)));
            
            redisTemplate.opsForHash().putAll(userId, userMap);
        }
    }
}
```

4. Troubleshooting Common Issues
------------------------------

### Connection Issues

If you encounter connection timeouts, check these common causes:

1. Verify security group settings
2. Check if your application is in the same VPC
3. Ensure SSL is properly configured
4. Verify endpoint and port

### Security Group Checklist

✓ Inbound rule for port 6379 exists  
✓ Source IP/security group is correctly specified  
✓ Security group is attached to MemoryDB cluster  
✓ VPC settings are correct  

5. Best Practices
---------------

### Security Best Practices

* Use VPC endpoints when possible
* Implement proper access controls
* Regularly rotate credentials
* Enable encryption in transit (SSL/TLS)

### Performance Optimization

* Use appropriate node types
* Monitor memory usage
* Implement proper error handling
* Use connection pooling

6. Monitoring and Maintenance
---------------------------

### CloudWatch Metrics

Monitor these key metrics:
* DatabaseMemoryUsagePercentage
* CacheHits/CacheMisses
* NewConnections
* CPUUtilization

### Setting Up Alerts

Configure CloudWatch alarms for:
* High memory usage (>80%)
* Connection spikes
* Error rates

Conclusion
----------

Amazon MemoryDB provides a robust, Redis-compatible database service that can be easily integrated with Spring Boot applications. By following proper security configurations and best practices, you can build scalable, high-performance applications.

Resources
---------

* [AWS MemoryDB Documentation](https://docs.aws.amazon.com/memorydb/)
* [Spring Data Redis Documentation](https://docs.spring.io/spring-data/redis/docs/current/reference/html/)
* [AWS Security Best Practices](https://aws.amazon.com/security/security-learning/)

---

*Tags: AWS, MemoryDB, Spring Boot, Redis, Security Groups, Java, Cache, Database*
