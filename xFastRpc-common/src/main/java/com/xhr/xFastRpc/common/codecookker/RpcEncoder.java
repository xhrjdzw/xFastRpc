package com.xhr.xFastRpc.common.codecookker;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.SerializationUtils;


/**
 * Rpc 编码器
 * @author 徐浩然
 * @version RpcEncoder, 2017-09-07
 */
public class RpcEncoder extends MessageToByteEncoder
{
    private  Class<?> genericClass;

    public  RpcEncoder(Class<?> genericClass) { this.genericClass = genericClass;}

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception
    {
        if (genericClass.isInstance(msg))
        {
            byte[] data = SerializationUtil.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }

    }
}
