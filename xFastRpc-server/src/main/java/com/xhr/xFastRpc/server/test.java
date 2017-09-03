package com.xhr.xFastRpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 徐浩然
 * @version test, 2017-09-01
 */
public class test
{
    private static final Logger LOGGER = LoggerFactory.getLogger(test.class);

    public static void main(String[] args){
       try
       {
           Class c = null;
             c = Class.forName("com.xhr.xFastRpc.server.fatoy");
           Class up = c.getSuperclass();
           Object sss= up.getClass();
           System.out.println(c.toString());
           System.out.println(up);
           System.out.println(sss);
           LOGGER.error(c.toString());
           LOGGER.error("",up );
           LOGGER.error("", sss);
           
       }catch (ClassNotFoundException e){
           System.out.print("sssssss");
       }
    }
}
class toy{
    toy(){}
    toy(int i){}
}
class fatoy extends toy{
    fatoy (){super(1);}
}
