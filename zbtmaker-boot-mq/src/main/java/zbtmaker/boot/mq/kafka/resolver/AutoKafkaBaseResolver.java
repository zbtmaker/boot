package zbtmaker.boot.mq.kafka.resolver;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import zbtmaker.boot.common.util.MapUtils;
import zbtmaker.boot.mq.kafka.config.KafkaCommonConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zoubaitao
 * date 2022/07/03
 */
public class AutoKafkaBaseResolver {

    private final static String KAFKA_PRODUCER_PREFIX = "kafka.producer";
    private final static String KAFKA_CONSUMER_PREFIX = "kafka.consumer";
    /**
     * 生产者配置
     */
    private static List<KafkaCommonConfig> PRODUCER_CONFIGS;

    /**
     * 消费者配置
     */
    private static List<KafkaCommonConfig> CONSUMER_CONFIGS;

    public static void parseClusterConfigs(Environment environment) {
        Map<String, KafkaCommonConfig> producerClusterNameMapConfig = new HashMap<>();
        Map<String, KafkaCommonConfig> consumerClusterNameMapConfig = new HashMap<>();
        AbstractEnvironment abstractEnvironment = (AbstractEnvironment) environment;
        for (PropertySource<?> source : abstractEnvironment.getPropertySources()) {
            Object o = source.getSource();
            if (o instanceof Map) {
                for (Map.Entry<String, Object> entry : (((Map<String, Object>) o).entrySet())) {
                    String key = entry.getKey();
                    if (key.startsWith(KAFKA_CONSUMER_PREFIX)) {
                        String keySuffix = key.substring(KAFKA_CONSUMER_PREFIX.length() + 1);
                        parseKey(keySuffix, entry.getValue().toString(), consumerClusterNameMapConfig);
                    } else if (key.startsWith(KAFKA_PRODUCER_PREFIX)) {
                        String keySuffix = key.substring(KAFKA_PRODUCER_PREFIX.length() + 1);
                        parseKey(keySuffix, entry.getValue().toString(), producerClusterNameMapConfig);
                    }
                }
            }
        }
        // 设置生产者默认序列化方式
        if (MapUtils.isNotEmpty(producerClusterNameMapConfig)) {
            PRODUCER_CONFIGS = new ArrayList<>(producerClusterNameMapConfig.values());
            for (KafkaCommonConfig commonConfig : PRODUCER_CONFIGS) {
                Map<String, Object> properties = commonConfig.getProperties();
                if (!properties.containsKey(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)) {
                    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
                }
                if (!properties.containsKey(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)) {
                    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
                }
            }
        }
        // 设置默认反序列化方式
        if (MapUtils.isNotEmpty(consumerClusterNameMapConfig)) {
            CONSUMER_CONFIGS = new ArrayList<>(consumerClusterNameMapConfig.values());
            for (KafkaCommonConfig commonConfig : CONSUMER_CONFIGS) {
                Map<String, Object> properties = commonConfig.getProperties();
                if (!properties.containsKey(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG)) {
                    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
                }
                if (!properties.containsKey(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG)) {
                    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
                }
            }
        }

    }

    private static void parseKey(String keySuffix, String value, Map<String, KafkaCommonConfig> clusterNameMapConfig) {
        int index = keySuffix.indexOf(".");
        if (index == -1) {
            return;
        }
        String clusterName = keySuffix.substring(0, index);
        KafkaCommonConfig commonConfig = clusterNameMapConfig.get(clusterName);
        if (commonConfig == null) {
            commonConfig = new KafkaCommonConfig();
            commonConfig.setClusterName(clusterName);
            commonConfig.setProperties(new HashMap<>());
            clusterNameMapConfig.put(clusterName, commonConfig);
        }
        commonConfig.getProperties().put(keySuffix.substring(index + 1), value);
    }

    public static List<KafkaCommonConfig> getProducerConfigs() {
        return PRODUCER_CONFIGS;
    }

    public static List<KafkaCommonConfig> getConsumerConfigs() {
        return CONSUMER_CONFIGS;
    }
}
