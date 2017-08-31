package com.xhr.xFastRpc.sample_server;

import com.xhr.xFastRpc.sample_api.HiService;
import com.xhr.xFastRpc.sample_api.MyName;
import com.xhr.xFastRpc.server.RpcService;

/**
 * @author 徐浩然
 * @version HiServiceImpl, 2017-08-31
 */
//rcp服务注解
@RpcService(HiService.class)
public class HiServiceImpl implements HiService
{
    @Override
    public String hello(String name)
    {
        return "Hi, javavava" + name;
    }

    @Override
    public String hello(MyName myname)
    {
        return "Hi jvavavavavav !" + myname.getFirstName() + " " + myname.getLastName();
    }
}
