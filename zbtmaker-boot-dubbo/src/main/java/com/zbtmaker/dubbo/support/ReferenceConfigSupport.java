package com.zbtmaker.dubbo.support;

import com.zbtmaker.dubbo.bo.GenericRpcReq;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zoubaitao
 * date 2022/09/18
 */
public class ReferenceConfigSupport {

    private static final Map<String, Holder<ReferenceConfig<GenericService>>> REFERENCE_CONFIG_CACHE = new ConcurrentHashMap<>();

    public static ReferenceConfig<GenericService> getReferenceConfig(GenericRpcReq genericRpcReq) {
        String reqId = genericRpcReq.getReferenceConfigId();
        Holder<ReferenceConfig<GenericService>> holder = REFERENCE_CONFIG_CACHE.get(reqId);
        if (holder == null) {
            holder = new Holder<>();
            REFERENCE_CONFIG_CACHE.putIfAbsent(reqId, holder);
        }
        ReferenceConfig<GenericService> referenceConfig = holder.getValue();
        if (referenceConfig == null) {
            synchronized (holder) {
                referenceConfig = holder.getValue();
                if (referenceConfig == null) {
                    referenceConfig = buildReferenceConfig(genericRpcReq);
                    holder.setValue(referenceConfig);
                }
            }
        }
        return referenceConfig;
    }

    private static ReferenceConfig<GenericService> buildReferenceConfig(GenericRpcReq genericRpcReq) {
        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(genericRpcReq.getApplicationConfig());
        referenceConfig.setRegistry(genericRpcReq.getRegistryConfig());
        referenceConfig.setInterface(genericRpcReq.getInterfaceName());
        referenceConfig.setGroup(genericRpcReq.getGroup());
        referenceConfig.setVersion(genericRpcReq.getVersion());
        referenceConfig.setRetries(genericRpcReq.getRetries());
        referenceConfig.setLoadbalance(genericRpcReq.getLoadBalance());
        referenceConfig.setTimeout(genericRpcReq.getTimeout());
        referenceConfig.setGeneric("true");
        return referenceConfig;
    }
}
