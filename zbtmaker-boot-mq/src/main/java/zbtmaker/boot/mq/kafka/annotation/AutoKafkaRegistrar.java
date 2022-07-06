package zbtmaker.boot.mq.kafka.annotation;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.CollectionUtils;
import zbtmaker.boot.mq.kafka.config.KafkaCommonConfig;
import zbtmaker.boot.mq.kafka.resolver.AutoKafkaBaseResolver;

import java.util.List;
import java.util.Objects;

/**
 * @author zoubaitao
 * date 2022/07/03
 */
public class AutoKafkaRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AutoKafkaBaseResolver.caffeineCacheManager(environment);
        List<KafkaCommonConfig> producerConfigs = AutoKafkaBaseResolver.getProducerConfigs();
        if (!CollectionUtils.isEmpty(producerConfigs)) {
            for (KafkaCommonConfig producerConfig : producerConfigs) {
                registerKafkaTemplate(producerConfig, registry);
            }
        }

        List<KafkaCommonConfig> consumerConfigs = AutoKafkaBaseResolver.getConsumerConfigs();
        if (!CollectionUtils.isEmpty(consumerConfigs)) {
            for (KafkaCommonConfig consumerConfig : consumerConfigs) {
                registerContainerFactory(consumerConfig, registry);
            }
        }
    }

    private void registerKafkaTemplate(KafkaCommonConfig producerConfig, BeanDefinitionRegistry registry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setParentName(KafkaTemplate.class.getName());
        ConstructorArgumentValues cav = beanDefinition.getConstructorArgumentValues();
        cav.addIndexedArgumentValue(0, new DefaultKafkaProducerFactory<>(producerConfig.getProperties()));
        String beanName = "kafkaTemplate#" + producerConfig.getClusterName();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    private void registerContainerFactory(KafkaCommonConfig consumerConfig, BeanDefinitionRegistry registry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setParentName(ConcurrentKafkaListenerContainerFactory.class.getName());
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("consumerFactory", new DefaultKafkaProducerFactory<>(consumerConfig.getProperties()));
        Boolean batchListener = Objects.isNull(consumerConfig.getProperties().get("batch.listener"))
                ? Boolean.FALSE
                : Boolean.parseBoolean(consumerConfig.getProperties().get("batch.listener").toString());
        if (BooleanUtils.isTrue(batchListener)) {
            mpv.addPropertyValue("batchListener", Boolean.TRUE);
        }
        String beanName = "containerFactory#" + consumerConfig.getClusterName();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
}
