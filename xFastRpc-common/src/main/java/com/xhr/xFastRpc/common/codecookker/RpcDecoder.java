package com.xhr.xFastRpc.common.codecookker;

import com.xhr.xFastRpc.common.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Rpc 解码器
 * @author 徐浩然
 * @version RpcDecoder, 2017-09-07
 */
public class RpcDecoder extends ByteToMessageDecoder
{
    private  Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass)
    {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        if (in.readableBytes() <4)
        {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() == in.readInt())
        {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        out.add(SerializationUtil.deserialize(data,genericClass));
    }
}
