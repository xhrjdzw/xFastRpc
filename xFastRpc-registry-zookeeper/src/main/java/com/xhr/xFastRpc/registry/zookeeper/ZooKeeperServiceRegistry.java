package com.xhr.xFastRpc.registry.zookeeper;

import com.xhr.xFastRpc.registry.ZkServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 徐浩然
 * @version ZooKeeperServiceRegistry, 2017-08-30
 */
public class ZooKeeperServiceRegistry implements ZkServiceRegistry
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperServiceRegistry.class);

    private final ZkClient zkClient;

    public ZooKeeperServiceRegistry(String zkServerAdderss){
        //开启 zk 客戶端
        zkClient = new ZkClient(zkServerAdderss,ZkConstant.ZK_SESSION_TIMEOUT, ZkConstant.ZK_CONNECTION_TIMEOUT );
        LOGGER.debug("star zkClient");
    }
    @Override
    /**
     * 注册服务
     *
     * @author 徐浩然
     * @version 2017-08-30
     * @param   * @param serviceName
     * @param serviceAddress
     * @return void
     */
    public void registry(String serviceName, String serviceAddress)
    {
        //创建 注册节点 创建service 创建地址节点
        String registryPath = ZkConstant.ZK_PATH;
        // 如果不存在节点，就新建一个节点 需要判断是否成功


    }
}
