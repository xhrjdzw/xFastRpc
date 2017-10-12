package com.xhr.xFastRpc.server;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 徐浩然
 * @version test, 2017-10-12
 */
public class test
{
    public static void main(String[] args)
    {
        Animal a1 = new Dog();
        a1.shout();//编译通过
        a1.shout();//编译出错
        Dog d1 = new Dog();
        List list = new ArrayList();
        ArrayList arrayList = new ArrayList();
        list.trimToSize();//错误，没有该方法。
        arrayList.trimToSize();//ArrayList里有该方法。

    }
}

abstract class Animal
{
    //动物名字
    String name;

    //动物叫声
    public void shout()
    {
        System.out.println("叫声...");
    }
}

class Dog extends Animal
{
    //狗类独有的方法
    public void guard()
    {
        System.out.println("狗有看门的独特本领！");
    }
}
}
