package zbtmaker.boot.mq.kafka.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zoubaitao
 * date 2022/07/03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KafkaConsumerConfig extends KafkaCommonConfig {

    /**
     * 是否开启批量消费，true-开启，false-关闭，默认false-关闭
     */
    private Boolean batchListener;
}
