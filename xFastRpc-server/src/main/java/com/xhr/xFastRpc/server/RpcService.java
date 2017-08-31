package com.xhr.xFastRpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rpc 服务注解接口(用于服务类上的注解)
 * @author 徐浩然
 * @version RpcService, 2017-08-31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService
{
    /**
     * 服务接口
     *
     * @author 徐浩然
     * @version 2017-08-31
     */
    Class<?> value();

    /**
     * 提供服务的版本号
     *
     * @author 徐浩然
     * @version 2017-08-31
     */
    String version() default "";
}
