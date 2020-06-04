package mirror;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import com.lody.virtual.helper.utils.VLog;
import static mirror.RefStaticMethod.getProtoType;
@SuppressWarnings("unchecked")
public class RefMethod<T> {
    private Method method;
    boolean isPrint = false;
    public RefMethod(Class<?> cls, Field field) throws NoSuchMethodException {
        if (field.isAnnotationPresent(MethodParams.class)) {
            Class<?>[] types = field.getAnnotation(MethodParams.class).value();
            Class<?>[] methodParamsOverload = ParamsOverload.methodParamsOverload(field);
            if (methodParamsOverload != null){
                types = methodParamsOverload;
            }
            for (int i = 0; i < types.length; i++) {
                Class<?> clazz = types[i];
                if (clazz.getClassLoader() == getClass().getClassLoader()) {
                    try {
                        Class.forName(clazz.getName());
                        Class<?> realClass = (Class<?>) clazz.getField("TYPE").get(null);
                        types[i] = realClass;
                        VLog.e("realClass:"+realClass);
                    } catch (Throwable e) {
                        VLog.printThrowable(e);
                        throw new RuntimeException(e);
                    }
                }
            }
            this.method = cls.getDeclaredMethod(field.getName(), types);
            this.method.setAccessible(true);
        } else if (field.isAnnotationPresent(MethodReflectParams.class)) {
            String[] typeNames = field.getAnnotation(MethodReflectParams.class).value();
            Class<?>[] types = new Class<?>[typeNames.length];
            for (int i = 0; i < typeNames.length; i++) {
                Class<?> type = getProtoType(typeNames[i]);
                if (type == null) {
                    try {
                        type = Class.forName(typeNames[i]);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                types[i] = type;
            }
            this.method = cls.getDeclaredMethod(field.getName(), types);
            this.method.setAccessible(true);
        } else {
            for (Method method : cls.getDeclaredMethods()) {
                if (method.getName().equals(field.getName())) {
                    this.method = method;
                    this.method.setAccessible(true);
                    break;
                }
            }
        }
        if (this.method == null) {
            throw new NoSuchMethodException(field.getName());
        }
    }
    public T call(Object receiver, Object... args) {
        try {
            return (T) this.method.invoke(receiver, args);
        } catch (InvocationTargetException e) {
            VLog.printException(e);
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            } else {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            Type[] genericExceptionTypes = method.getGenericExceptionTypes();
            for (Object arg:genericExceptionTypes){
            }
            e.printStackTrace();
            for (Object arg:args){
            }
            VLog.printThrowable(e);
        }
        return null;
    }
    public T callWithException(Object receiver, Object... args) throws Throwable {
        try {
            return (T) this.method.invoke(receiver, args);
        } catch (InvocationTargetException e) {
            VLog.printException(e);
            if (e.getCause() != null) {
                throw e.getCause();
            }
            throw e;
        }
    }
    public Class<?>[] paramList() {
        return method.getParameterTypes();
    }
}