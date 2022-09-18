package com.zbtmaker.dubbo.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zbtmaker.dubbo.bo.GenericRpcReq;
import com.zbtmaker.dubbo.service.GenericRpcService;
import com.zbtmaker.dubbo.support.ReferenceConfigSupport;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * @author zoubaitao
 * date 2022/09/18
 */
public class GenericRpcServiceImpl implements GenericRpcService {

    @Override
    public Object invoke(GenericRpcReq rpcReq) {
        Entry entry = null;
        try {
            entry = SphU.entry(rpcReq.getBlockHandler());
            ReferenceConfig<GenericService> referenceConfig = ReferenceConfigSupport.getReferenceConfig(rpcReq);
            GenericService genericService = referenceConfig.get();
            return genericService.$invoke(rpcReq.getMethod(), rpcReq.getParamTypes(), rpcReq.getParams());
        } catch (BlockException ex) {

        } finally{
            if(entry != null) {
                entry.exit(1);
            }
        }
        return null;
    }
}
