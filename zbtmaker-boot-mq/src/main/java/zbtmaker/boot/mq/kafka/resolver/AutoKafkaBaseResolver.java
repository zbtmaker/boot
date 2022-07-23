package zbtmaker.boot.mq.kafka.resolver;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import zbtmaker.boot.common.util.MapUtils;
import zbtmaker.boot.mq.kafka.config.KafkaCommonConfig;
import zbtmaker.boot.mq.kafka.config.KafkaConsumerConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zoubaitao
 * date 2022/07/03
 */
public class AutoKafkaBaseResolver {
    private final static String KAFKA_COMMON_PREFIX = "kafka.common";
    private final static String KAFKA_PRODUCER_PREFIX = "kafka.producer";
    private final static String KAFKA_CONSUMER_PREFIX = "kafka.consumer";
    private final static String BATCH_LISTENER = "batch.listener";
    private final static String AUTO_STARTUP = "auto.startup";
    /**
     * 生产者配置
     */
    private static List<KafkaCommonConfig> PRODUCER_CONFIGS;

    /**
     * 消费者配置
     */
    private static List<KafkaConsumerConfig> CONSUMER_CONFIGS;

    public static void parseClusterConfigs(Environment environment) {
        Map<String, KafkaCommonConfig> producerClusterNameMapConfig = new HashMap<>();
        Map<String, KafkaConsumerConfig> consumerClusterNameMapConfig = new HashMap<>();
        Map<String, KafkaCommonConfig> commonConfigMap = new HashMap<>();
        AbstractEnvironment abstractEnvironment = (AbstractEnvironment) environment;
        for (PropertySource<?> source : abstractEnvironment.getPropertySources()) {
            Object o = source.getSource();
            if (o instanceof Map) {
                for (Map.Entry<String, Object> entry : (((Map<String, Object>) o).entrySet())) {
                    String key = entry.getKey();
                    if (key.startsWith(KAFKA_COMMON_PREFIX)) {
                        String keySuffix = key.substring(KAFKA_COMMON_PREFIX.length() + 1);
                        parserCommonRealProp(keySuffix, entry.getValue().toString(), commonConfigMap);
                    } else if (key.startsWith(KAFKA_CONSUMER_PREFIX)) {
                        String keySuffix = key.substring(KAFKA_CONSUMER_PREFIX.length() + 1);
                        parseConsumerRealProp(keySuffix, entry.getValue().toString(), consumerClusterNameMapConfig);
                    } else if (key.startsWith(KAFKA_PRODUCER_PREFIX)) {
                        String keySuffix = key.substring(KAFKA_PRODUCER_PREFIX.length() + 1);
                        parserCommonRealProp(keySuffix, entry.getValue().toString(), producerClusterNameMapConfig);
                    }
                }
            }
        }
        // 设置生产者默认序列化方式
        if (MapUtils.isNotEmpty(producerClusterNameMapConfig)) {
            PRODUCER_CONFIGS = new ArrayList<>(producerClusterNameMapConfig.values());
            for (KafkaCommonConfig producerConfig : PRODUCER_CONFIGS) {
                initCommonConfig(producerConfig, commonConfigMap);
                Map<String, Object> properties = producerConfig.getProperties();
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
            for (KafkaCommonConfig consumerConfig : CONSUMER_CONFIGS) {
                // 初始化公共配置
                initCommonConfig(consumerConfig, commonConfigMap);

                // 初始化key, value 反序列
                Map<String, Object> properties = consumerConfig.getProperties();
                if (!properties.containsKey(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG)) {
                    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
                }
                if (!properties.containsKey(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG)) {
                    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
                }
            }
        }
    }

    private static void initCommonConfig(KafkaCommonConfig consumerConfig, Map<String, KafkaCommonConfig> clusterNameMapCommonConfig) {
        String clusterName = consumerConfig.getClusterName();
        Map<String, Object> properties = consumerConfig.getProperties();
        KafkaCommonConfig commonConfig = clusterNameMapCommonConfig.get(clusterName);
        if (commonConfig != null && MapUtils.isNotEmpty(commonConfig.getProperties())) {
            if (MapUtils.isEmpty(consumerConfig.getProperties())) {
                properties = new HashMap<>();
                consumerConfig.setProperties(properties);
                properties.putAll(commonConfig.getProperties());
            }
            properties.putAll(commonConfig.getProperties());
        }
    }

    /**
     * 解析集群名称
     *
     * @param key
     * @return 集群名称
     */
    private static String parseCusterName(String key) {
        int index = key.indexOf(".");
        if (index == -1) {
            return StringUtils.EMPTY;
        }
        return key.substring(0, index);
    }

    private static void parseConsumerRealProp(String key, String value, Map<String, KafkaConsumerConfig> clusterNameMapConfig) {
        String clusterName = parseCusterName(key);
        if (StringUtils.isEmpty(clusterName)) {
            return;
        }
        KafkaConsumerConfig commonConfig = clusterNameMapConfig.get(clusterName);
        if (commonConfig == null) {
            commonConfig = new KafkaConsumerConfig();
            commonConfig.setClusterName(clusterName);
            commonConfig.setProperties(new HashMap<>());
            clusterNameMapConfig.put(clusterName, commonConfig);
        }

        String realProps = key.substring(clusterName.length());
        if (AUTO_STARTUP.equals(realProps)) {
            commonConfig.setAutoStartup(Boolean.parseBoolean(value));
        } else if (BATCH_LISTENER.equals(realProps)) {
            commonConfig.setBatchListener(Boolean.parseBoolean(value));
        } else {
            commonConfig.getProperties().put(realProps, value);
        }
    }

    private static void parserCommonRealProp(String key, String value, Map<String, KafkaCommonConfig> clusterNameMapConfig) {
        String clusterName = parseCusterName(key);
        if (StringUtils.isEmpty(clusterName)) {
            return;
        }
        KafkaCommonConfig commonConfig = clusterNameMapConfig.get(clusterName);
        if (commonConfig == null) {
            commonConfig = new KafkaCommonConfig();
            commonConfig.setClusterName(clusterName);
            commonConfig.setProperties(new HashMap<>());
            clusterNameMapConfig.put(clusterName, commonConfig);
        }
        String realProps = key.substring(clusterName.length());
        commonConfig.getProperties().put(realProps, value);
    }

    public static List<KafkaCommonConfig> getProducerConfigs() {
        return PRODUCER_CONFIGS;
    }

    public static List<KafkaConsumerConfig> getConsumerConfigs() {
        return CONSUMER_CONFIGS;
    }
}
