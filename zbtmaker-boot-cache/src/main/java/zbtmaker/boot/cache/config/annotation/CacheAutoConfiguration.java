package zbtmaker.boot.cache.config.annotation;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import zbtmaker.boot.common.util.JacksonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zoubaitao
 * date 2022/07/10
 */
@Configuration
@EnableCaching
public class CacheAutoConfiguration {

    private static final String CACHE_PREFIX = "app.cache";

    @Autowired
    private Environment environment;

    @SuppressWarnings("unchecked")
    @Bean
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        Map<String, CaffeineCache> cacheMap = new HashMap<>();
        AbstractEnvironment abstractEnvironment = (AbstractEnvironment) environment;
        // 这里是为了获取项目中所有prefix为前缀的key，但是如果某个公司有公共的东西，那么就可以
        for (PropertySource<?> source : abstractEnvironment.getPropertySources()) {
            Object o = source.getSource();
            if (o instanceof Map) {
                for (Map.Entry<String, Object> entry : (((Map<String, Object>) o).entrySet())) {
                    String key = entry.getKey();
                    if (key.startsWith(CACHE_PREFIX)) {
                        String cacheName = key.substring(CACHE_PREFIX.length() + 1);
                        CacheConfig cacheConfig = JacksonUtils.parseObject(entry.getValue().toString(), CacheConfig.class);
                        if (cacheConfig == null) {
                            continue;
                        }
                        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();

                        caffeine.maximumSize(cacheConfig.maximumSize);
                        // 设置afterWriter参数
                        if (cacheConfig.expireAfterWrite != null) {
                            caffeine.expireAfterWrite(cacheConfig.expireAfterWrite, TimeUnit.SECONDS);
                        }
                        // 设置afterAccess参数
                        if (cacheConfig.expireAfterAccess != null) {
                            caffeine.expireAfterAccess(cacheConfig.expireAfterAccess, TimeUnit.SECONDS);
                        }
                        CaffeineCache caffeineCache = new CaffeineCache(cacheName, caffeine.build());
                        cacheMap.put(cacheName, caffeineCache);
                    }
                }
            }
        }
        cacheManager.setCaches(cacheMap.values());
        return cacheManager;
    }

    private static class CacheConfig {
        /**
         * write后过期
         */
        private Integer expireAfterWrite;

        /**
         * access后过期
         */
        private Integer expireAfterAccess;

        /**
         * 缓存最大值
         */
        private Integer maximumSize;

        /**
         * 是否上报监控
         */
        private Boolean report;

        public Integer getExpireAfterWrite() {
            return expireAfterWrite;
        }

        public void setExpireAfterWrite(Integer expireAfterWrite) {
            this.expireAfterWrite = expireAfterWrite;
        }

        public Integer getExpireAfterAccess() {
            return expireAfterAccess;
        }

        public void setExpireAfterAccess(Integer expireAfterAccess) {
            this.expireAfterAccess = expireAfterAccess;
        }

        public Integer getMaximumSize() {
            return maximumSize;
        }

        public void setMaximumSize(Integer maximumSize) {
            this.maximumSize = maximumSize;
        }

        public Boolean getReport() {
            return report;
        }

        public void setReport(Boolean report) {
            this.report = report;
        }

        @Override
        public String toString() {
            return "{" +
                    "expireAfterWriter=" + expireAfterWrite +
                    ", maximumSize=" + maximumSize +
                    '}';
        }
    }
}
