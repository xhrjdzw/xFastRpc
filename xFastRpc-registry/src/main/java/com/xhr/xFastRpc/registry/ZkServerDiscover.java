package com.xhr.xFastRpc.registry;

/**
 * 发现zk服务
 *
 * @author 徐浩然
 * @version ZkServerDiscover, 2017-08-30
 */
public interface ZkServerDiscover
{

    /**
     *
     *
     * @author 徐浩然
     * @version 2017-08-30
     * @param   addServerName 服务名字
     * @return  服务地址
     */
    String discover(String addServerName);



}
