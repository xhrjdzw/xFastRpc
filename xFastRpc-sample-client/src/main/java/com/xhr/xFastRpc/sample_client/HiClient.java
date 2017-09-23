package com.xhr.xFastRpc.sample_client;

import com.xhr.xFastRpc.client.xFastRpcProxy;
import com.xhr.xFastRpc.sample_api.HiService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.xhr.xFastRpc.
/**
 * @author 徐浩然
 * @version HiClient, 2017-09-23
 */
public class HiClient
{
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        xFastRpcProxy rpcProxy = context.getBean(xFastRpcProxy.class);

        HiService helloService = rpcProxy.create(HiService.class);
        String result = helloService.hello("World");
        System.out.println(result);

        HiService helloService2 = rpcProxy.create(HiService.class, "sample.hello2");
        String result2 = helloService2.hello("The holyshit 世界");
        System.out.println(result2);

        System.exit(0);
    }
}
