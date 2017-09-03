package com.xhr.xFastRpc.server;

import com.xhr.xFastRpc.registry.ZkServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 徐浩然
 * @version RcpServer, 2017-08-31
 */
public class RcpServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RcpServer.class);

    private  String serverAddress;
    // 在zk 注册服务名称和地址


    private ZkServiceRegistry zkServiceRegistry;

    /**
     * 服务的名字和对象的关系需要保存
    * */
    private Map<String ,Object> serverObjectMap = new HashMap<>();

    public RcpServer(String serverAddress){ this.serverAddress = serverAddress; }

    public RcpServer(String serverAddress , ZkServiceRegistry zkServiceRegistry){
        this.serverAddress =serverAddress;
        this.zkServiceRegistry = zkServiceRegistry;
    }

}
