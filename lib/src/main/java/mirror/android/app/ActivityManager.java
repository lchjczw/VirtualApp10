package mirror.android.app;
import android.os.IInterface;
import com.kook.tools.util.AndroidVersionConstant;
import mirror.MethodParamsOverload;
import mirror.RefClass;
import mirror.RefStaticMethod;
import mirror.RefStaticObject;
public class ActivityManager {
    public static Class<?> TYPE = RefClass.load(ActivityManager.class, "android.app.ActivityManager");
    @MethodParamsOverload(intRange1 = {-(AndroidVersionConstant.Oreo_8),Integer.MAX_VALUE})
    public static RefStaticMethod<IInterface> getService;
    @MethodParamsOverload(intRange1 = {-(AndroidVersionConstant.Oreo_8),Integer.MAX_VALUE})
    public static RefStaticObject<Object> IActivityManagerSingleton;
    @MethodParamsOverload(intRange1 = {0,AndroidVersionConstant.Oreo_8})
    public static RefStaticObject<Object> gDefault;
    @MethodParamsOverload(intRange1 = {0,AndroidVersionConstant.Oreo_8})
    public static RefStaticMethod<IInterface> getDefault;
}
