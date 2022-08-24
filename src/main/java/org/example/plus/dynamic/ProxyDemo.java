package org.example.plus.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2022/8/24
 */
public class ProxyDemo {
    interface ProxyInterface {
        String sayHello();
    }

    public static void main(String[] args) {
        class One implements ProxyInterface {

            @Override
            public String sayHello() {
                return "aaa";
            }
        }

        class OneProxy implements InvocationHandler{
            public final One one;
            public OneProxy(One one) {
                this.one = one;
            }
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("代理 invoke");
                return method.invoke(one, args);
            }
        }

        One one = new One();
        InvocationHandler handler = new OneProxy(one);
        ProxyInterface o = (ProxyInterface) Proxy.newProxyInstance(one.getClass().getClassLoader(), new Class[]{ProxyInterface.class}, handler);
        System.out.println(o.sayHello());
    }


}
