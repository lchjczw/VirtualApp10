package com.lody.virtual.helper.utils;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
public class Reflect {
    private final Object object;
    private final boolean isClass;
    private Reflect(Class<?> type) {
        this.object = type;
        this.isClass = true;
    }
    private Reflect(Object object) {
        this.object = object;
        this.isClass = false;
    }
    public static Reflect on(String name) throws ReflectException {
        return on(forName(name));
    }
    public static Reflect on(String name, ClassLoader classLoader) throws ReflectException {
        return on(forName(name, classLoader));
    }
    public static Reflect on(Class<?> clazz) {
        return new Reflect(clazz);
    }
    public static Reflect on(Object object) {
        return new Reflect(object);
    }
    public static <T extends AccessibleObject> T accessible(T accessible) {
        if (accessible == null) {
            return null;
        }
        if (accessible instanceof Member) {
            Member member = (Member) accessible;
            if (Modifier.isPublic(member.getModifiers())
                    && Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
                return accessible;
            }
        }
        if (!accessible.isAccessible()) {
            accessible.setAccessible(true);
        }
        return accessible;
    }
    private static String property(String string) {
        int length = string.length();
        if (length == 0) {
            return "";
        } else if (length == 1) {
            return string.toLowerCase();
        } else {
            return string.substring(0, 1).toLowerCase() + string.substring(1);
        }
    }
    private static Reflect on(Constructor<?> constructor, Object... args) throws ReflectException {
        try {
            return on(accessible(constructor).newInstance(args));
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }
    private static Reflect on(Method method, Object object, Object... args) throws ReflectException {
        try {
            accessible(method);
            if (method.getReturnType() == void.class) {
                method.invoke(object, args);
                return on(object);
            } else {
                return on(method.invoke(object, args));
            }
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }
    private static Object unwrap(Object object) {
        if (object instanceof Reflect) {
            return ((Reflect) object).get();
        }
        return object;
    }
    private static Class<?>[] types(Object... values) {
        if (values == null) {
            return new Class[0];
        }
        Class<?>[] result = new Class[values.length];
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            result[i] = value == null ? NULL.class : value.getClass();
        }
        return result;
    }
    private static Class<?> forName(String name) throws ReflectException {
        try {
            return Class.forName(name);
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }
    private static Class<?> forName(String name, ClassLoader classLoader) throws ReflectException {
        try {
            return Class.forName(name, true, classLoader);
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }
    public static Class<?> wrapper(Class<?> type) {
        if (type == null) {
            return null;
        } else if (type.isPrimitive()) {
            if (boolean.class == type) {
                return Boolean.class;
            } else if (int.class == type) {
                return Integer.class;
            } else if (long.class == type) {
                return Long.class;
            } else if (short.class == type) {
                return Short.class;
            } else if (byte.class == type) {
                return Byte.class;
            } else if (double.class == type) {
                return Double.class;
            } else if (float.class == type) {
                return Float.class;
            } else if (char.class == type) {
                return Character.class;
            } else if (void.class == type) {
                return Void.class;
            }
        }
        return type;
    }
    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) object;
    }
    public Reflect set(String name, Object value) throws ReflectException {
        try {
            Field field = field0(name);
            field.setAccessible(true);
            field.set(object, unwrap(value));
            return this;
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }
    public <T> T get(String name) throws ReflectException {
        return field(name).get();
    }
    public Reflect field(String name) throws ReflectException {
        try {
            Field field = field0(name);
            return on(field.get(object));
        } catch (Exception e) {
            throw new ReflectException(object.getClass().getName(), e);
        }
    }
    private Field field0(String name) throws ReflectException {
        Class<?> type = type();
        try {
            return type.getField(name);
        }
        catch (NoSuchFieldException e) {
            do {
                try {
                    return accessible(type.getDeclaredField(name));
                } catch (NoSuchFieldException ignore) {
                }
                type = type.getSuperclass();
            } while (type != null);
            throw new ReflectException(e);
        }
    }
    public Map<String, Reflect> fields() {
        Map<String, Reflect> result = new LinkedHashMap<String, Reflect>();
        Class<?> type = type();
        do {
            for (Field field : type.getDeclaredFields()) {
                if (!isClass ^ Modifier.isStatic(field.getModifiers())) {
                    String name = field.getName();
                    if (!result.containsKey(name))
                        result.put(name, field(name));
                }
            }
            type = type.getSuperclass();
        } while (type != null);
        return result;
    }
    public Reflect call(String name) throws ReflectException {
        return call(name, new Object[0]);
    }
    public Reflect call(String name, Object... args) throws ReflectException {
        Class<?>[] types = types(args);
        try {
            Method method = exactMethod(name, types);
            return on(method, object, args);
        } catch (NoSuchMethodException e) {
            try {
                Method method = similarMethod(name, types);
                return on(method, object, args);
            } catch (NoSuchMethodException e1) {
                throw new ReflectException(e1);
            }
        }
    }
    public Method exactMethod(String name, Class<?>[] types) throws NoSuchMethodException {
        Class<?> type = type();
        try {
            return type.getMethod(name, types);
        } catch (NoSuchMethodException e) {
            do {
                try {
                    return type.getDeclaredMethod(name, types);
                } catch (NoSuchMethodException ignore) {
                }
                type = type.getSuperclass();
            } while (type != null);
            throw new NoSuchMethodException();
        }
    }
    private Method similarMethod(String name, Class<?>[] types) throws NoSuchMethodException {
        Class<?> type = type();
        for (Method method : type.getMethods()) {
            if (isSimilarSignature(method, name, types)) {
                return method;
            }
        }
        do {
            for (Method method : type.getDeclaredMethods()) {
                if (isSimilarSignature(method, name, types)) {
                    return method;
                }
            }
            type = type.getSuperclass();
        } while (type != null);
        throw new NoSuchMethodException("No similar method " + name + " with params " + Arrays.toString(types)
                + " could be found on type " + type() + ".");
    }
    private boolean isSimilarSignature(Method possiblyMatchingMethod, String desiredMethodName,
                                       Class<?>[] desiredParamTypes) {
        return possiblyMatchingMethod.getName().equals(desiredMethodName)
                && match(possiblyMatchingMethod.getParameterTypes(), desiredParamTypes);
    }
    public Reflect create() throws ReflectException {
        return create(new Object[0]);
    }
    public Reflect create(Object... args) throws ReflectException {
        Class<?>[] types = types(args);
        try {
            Constructor<?> constructor = type().getDeclaredConstructor(types);
            return on(constructor, args);
        } catch (NoSuchMethodException e) {
            for (Constructor<?> constructor : type().getDeclaredConstructors()) {
                if (match(constructor.getParameterTypes(), types)) {
                    return on(constructor, args);
                }
            }
            throw new ReflectException(e);
        }
    }
    @SuppressWarnings("unchecked")
    public <P> P as(Class<P> proxyType) {
        final boolean isMap = (object instanceof Map);
        final InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                try {
                    return on(object).call(name, args).get();
                } catch (ReflectException e) {
                    if (isMap) {
                        Map<String, Object> map = (Map<String, Object>) object;
                        int length = (args == null ? 0 : args.length);
                        if (length == 0 && name.startsWith("get")) {
                            return map.get(property(name.substring(3)));
                        } else if (length == 0 && name.startsWith("is")) {
                            return map.get(property(name.substring(2)));
                        } else if (length == 1 && name.startsWith("set")) {
                            map.put(property(name.substring(3)), args[0]);
                            return null;
                        }
                    }
                    throw e;
                }
            }
        };
        return (P) Proxy.newProxyInstance(proxyType.getClassLoader(), new Class[]{proxyType}, handler);
    }
    private boolean match(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        if (declaredTypes.length == actualTypes.length) {
            for (int i = 0; i < actualTypes.length; i++) {
                if (actualTypes[i] == NULL.class)
                    continue;
                if (wrapper(declaredTypes[i]).isAssignableFrom(wrapper(actualTypes[i])))
                    continue;
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    public int hashCode() {
        return object.hashCode();
    }
    public boolean equals(Object obj) {
        return obj instanceof Reflect && object.equals(((Reflect) obj).get());
    }
    public String toString() {
        return object.toString();
    }
    public Class<?> type() {
        if (isClass) {
            return (Class<?>) object;
        } else {
            return object.getClass();
        }
    }
    public static String getMethodDetails(Method method) {
        StringBuilder sb = new StringBuilder(40);
        sb.append(Modifier.toString(method.getModifiers()))
                .append(" ")
                .append(method.getReturnType().getName())
                .append(" ")
                .append(method.getName())
                .append("(");
        Class<?>[] parameters = method.getParameterTypes();
        for (Class<?> parameter : parameters) {
            sb.append(parameter.getName()).append(", ");
        }
        if (parameters.length > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(")");
        return sb.toString();
    }
    private static class NULL {
    }
    public Reflect callBest(String name, Object... args) throws ReflectException {
        Class<?>[] types = types(args);
        Class<?> type = type();
        Method bestMethod = null;
        int level = 0;
        for (Method method : type.getDeclaredMethods()) {
            if (isSimilarSignature(method, name, types)) {
                bestMethod = method;
                level = 2;
                break;
            }
            if (matchObjectMethod(method, name, types)) {
                bestMethod = method;
                level = 1;
                continue;
            }
            if (method.getName().equals(name) && method.getParameterTypes().length == 0 && level == 0) {
                bestMethod = method;
            }
        }
        if (bestMethod != null) {
            if (level == 0) {
                args = new Object[0];
            }
            if (level == 1) {
                Object[] args2 = {args};
                args = args2;
            }
            return on(bestMethod, object, args);
        } else {
            throw new ReflectException("no method found for " + name, new NoSuchMethodException("No best method " + name + " with params " + Arrays.toString(types)
                    + " could be found on type " + type() + "."));
        }
    }
    private boolean matchObjectMethod(Method possiblyMatchingMethod, String desiredMethodName,
                                      Class<?>[] desiredParamTypes) {
        return possiblyMatchingMethod.getName().equals(desiredMethodName)
                && matchObject(possiblyMatchingMethod.getParameterTypes());
    }
    private boolean matchObject(Class<?>[] parameterTypes) {
        Class<Object[]> c = Object[].class;
        return parameterTypes.length > 0 && parameterTypes[0].isAssignableFrom(c);
    }
}
