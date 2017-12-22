package com.xhr.xFastRpc.sample_server;

import com.xhr.xFastRpc.sample_api.HiService;
import com.xhr.xFastRpc.server.RpcService;

/**
 * @author 徐浩然
 * @version HiServiceImpl2, 2017-12-22
 */
@RpcService(value = HiService.class, version = "sample.hello2")
public class HelloServiceImpl2 implements HiService
{
    @Override
    public String hello(String name) {
        return "你好! " + name;
    }


}

