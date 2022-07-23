package zbtmaker.boot.mq.kafka.config;

import lombok.Data;

import java.util.Map;

/**
 * @author zoubaitao
 * date 2022/07/03
 */
@Data
public class KafkaCommonConfig {

    /**
     * 相关配置
     */
    private Map<String, Object> properties;

    /**
     * 集群名称
     */
    private String clusterName;


}
