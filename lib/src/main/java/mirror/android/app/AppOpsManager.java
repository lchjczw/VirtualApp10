package mirror.android.app;
import android.os.IInterface;
import com.kook.tools.util.AndroidVersionConstant;
import mirror.MethodParamsOverload;
import mirror.RefClass;
import mirror.RefObject;
import mirror.RefStaticObject;
public class AppOpsManager {
    public static Class<?> TYPE = RefClass.load(AppOpsManager.class, "android.app.AppOpsManager");
    @MethodParamsOverload(intRange1 = {-(AndroidVersionConstant.Q_10),Integer.MAX_VALUE})
    public static RefObject<IInterface> mService;
}
