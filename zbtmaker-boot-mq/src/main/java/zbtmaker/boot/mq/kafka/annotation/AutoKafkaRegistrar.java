package zbtmaker.boot.mq.kafka.annotation;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.CollectionUtils;
import zbtmaker.boot.mq.kafka.config.KafkaConsumerConfig;
import zbtmaker.boot.mq.kafka.config.KafkaProducerConfig;
import zbtmaker.boot.mq.kafka.resolver.AutoKafkaBaseResolver;

import java.util.List;

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
        AutoKafkaBaseResolver.parseClusterConfigs(environment);
        List<KafkaProducerConfig> producerConfigs = AutoKafkaBaseResolver.getProducerConfigs();
        if (!CollectionUtils.isEmpty(producerConfigs)) {
            for (KafkaProducerConfig producerConfig : producerConfigs) {
                registerKafkaTemplate(producerConfig, registry);
            }
        }

        List<KafkaConsumerConfig> consumerConfigs = AutoKafkaBaseResolver.getConsumerConfigs();
        if (!CollectionUtils.isEmpty(consumerConfigs)) {
            for (KafkaConsumerConfig consumerConfig : consumerConfigs) {
                registerContainerFactory(consumerConfig, registry);
            }
        }
    }

    private void registerKafkaTemplate(KafkaProducerConfig producerConfig, BeanDefinitionRegistry registry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(KafkaTemplate.class);
        ConstructorArgumentValues cav = beanDefinition.getConstructorArgumentValues();
        cav.addIndexedArgumentValue(0, new DefaultKafkaProducerFactory<>(producerConfig.getProperties()));
        String beanName = "kafkaTemplate#" + producerConfig.getClusterName();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    private void registerContainerFactory(KafkaConsumerConfig consumerConfig, BeanDefinitionRegistry registry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ConcurrentKafkaListenerContainerFactory.class);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("consumerFactory", new DefaultKafkaConsumerFactory<>(consumerConfig.getProperties()));

        mpv.add("autoStartup", consumerConfig.getAutoStartup());
        mpv.addPropertyValue("batchListener", consumerConfig.getBatchListener());

        String beanName = "containerFactory#" + consumerConfig.getClusterName();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
}
