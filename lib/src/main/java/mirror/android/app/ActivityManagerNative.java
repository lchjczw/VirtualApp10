package mirror.android.app;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.IInterface;
import com.kook.tools.util.AndroidVersionConstant;
import mirror.MethodParamsOverload;
import mirror.RefClass;
import mirror.RefStaticObject;
import mirror.RefStaticMethod;
public class ActivityManagerNative {
    public static Class<?> TYPE = RefClass.load(ActivityManagerNative.class, "android.app.ActivityManagerNative");
    @MethodParamsOverload(intRange1 = {0, AndroidVersionConstant.Oreo_8})
    public static RefStaticObject<Object> gDefault;
    public static RefStaticMethod<IInterface> getDefault;
}