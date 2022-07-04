package zbtmaker.boot.mq.kafka.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zoubaitao
 * date 2022/07/03
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AutoKafkaConfiguration.class})
public @interface EnableAutoKafkaConfig {
}
