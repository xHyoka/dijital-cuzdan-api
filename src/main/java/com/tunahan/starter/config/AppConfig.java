    package com.tunahan.starter.config;

    import org.springframework.cache.annotation.EnableCaching;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.data.redis.cache.RedisCacheConfiguration;
    import org.springframework.data.redis.cache.RedisCacheManager;
    import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
    import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
    import org.springframework.data.redis.core.RedisTemplate;
    import redis.clients.jedis.Jedis;

    import java.time.Duration;

    @Configuration
    @EnableCaching
    public class AppConfig {

        @Bean
        public JedisConnectionFactory jedisConnectionFactory(){
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
            redisStandaloneConfiguration.setHostName("redis");
            redisStandaloneConfiguration.setPort(6379);
            return new JedisConnectionFactory(redisStandaloneConfiguration);
        }


        @Bean
        public RedisTemplate redisTemplate(){
            RedisTemplate template = new RedisTemplate<>();
            template.setConnectionFactory(jedisConnectionFactory());
            return template;
        }

        @Bean
        public RedisCacheManager cacheManager(JedisConnectionFactory jedisConnectionFactory) {
            RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(60)) // Cache'in ömrü
                    .disableCachingNullValues();

            return RedisCacheManager.builder(jedisConnectionFactory)
                    .cacheDefaults(cacheConfig)
                    .build();
        }
    }
