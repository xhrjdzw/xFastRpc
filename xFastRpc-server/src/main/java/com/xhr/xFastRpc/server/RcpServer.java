package com.xhr.xFastRpc.server;

import com.xhr.xFastRpc.common.bean.RpcRequest;
import com.xhr.xFastRpc.common.bean.RpcResponse;
import com.xhr.xFastRpc.common.codecookker.RpcDecoder;
import com.xhr.xFastRpc.common.codecookker.RpcEncoder;
import com.xhr.xFastRpc.registry.ZkServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
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
 *
 * @author 徐浩然
 * @version RcpServer, 2017-08-31
 */
public class RcpServer implements ApplicationContextAware, InitializingBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RcpServer.class);

    private String serverAddress;
    // 在zk 注册服务名称和地址


    private ZkServiceRegistry zkServiceRegistry;

    /**
     * 服务的名字和对象的关系需要保存
     */
    private Map<String, Object> serverObjectMap = new HashMap<>();

    public RcpServer(String serverAddress)
    {
        this.serverAddress = serverAddress;
    }

    public RcpServer(String serverAddress, ZkServiceRegistry zkServiceRegistry)
    {
        this.serverAddress = serverAddress;
        this.zkServiceRegistry = zkServiceRegistry;
    }

    /**
     * 扫描注解 初始化Map
     *
     * @param applicationContext 上下文
     * @return null
     * @author 徐浩然
     * @version 2017-09-07
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap))
        {
            for (Object serviceBean : serviceBeanMap.values())
            {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String serviceVercion = rpcService.version();
                if (StringUtils.isNotEmpty(serviceVercion))
                {
                    serviceName += "-" + serviceVercion;
                }
                serverObjectMap.put(serviceName, serviceBean);
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //初始化服务端 Bootstrap对象
        try
        {


            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(
                    new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception
                        {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new RpcDecoder(RpcRequest.class));
                            pipeline.addLast(new RpcEncoder(RpcResponse.class));
                            pipeline.addLast(new Rpc)
                            //解码 编码 处理 RPC 请求
//                            pipeline.addLast(new RpcService);
                        }
                    }
            );

            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);

            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            //获取RPC服务器的IP和端口
            String[] addressArray = StringUtils.split(serverAddress, ":");
            String ip = addressArray[0];
            int port = Integer.parseInt(addressArray[1]);
            //启动rpc服务
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            //注册 RPC 服务
            if (zkServiceRegistry != null)
            {
                for (String interfaceName : serverObjectMap.keySet())
                {
                    zkServiceRegistry.registry(interfaceName, serverAddress);
                    LOGGER.debug("开始注册服务:   ", interfaceName + "   " + serverAddress);
                }
            }
            //关闭rpc服务器
            future.channel().closeFuture().sync();
        } finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }
}
