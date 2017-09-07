package com.xhr.xFastRpc.server;

import com.xhr.xFastRpc.registry.ZkServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc 服务器 发布RPC服务
 * @author 徐浩然
 * @version RcpServer, 2017-08-31
 */
public class RcpServer implements ApplicationContextAware, InitializingBean
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

    /**
     * 扫描注解 初始化Map
     *
     * @author 徐浩然
     * @version 2017-09-07
     * @param   applicationContext 上下文
     * @return null
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        Map<String , Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean :  serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String serviceVercion = rpcService.version();
                if (StringUtils.isNotEmpty(serviceVercion))
                {
                    serviceName+= "-" + serviceVercion;
                }
                serverObjectMap.put(serviceName,serviceBean);
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //初始化服务端 Bootstrap对象
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(
                new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception{
                        ChannelPipeline pipeline = channel.pipeline();
                        //解码 编码 处理 RPC 请求
                        pipeline.addLast(new RpcService)
                    }
                }
        );
    }


}
