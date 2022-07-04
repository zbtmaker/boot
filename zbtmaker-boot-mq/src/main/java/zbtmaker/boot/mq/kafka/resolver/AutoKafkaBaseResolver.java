package zbtmaker.boot.mq.kafka.resolver;

import org.apache.commons.lang3.StringUtils;
import zbtmaker.boot.common.constant.StringConstants;
import zbtmaker.boot.mq.kafka.config.KafkaConsumerConfig;
import zbtmaker.boot.mq.kafka.config.KafkaProducerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zoubaitao
 * date 2022/07/03
 */
public class AutoKafkaBaseResolver {

    /**
     * 生产者配置
     */
    private static final List<KafkaProducerConfig> PRODUCER_CONFIGS = new ArrayList<>();

    /**
     * 消费者配置
     */
    private static final List<KafkaConsumerConfig> CONSUMER_CONFIGS = new ArrayList<>();

    /**
     * 初始化消费者和生产者配置
     */
    public static void init() {
        initConsumerConfigs();
        initProducerConfigs();
    }

    /**
     * 初始化生产者配置
     */
    private static void initProducerConfigs() {
        String producerClusterNames = "moli,xiangrikui";
        if (StringUtils.isBlank(producerClusterNames)) {
            return;
        }
        String[] clusterNames = producerClusterNames.split(StringConstants.DOT);
        if (clusterNames.length == 0) {
            return;
        }

        for (String clusterName : clusterNames) {
            KafkaProducerConfig producerConfig = KafkaConfigResolver.createProducerConfig(clusterName);
            PRODUCER_CONFIGS.add(producerConfig);
        }
    }

    /**
     * 初始化消费者配置
     */
    private static void initConsumerConfigs() {
        String consumerClusterNames = "moli,xiangrikui";
        if (StringUtils.isBlank(consumerClusterNames)) {
            return;
        }
        String[] clusterNames = consumerClusterNames.split(StringConstants.DOT);
        if (clusterNames.length == 0) {
            return;
        }
        for (String clusterName : clusterNames) {
            KafkaConsumerConfig consumerConfig = KafkaConfigResolver.createConsumerConfig(clusterName);
            CONSUMER_CONFIGS.add(consumerConfig);
        }
    }

    public static List<KafkaProducerConfig> getProducerConfigs() {
        return PRODUCER_CONFIGS;
    }

    public static List<KafkaConsumerConfig> getConsumerConfigs() {
        return CONSUMER_CONFIGS;
    }
}
