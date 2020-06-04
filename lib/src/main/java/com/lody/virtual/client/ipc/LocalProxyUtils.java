package com.lody.virtual.client.ipc;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
public class LocalProxyUtils {
    public static <T> T genProxy(Class<T> interfaceClass, final Object base) {
        if (true) {
            return (T) base;
        }
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{ interfaceClass }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    return method.invoke(base, args);
                } catch (Throwable e) {
                    throw e.getCause() == null ? e : e.getCause();
                }
            }
        });
    }
}
