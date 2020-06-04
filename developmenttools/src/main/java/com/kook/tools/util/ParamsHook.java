package com.kook.tools.util;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class ParamsHook {
    public static Object invokeMethod(Object object, String methodName, Object... values) {
        Method recycleMethod = null;
        Class<?> clazzClz = null;
        Method injectMethod = null;
        if (clazzClz == null) {
            clazzClz = object.getClass();
        }
        if (injectMethod == null) {
            Method[] declaredMethods = clazzClz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (method.getName().equals(methodName)) {
                    injectMethod = method;
                    injectMethod.setAccessible(true);
                }
            }
        }
        try {
            if (injectMethod != null) {
                if (values == null) {
                    return injectMethod.invoke(object);
                } else {
                    return injectMethod.invoke(object, values);
                }
            }else {
                Method[] declaredMethods = clazzClz.getDeclaredMethods();
                for (Method method : declaredMethods) {
                    DebugKook.i("method:"+method.getName());
                }
                DebugKook.e("当前"+methodName+"方法没有找到");
            }
        } catch (IllegalAccessException e) {
            DebugKook.printException(e);
        } catch (InvocationTargetException e) {
            DebugKook.printException(e);
        } catch (Exception e) {
            DebugKook.printException(e);
        }
        return null;
    }
    public static Object invokeParameter(Object object, String parameterName) {
        Class<?> clazzClz = null;
        Field injectField = null;
        if (clazzClz == null) {
            clazzClz = object.getClass();
        }
        if (injectField == null) {
            Field[] declaredFields = clazzClz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.getName().equals(parameterName)) {
                    injectField = field;
                    injectField.setAccessible(true);
                }
            }
        }
        try {
            if (injectField != null) {
                return injectField.get(object);
            }else {
                DebugKook.e("反射属性 "+parameterName+" 未找到");
            }
        } catch (IllegalAccessException e) {
            DebugKook.printException(e);
        } catch (Exception e) {
            DebugKook.printException(e);
        }
        return null;
    }
}
