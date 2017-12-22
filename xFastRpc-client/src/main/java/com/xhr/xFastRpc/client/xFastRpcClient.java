package com.xhr.xFastRpc.client;

import com.xhr.xFastRpc.common.bean.RpcRequest;
import com.xhr.xFastRpc.common.bean.RpcResponse;
import com.xhr.xFastRpc.common.codecookker.RpcDecoder;
import com.xhr.xFastRpc.common.codecookker.RpcEncoder;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;

/**
 * 客户端发送rpc请求
 *
 * @author 徐浩然
 * @version xFastRpcClient, 2017-09-12
 */
public class xFastRpcClient extends SimpleChannelInboundHandler<RpcResponse>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(xFastRpcClient.class);

    private final String host;
    private final int port;

    private RpcResponse response;

    public xFastRpcClient(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception
    {
        this.response = response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        LOGGER.error("api GG", cause);
        ctx.close();
    }

    public RpcResponse send(RpcRequest request) throws Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();

        try
        {
            //创建并初始化 Netty 客户端 Bootstrap 对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>()
                              {
                                  @Override
                                  public void initChannel(SocketChannel channel) throws Exception
                                  {
                                      ChannelPipeline pipeline = channel.pipeline();
                                      // 编码 RPC 请求
                                      pipeline.addLast(new RpcEncoder(RpcRequest.class));
                                      // 解码 RPC 响应
                                      pipeline.addLast(new RpcDecoder(RpcResponse.class));
                                      // 处理 RPC 响应
                                      pipeline.addLast(xFastRpcClient.this);
                                  }
                              }
            );
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            //链接 rpc服务器
            ChannelFuture future = bootstrap.connect(host, port).sync();
            //写入 Rpc 请求 关闭连接
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
            //返回rpc 响应对象
            return response;
        } finally
        {
            group.shutdownGracefully();
        }
    }
}
