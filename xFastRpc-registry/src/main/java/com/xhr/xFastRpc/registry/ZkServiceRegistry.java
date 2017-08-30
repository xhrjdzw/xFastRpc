package com.xhr.xFastRpc.registry;

/**
 * @author 徐浩然
 * @version zkServiceRegistry, 2017-08-30
 */
public interface ZkServiceRegistry
{

    /**
     * 在zk 注册服务名称和地址
     *
     * @author 徐浩然
     * @version 2017-08-30
     * @param serviceAddress 服务地址
     * @param serviceName 服务名字
     * @return void
     */
    void registry(String serviceName , String serviceAddress);
}
