package zbtmaker.boot.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import zbtmaker.boot.common.util.JacksonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zoubaitao
 * date 2022/02/27
 */
public class CacheResolver {

    private static final String CACHE_PREFIX = "app.cache";
    @Autowired
    private Environment environment;

    @SuppressWarnings("unchecked")
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
                        String cacheName = key.substring(CACHE_PREFIX.length());
                        CacheConfig cacheConfig = JacksonUtil.parseObject(entry.getValue().toString(), CacheConfig.class);
                        if (cacheConfig == null) {
                            continue;
                        }
                        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
                        caffeine.expireAfterWrite(cacheConfig.expireAfterWriter, TimeUnit.SECONDS);
                        caffeine.maximumSize(cacheConfig.maximumSize);
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
        private Integer expireAfterWriter;

        private Integer maximumSize;

        public Integer getExpireAfterWriter() {
            return expireAfterWriter;
        }

        public void setExpireAfterWriter(Integer expireAfterWriter) {
            this.expireAfterWriter = expireAfterWriter;
        }

        public Integer getMaximumSize() {
            return maximumSize;
        }

        public void setMaximumSize(Integer maximumSize) {
            this.maximumSize = maximumSize;
        }

        @Override
        public String toString() {
            return "{" +
                    "expireAfterWriter=" + expireAfterWriter +
                    ", maximumSize=" + maximumSize +
                    '}';
        }
    }
}

