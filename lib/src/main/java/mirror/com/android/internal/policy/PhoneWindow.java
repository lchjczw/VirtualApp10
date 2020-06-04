package mirror.com.android.internal.policy;
import android.os.Build;
import android.os.IInterface;
import mirror.RefClass;
import mirror.RefStaticObject;
public class PhoneWindow {
    public static Class<?> TYPE;
    public static RefStaticObject<IInterface> sWindowManager;
    static {
        if (android.os.Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            TYPE = RefClass.load(PhoneWindow.class, "com.android.internal.policy.PhoneWindow$WindowManagerHolder");
        }else {
            TYPE = RefClass.load(PhoneWindow.class, "com.android.internal.policy.impl.PhoneWindow$WindowManagerHolder");
            if (TYPE == null) {
                TYPE = RefClass.load(PhoneWindow.class, "com.android.internal.policy.PhoneWindow$WindowManagerHolder");
            }
        }
    }
}