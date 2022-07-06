package zbtmaker.boot.mq.kafka.resolver;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import zbtmaker.boot.mq.kafka.config.KafkaCommonConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zoubaitao
 * date 2022/07/03
 */
public class KafkaConfigResolver {

    /**
     * 生产者配置前缀
     */
    private final static String PRODUCER_CONFIG_PREFIX = "kafka.producer.";

    /**
     * 消费者配置前缀
     */
    private final static String CONSUMER_CONFIG_PREFIX = "kafka.consumer.";

    /**
     * 初始化消费者配置
     *
     * @param clusterName 集群名称
     * @return 消费者配置
     */
    public static KafkaConsumerConfig createConsumerConfig(String clusterName) {
        KafkaConsumerConfig consumerConfig = new KafkaConsumerConfig();
        consumerConfig.setClusterName(clusterName);

        Map<String, Object> properties = new HashMap<>();
        consumerConfig.setProperties(properties);

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "");

        consumerConfig.setBatchListener(Boolean.TRUE);
        return consumerConfig;
    }

    /**
     * 初始化生产者配置
     *
     * @param clusterName 集群名称
     * @return 生产者配置
     */
    public static KafkaProducerConfig createProducerConfig(String clusterName) {
        KafkaProducerConfig producerConfig = new KafkaProducerConfig();
        producerConfig.setClusterName(clusterName);

        Map<String, Object> properties = new HashMap<>();
        producerConfig.setProperties(properties);

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "");

        return producerConfig;
    }

    /**
     * 初始化公共配置
     *
     * @param commonConfig 公共配置
     */
    private static void initCommonConfig(KafkaCommonConfig commonConfig) {
        Map<String, Object> properties = commonConfig.getProperties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "");
        properties.put(SaslConfigs.SASL_MECHANISM, "");
        properties.put(SaslConfigs.SASL_JAAS_CONFIG, "");
    }

}
