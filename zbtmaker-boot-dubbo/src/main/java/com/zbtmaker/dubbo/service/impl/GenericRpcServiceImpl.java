package com.zbtmaker.dubbo.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zbtmaker.dubbo.bo.GenericRpcReq;
import com.zbtmaker.dubbo.service.GenericRpcService;
import com.zbtmaker.dubbo.support.ReferenceConfigSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.stereotype.Service;
import zbtmaker.boot.common.util.JacksonUtils;

/**
 * @author zoubaitao
 * date 2022/09/18
 */
@Slf4j
@Service
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
            log.error("泛化调用异常, 接口::{}, 方法::{}, 请求参数::{}", rpcReq.getInterfaceName(), rpcReq.getMethod(), JacksonUtils.toString(rpcReq), ex);
        } finally {
            if (entry != null) {
                entry.exit(1);
            }
        }
        return null;
    }
}
