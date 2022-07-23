package zbtmaker.boot.mq.kafka.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zoubaitao
 * date 2022/07/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KafkaConsumerConfig extends KafkaCommonConfig {

    /**
     * 是否自启动
     */
    private Boolean autoStartup = Boolean.TRUE;

    /**
     * 是否开启ContainerFactory批量监听
     */
    private Boolean batchListener = Boolean.FALSE;
}
