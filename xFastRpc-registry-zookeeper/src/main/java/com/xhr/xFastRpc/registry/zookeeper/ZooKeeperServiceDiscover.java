package com.xhr.xFastRpc.registry.zookeeper;

import com.xhr.xFastRpc.registry.ZkServerDiscover;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 探测服务接口
 *
 * @author 徐浩然
 * @version ZooKeeperServiceDiscover, 2017-08-30
 */
public class ZooKeeperServiceDiscover implements ZkServerDiscover
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperServiceDiscover.class);

    private String zkServerAdderss;

    public ZooKeeperServiceDiscover(String zkServerAdderss)
    {
        this.zkServerAdderss = zkServerAdderss;
    }

    @Override
    /**
     *
     *
     * @author 徐浩然
     * @version 2017-08-30
     * @param   * @param addServerName
     * @return java.lang.String
     */
    public String discover(String serverName)
    {
        //探测服务接口流程
        /*
        创建 zk 客户端
        获取 server 节点
        获取 address 节点
        */
        //创建zk client
        ZkClient zkClient = new ZkClient(zkServerAdderss, ZkConstant.ZK_SESSION_TIMEOUT, ZkConstant.ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("连接到zk Server");


        try
        {
            // 获得 service 节点
            String servicePath = ZkConstant.ZK_PATH + "/" + serverName;
            List<String> addressList = zkClient.getChildren(servicePath);
            // 获得 address 节点
            String address;


            if (addressList.size() == 1)
            {
                address = addressList.get(0);
            } else
            {
                address = addressList.get(ThreadLocalRandom.current().nextInt(addressList.size()));
            }

            //获取 address 节点的值
            String addressPath = servicePath + "/" + address;

            return  zkClient.readData(addressPath);
        } finally
        {
            zkClient.close();
        }
    }
}
