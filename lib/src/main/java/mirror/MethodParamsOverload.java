package mirror;
import android.os.Build;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodParamsOverload {
    public static String GT = "gt";
    public static String LT = "lt";
    public static String EQ = "eq";
    public static String NE = "ne";
    public static String GE = "ge";
    public static String LE = "le";
    Class<?>[] value1() default {};
    Class<?>[] value2() default {}; 
    Class<?>[] value3() default {}; 
    Class<?>[] value4() default {};
    Class<?>[] value5() default {};
    String[] valueReflect1() default {};
    String[] valueReflect2() default {};
    String[] valueReflect3() default {};
    String[] valueReflect4() default {};
    String[] valueReflect5() default {};
    int[] intRange1() default {};
    int[] intRange2() default {};
    int[] intRange3() default {};
    int[] intRange4() default {};
    int[] intRange5() default {};
    String tips() default "";
}