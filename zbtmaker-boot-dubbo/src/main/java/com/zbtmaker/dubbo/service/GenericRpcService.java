package com.zbtmaker.dubbo.service;

import com.zbtmaker.dubbo.bo.GenericRpcReq;

/**
 * @author zoubaitao
 * date 2022/09/18
 */
public interface GenericRpcService {

    /**
     * 泛化调用
     *
     * @param rpcReq 泛化调用请求参数
     * @return 结果
     */
    Object invoke(GenericRpcReq rpcReq);
}
