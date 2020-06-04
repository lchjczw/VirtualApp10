package mirror.android.app;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.IBinder;
import android.os.IInterface;
import com.kook.tools.util.AndroidVersionConstant;
import mirror.MethodParams;
import mirror.MethodParamsOverload;
import mirror.RefBoolean;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.RefStaticMethod;
public class IActivityManager {
    public static Class<?> TYPE = RefClass.load(IActivityManager.class, "android.app.IActivityManager");
    @MethodParams({IBinder.class, boolean.class})
    public static RefMethod<Integer> getTaskForActivity;
    @MethodParams({IBinder.class, int.class})
    public static RefMethod<Void> setRequestedOrientation;
    public static RefMethod<Integer> startActivity;
    @MethodParamsOverload(intRange3 = {0,AndroidVersionConstant.Q_10}, tips = "目前这个属性在android 10 上面没有，但是在android 9 上面也是没有的，android 9 没报错，android 10 报错了，需要查一下")
    public static RefMethod<Integer> startActivities;
    @MethodParamsOverload(value1 = {IBinder.class, int.class, Intent.class},
            value2 = {IBinder.class, int.class, Intent.class, boolean.class},
            value3 = {IBinder.class, int.class, Intent.class, int.class},
            intRange1 = {0, AndroidVersionConstant.Lollipop_5},
            intRange2 = {-(AndroidVersionConstant.Lollipop_5),AndroidVersionConstant.Nougat_7},
            intRange3 = {-(AndroidVersionConstant.Nougat_7),Integer.MAX_VALUE}
            )
    @MethodParams({IBinder.class, int.class, Intent.class})
    public static RefMethod<Boolean> finishActivity;
    public static class ContentProviderHolder {
        public static Class<?> TYPE = RefClass.load(ContentProviderHolder.class, "android.app.IActivityManager$ContentProviderHolder");
        public static RefObject<ProviderInfo> info;
        public static RefObject<IInterface> provider;
        public static RefBoolean noReleaseNeeded;
    }
    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.app.IActivityManager$Stub");
        @MethodParams({IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}
