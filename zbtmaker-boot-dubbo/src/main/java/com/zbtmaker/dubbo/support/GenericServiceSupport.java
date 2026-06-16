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
public class GenericServiceSupport {

    private static final Map<String, Holder<GenericService>> GENERIC_SERVICE_CACHE = new ConcurrentHashMap<>();

    public static GenericService getGenericService(GenericRpcReq genericRpcReq) {
        String reqId = genericRpcReq.getReferenceConfigId();
        Holder<GenericService> holder = GENERIC_SERVICE_CACHE.get(reqId);
        if (holder == null) {
            holder = new Holder<>();
            GENERIC_SERVICE_CACHE.putIfAbsent(reqId, holder);
        }
        GenericService genericService = holder.getValue();
        if (genericService == null) {
            synchronized (holder) {
                genericService = holder.getValue();
                if (genericService == null) {
                    holder.setValue(buildReferenceConfig(genericRpcReq).get());
                    genericService = holder.getValue();
                }
            }
        }
        return genericService;
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
