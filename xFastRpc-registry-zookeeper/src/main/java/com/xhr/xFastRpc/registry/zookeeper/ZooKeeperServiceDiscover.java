package com.xhr.xFastRpc.registry.zookeeper;

import com.xhr.xFastRpc.registry.ZkServerDiscover;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public String discover(String addServerName)
    {
        //探测服务接口流程
        /*
        创建 zk 客户端
        获取 server 节点
        获取 address 节点
        */

        //创建zk client
        ZkClient zkClient = new ZkClient(zkServerAdderss, zkConstant.ZK_SESSION_TIMEOUT, zkConstant.ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("连接到zk Server");


        try
        {
            return null;
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            return null;
        }
    }
}
