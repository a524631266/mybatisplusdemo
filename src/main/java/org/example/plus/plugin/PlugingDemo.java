package org.example.plus.plugin;

import org.apache.ibatis.plugin.*;

/**
 * @author : Liangliang.Zhang4
 * @version : 1.0
 * @date : 2022/8/24
 */
public class PlugingDemo {

    public static void main(String[] args) {

        class Hello implements InterfaceDemo {
            @Override
            public String sayHello(String input) {
                System.out.println("hello inner");
                return "aa";
            }
        }
        @Intercepts(value  = {
                @Signature(type = InterfaceDemo.class, method = "sayHello", args = {String.class})
        })
        class Plug implements Interceptor {

            @Override
            public Object intercept(Invocation invocation) throws Throwable {
                System.out.println("intercept aaa");
                Object target = invocation.getTarget();
                Object[] args1 = invocation.getArgs();
                return invocation.getMethod().invoke(target, args1);
//                return invocation.proceed();
//                return "asdf";
            }
        }
        Hello hello = new Hello();
        InterfaceDemo wrapHello = (InterfaceDemo)Plugin.wrap(hello, new Plug());
        System.out.println(wrapHello.sayHello("aaa"));
    }

    interface InterfaceDemo {
        String sayHello(String input);
    }

}
