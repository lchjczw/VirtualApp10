package mirror;
import android.os.Build;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import com.lody.virtual.helper.utils.VLog;
public final class RefClass {
    private static HashMap<Class<?>,Constructor<?>> REF_TYPES = new HashMap<Class<?>, Constructor<?>>();
    static {
        try {
            REF_TYPES.put(RefObject.class, RefObject.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefMethod.class, RefMethod.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefInt.class, RefInt.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefLong.class, RefLong.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefFloat.class, RefFloat.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefDouble.class, RefDouble.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefBoolean.class, RefBoolean.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefStaticObject.class, RefStaticObject.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefStaticInt.class, RefStaticInt.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefStaticMethod.class, RefStaticMethod.class.getConstructor(Class.class, Field.class));
            REF_TYPES.put(RefConstructor.class, RefConstructor.class.getConstructor(Class.class, Field.class));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Class<?> load(Class<?> mappingClass, String className) {
        try {
            return load(mappingClass, Class.forName(className));
        } catch (Exception e) {
            VLog.printException(e);
            return null;
        }
    }
    public static Class load(Class mappingClass, Class<?> realClass) {
        Field[] fields = mappingClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (Modifier.isStatic(field.getModifiers())) {
                    Constructor<?> constructor = REF_TYPES.get(field.getType());
                    if (constructor != null) {
                        boolean performJudgeValue = ParamsOverload.sdkcompatible(field);
                        if (performJudgeValue) {
                            field.set(null, constructor.newInstance(realClass, field));
                        }else {
                            int sdkInt = Build.VERSION.SDK_INT;
                            VLog.i("当前 SDK = "+sdkInt+"，这个版本中 "+mappingClass+"中"+field.getName()+"不满足sdk 的需求，没有了这个属性");
                        }
                    }
                }
            } catch (Exception e) {
                VLog.e(" field.getName:"+field.getName());
                VLog.printException(e);
            }
        }
        return realClass;
    }
}