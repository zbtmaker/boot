package zbtmaker.boot.mq.kafka.annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;
import zbtmaker.boot.mq.kafka.config.KafkaConsumerConfig;
import zbtmaker.boot.mq.kafka.config.KafkaProducerConfig;
import zbtmaker.boot.mq.kafka.resolver.AutoKafkaBaseResolver;

import java.util.List;

/**
 * @author zoubaitao
 * date 2022/07/03
 */
public class AutoKafkaRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AutoKafkaBaseResolver.init();
        List<KafkaProducerConfig> producerConfigs = AutoKafkaBaseResolver.getProducerConfigs();
        if (!CollectionUtils.isEmpty(producerConfigs)) {

        }

        List<KafkaConsumerConfig> consumerConfigs = AutoKafkaBaseResolver.getConsumerConfigs();
        if (!CollectionUtils.isEmpty(consumerConfigs)) {

        }
    }
}
