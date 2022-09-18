package com.zbtmaker.dubbo.bo;

import lombok.Builder;
import lombok.Getter;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;

/**
 * @author zoubaitao
 * date 2022/09/18
 */
@Builder
@Getter
public class GenericRpcReq {
    /**
     * referenceConfig唯一标识符，由Interface+method+group+version,然后计算出一个HashID得出一个唯一标识符
     */
    private final String referenceConfigId;

    /**
     * 限流的唯一名称
     */
    private final String blockHandler;


    /**
     * 接口名称
     */
    private final String interfaceName;

    /**
     * 方法名
     */
    private final String method;

    /**
     * 分组名称
     */
    private final String group;

    /**
     * 版本
     */
    private final String version;

    /**
     * 超时时间
     */
    private final Integer timeout;

    /**
     * 重试次数
     */
    private final Integer retries;

    /**
     * 负载均衡策略
     */
    private final String loadBalance;

    /**
     * 请求参数类型
     */
    private final String[] paramTypes;

    /**
     * 请求参数
     */
    private final Object[] params;

    /**
     * 应用配置
     */
    private final ApplicationConfig applicationConfig;

    /**
     * 注册配置
     */
    private final RegistryConfig registryConfig;
}
