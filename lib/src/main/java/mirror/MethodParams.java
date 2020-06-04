package mirror;
import android.os.Build;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodParams {
    public static String GT = "gt";
    public static String LT = "lt";
    public static String EQ = "eq";
    public static String NE = "ne";
    public static String GE = "ge";
    public static String LE = "le";
    Class<?>[] value();
    public enum SDK{
        SDK_INT(Build.VERSION.SDK_INT);
        int sdk_int;
        private SDK(int sdk){
            this.sdk_int = sdk;
        }
    }
    SDK sdk_int() default SDK.SDK_INT;
}