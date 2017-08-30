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

    public ZooKeeperServiceRegistry(String zkServerAdderss)
    {
        //开启 zk 客戶端
        zkClient = new ZkClient(zkServerAdderss, ZkConstant.ZK_SESSION_TIMEOUT, ZkConstant.ZK_CONNECTION_TIMEOUT);
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
     *
     *思考:
     * 为什么要创建三个步骤的节点,从架构和业务逻辑分析
     * 节点的名字规则
     */
    public void registry(String serviceName, String serviceAddress)
    {
        //创建 注册节点 创建service 创建地址节点
        String registryPath = ZkConstant.ZK_PATH;
        // 如果不存在节点，就新建一个节点 需要判断是否成功 (持久的方式?)
        if (!zkClient.exists(registryPath))
        {
            //创建一个节点  注册节点registry
            zkClient.createPersistent(registryPath);
            //探测节点是否创建成功
            if (zkClient.exists(registryPath))
            {
                LOGGER.debug("创建节点成功", registryPath);
            }else{
                LOGGER.error("创建节点失败", registryPath);
            }
        }
        //创建一个service节点  持久节点
        //todo 可以优化成功判断
        String servicePath = registryPath + "/" +serviceName;
        if (!zkClient.exists(servicePath))
        {
            zkClient.createPersistent(servicePath);
            //判断节点结果
            if (zkClient.exists(servicePath))
            {
                LOGGER.debug("创建服务节点成功", servicePath);
            }else {
                LOGGER.error("创建服务节点失败", servicePath);
            }
        }
        //创建一个 address 节点 这是一个临时的节点
        String addressPath = servicePath + "xuAddress-";
        //返回节点全部地址
        String addressNode = zkClient.createEphemeralSequential(addressPath,serviceAddress);
        LOGGER.error("成功创建节点",addressNode);
    }
}
