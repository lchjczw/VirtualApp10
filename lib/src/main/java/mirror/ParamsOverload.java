package mirror;
import android.os.Build;
import java.io.File;
import java.lang.reflect.Field;
import static mirror.RefStaticMethod.getProtoType;
public class ParamsOverload {
    public static Class<?>[] methodParamsOverload(Field field){
        if (field.isAnnotationPresent(MethodParams.class) && field.isAnnotationPresent(MethodParamsOverload.class)) {
            MethodParams.SDK sdk_int = field.getAnnotation(MethodParams.class).sdk_int();
            int sdkInt = sdk_int.sdk_int;
            int[] intRange1 = field.getAnnotation(MethodParamsOverload.class).intRange1();
            int[] intRange2 = field.getAnnotation(MethodParamsOverload.class).intRange2();
            int[] intRange3 = field.getAnnotation(MethodParamsOverload.class).intRange3();
            int[] intRange4 = field.getAnnotation(MethodParamsOverload.class).intRange4();
            int[] intRange5 = field.getAnnotation(MethodParamsOverload.class).intRange5();
            boolean intRangeSDK = false;
            intRangeSDK = intRangeSDK(sdkInt, intRange1);
            if (intRangeSDK) {
                Class<?>[] classes = field.getAnnotation(MethodParamsOverload.class).value1();
                if (classes == null || classes.length == 0 ){
                    String[] typeNames = field.getAnnotation(MethodParamsOverload.class).valueReflect1();
                    classes = getValueReflect(typeNames);
                }
                return classes;
            }
            intRangeSDK = intRangeSDK(sdkInt, intRange2);
            if (intRangeSDK) {
                Class<?>[] classes = field.getAnnotation(MethodParamsOverload.class).value2();
                if (classes == null || classes.length == 0 ){
                    String[] typeNames = field.getAnnotation(MethodParamsOverload.class).valueReflect2();
                    classes = getValueReflect(typeNames);
                }
                return classes;
            }
            intRangeSDK = intRangeSDK(sdkInt, intRange3);
            if (intRangeSDK) {
                Class<?>[] classes = field.getAnnotation(MethodParamsOverload.class).value3();
                if (classes == null || classes.length == 0 ){
                    String[] typeNames = field.getAnnotation(MethodParamsOverload.class).valueReflect3();
                    classes = getValueReflect(typeNames);
                }
                return classes;
            }
            intRangeSDK = intRangeSDK(sdkInt, intRange4);
            if (intRangeSDK) {
                Class<?>[] classes = field.getAnnotation(MethodParamsOverload.class).value4();
                if (classes == null || classes.length == 0 ){
                    String[] typeNames = field.getAnnotation(MethodParamsOverload.class).valueReflect4();
                    classes = getValueReflect(typeNames);
                }
                return classes;
            }
            intRangeSDK = intRangeSDK(sdkInt, intRange5);
            if (intRangeSDK) {
                Class<?>[] classes = field.getAnnotation(MethodParamsOverload.class).value5();
                if (classes == null || classes.length == 0 ){
                    String[] typeNames = field.getAnnotation(MethodParamsOverload.class).valueReflect5();
                    classes = getValueReflect(typeNames);
                }
                return classes;
            }
        }
        return null;
    }
    public static Class<?>[] getValueReflect(String[] typeNames){
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
        return types;
    }
    public static boolean sdkcompatible(Field field){
        if (field.isAnnotationPresent(MethodParams.class) && field.isAnnotationPresent(MethodParamsOverload.class)) {
            MethodParams.SDK sdk_int = field.getAnnotation(MethodParams.class).sdk_int();
            int sdkInt = sdk_int.sdk_int;
            int[] intRange1 = field.getAnnotation(MethodParamsOverload.class).intRange1();
            int[] intRange2 = field.getAnnotation(MethodParamsOverload.class).intRange2();
            int[] intRange3 = field.getAnnotation(MethodParamsOverload.class).intRange3();
            int[] intRange4 = field.getAnnotation(MethodParamsOverload.class).intRange4();
            int[] intRange5 = field.getAnnotation(MethodParamsOverload.class).intRange5();
            boolean intRangeSDK = false;
            intRangeSDK = intRangeSDK(sdkInt, intRange1);
            if (intRangeSDK){
                return intRangeSDK;
            }
            intRangeSDK = intRangeSDK(sdkInt,intRange2);
            if (intRangeSDK){
                return intRangeSDK;
            }
            intRangeSDK = intRangeSDK(sdkInt,intRange3);
            if (intRangeSDK){
                return intRangeSDK;
            }
            intRangeSDK = intRangeSDK(sdkInt,intRange4);
            if (intRangeSDK){
                return intRangeSDK;
            }
            intRangeSDK = intRangeSDK(sdkInt,intRange5);
            if (intRangeSDK){
                return intRangeSDK;
            }
            return intRangeSDK;
        }else if (field.isAnnotationPresent(MethodParamsOverload.class)){
            int sdkInt = Build.VERSION.SDK_INT;
            int[] intRange1 = field.getAnnotation(MethodParamsOverload.class).intRange1();
            int[] intRange2 = field.getAnnotation(MethodParamsOverload.class).intRange2();
            int[] intRange3 = field.getAnnotation(MethodParamsOverload.class).intRange3();
            int[] intRange4 = field.getAnnotation(MethodParamsOverload.class).intRange4();
            int[] intRange5 = field.getAnnotation(MethodParamsOverload.class).intRange5();
            boolean intRangeSDK = false;
            intRangeSDK = intRangeSDK(sdkInt, intRange1);
            if (intRangeSDK){
                return intRangeSDK;
            }
            intRangeSDK = intRangeSDK(sdkInt,intRange2);
            if (intRangeSDK){
                return intRangeSDK;
            }
            intRangeSDK = intRangeSDK(sdkInt,intRange3);
            if (intRangeSDK){
                return intRangeSDK;
            }
            intRangeSDK = intRangeSDK(sdkInt,intRange4);
            if (intRangeSDK){
                return intRangeSDK;
            }
            intRangeSDK = intRangeSDK(sdkInt,intRange5);
            if (intRangeSDK){
                return intRangeSDK;
            }
            return intRangeSDK;
        }
        return true;
    }
    public static boolean intRangeSDK(int sdk_int,int[] intRange){
        if (intRange.length ==2){
            int from = intRange[0];
            int to = intRange[1];
            boolean eqFrom = false;
            if (from < 0){
                eqFrom = true;
            }
            boolean eqTo = false;
            if (to < 0){
                eqTo = true;
            }
            boolean minFrom = false;
            if (sdk_int > from){
                minFrom = true;
            }else if (eqFrom && sdk_int == from){ 
                minFrom = true;
            }
            boolean maxTo = false;
            if (sdk_int < to){
                maxTo = true;
            }else if (eqTo && sdk_int == to){
                maxTo = true;
            }
            return minFrom && maxTo;
        }
        return false;
    }
}
