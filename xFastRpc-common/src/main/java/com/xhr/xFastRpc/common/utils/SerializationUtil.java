package com.xhr.xFastRpc.common.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dyuproject.protostuff.runtime.RuntimeSchema.getSchema;

/**
 * 序列化工具
 * 如果序列化的对象 新增一个 属性字段，会有问题
 * 以前序列化是之前老的对象（没有加字段之前）
 * 如果对 老的序列号的字节数组 反序列化新的类（新加了个字段的类）对象
 * （ Protostuff ）
 *
 * @author 徐浩然
 * @version SerializationUtil, 2017-09-07
 */
public class SerializationUtil
{
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    public SerializationUtil()
    {
    }
    /* 序列化
    对象-数组*/

    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj)
    {
        //获得对象的类
        Class<T> cls = (Class<T>) obj.getClass();
        //使用LinkedBuffer分配一块默认大小的buffer空间
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try
        {
            //通过对象的类构建对应的schema
            Schema<T> schema = getSchema(cls);
            //使用给定的schema将对象序列化为一个byte数组，并返回
            return ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        } finally
        {
            buffer.clear();
        }
    }

    /*反序列化 字节- 对象*/
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data, Class<T> cls)
    {
        try
        {   //使用objenesis实例化一个类的对象
            T message = objenesis.newInstance(cls);
            //通过对象的类构建对应的schema
            Schema<T> schema = getSchema(cls);
            //使用给定的schema将byte数组和对象合并，并返回
            ProtobufIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    //优化  使用缓存 类对应的Schema可以被缓存起来
    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> cls)
    {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null)
        {
            //todo RuntimeSchema 用法需要学习一下
            schema = RuntimeSchema.createFrom(cls);
            cachedSchema.put(cls, schema);
        }
        return schema;
    }


}
