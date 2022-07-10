package zbtmaker.boot.cache.config.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zoubaitao
 * date 2022/07/10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CacheAutoConfiguration.class})
public @interface EnableAutoCaching {
}
