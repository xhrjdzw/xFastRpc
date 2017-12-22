package com.xhr.xFastRpc.common.codecookker;

import com.xhr.xFastRpc.common.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Rpc 解码器
 *
 * @author 徐浩然
 * @version RpcDecoder, 2017-09-07
 */
public class RpcDecoder extends ByteToMessageDecoder
{
    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass)
    {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception
    {
        if (byteBuf.readableBytes() < 4)

        {
            return;
        }
        byteBuf.markReaderIndex();
        short magic = byteBuf.readShort();
//        if (magic != Constants.MAGIC)
//
//        {
//            byteBuf.resetReaderIndex();
//            throw new ForestFrameworkException("ForestDecoder transport header not support, type: " + magic);
//        }

        byte version = byteBuf.readByte();
        byte extend = byteBuf.readByte();
        long messageID = byteBuf.readLong();
        int size = byteBuf.readInt();
        Object req = null;
        if (!(size == 0 && EventType.typeofHeartBeat(extend)))

        {
            if (byteBuf.readableBytes() < size)
            {
                byteBuf.resetReaderIndex();
                return;
            }
            // TODO 限制最大包长
            byte[] payload = new byte[size];
            byteBuf.readBytes(payload);
            Serialization serialization = SerializeType.getSerializationByExtend(extend);
            Compress compress = CompressType.getCompressTypeByValueByExtend(extend);
            req = serialization.deserialize(compress.unCompress(payload), MessageType.getMessageTypeByExtend(extend));
        }

        Header header = new Header(magic, version, extend, messageID, size);
        Message message = new Message(header, req);
        list.add(message);
    }
}
