package com.xhr.xFastRpc.server;

import com.xhr.xFastRpc.common.bean.RpcRequest;
import com.xhr.xFastRpc.common.bean.RpcResponse;
import com.xhr.xFastRpc.common.utils.StringUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.nio.cs.FastCharsetProvider;

import java.util.Map;

/**
 * Rpc 服务端处理  (处理真正的Rpc请求)
 *
 * @author 徐浩然
 * @version RcpServerHandler, 2017-09-08
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    private  final Map<String,Object> handlerMap;

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }


    /*
    *创建同时初始化Rpc 响应对象
    *写入 RPc 响应对象然后 关闭连接*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception
    {
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getServiceVersion());
        try
        {
            Object result = handle(request);
            response.setResult(result);
        }catch (Exception e){
            LOGGER.error("handle result failure", e);
            response.setException(e);
        }
        // 写入 RPC对象 同时关闭连接
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private  Object handle(RpcRequest request) throws Exception{
        String serviceName =request.getInterfaceName();
        String serviceVersion = request.getServiceVersion();
        if (StringUtil.isNotEmpty(serviceVersion))
        {
            serviceName+= "-"+serviceVersion;
        }
        Object serviceBean = handlerMap.get(serviceName);
        if (serviceBean == null)
        {
            throw new  RuntimeException("serviceBean is Fucked off");
        }
        //反射
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        //反射调用[反射调用方式]
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName , parameterTypes);
        return  serviceFastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public  void exceptionCaught(ChannelHandlerContext ctx , Throwable cause){
        LOGGER.error("sever cauht exception", cause );
        ctx.close();
    }
}


