package mirror.android.app;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.IInterface;
import com.kook.tools.util.AndroidVersionConstant;
import java.util.List;
import mirror.MethodParamsOverload;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.MethodParams;
import mirror.RefStaticMethod;
import mirror.android.app.backup.IBackupManager;
import mirror.android.content.res.CompatibilityInfo;
public class IApplicationThread {
    public static Class<?> TYPE = RefClass.load(IApplicationThread.class, "android.app.IApplicationThread");
    @MethodParams({List.class, IBinder.class})
    public static RefMethod<Void> scheduleNewIntent;
    @MethodParamsOverload(
            value1 = {IBinder.class, ServiceInfo.class},
            value2 = {IBinder.class, ServiceInfo.class, CompatibilityInfo.class},
            value3 = {IBinder.class, ServiceInfo.class, CompatibilityInfo.class, int.class},
            intRange1 = {0, AndroidVersionConstant.ICE_CREAM_SANDWICH_MR1_4_0_1},
            intRange2 = {-(AndroidVersionConstant.ELLY_BEAN_4_1),AndroidVersionConstant.KITKAT_4_4},
            intRange3 = {-(AndroidVersionConstant.KITKAT_4_4),Integer.MAX_VALUE}
    )
    @MethodParams({IBinder.class, ServiceInfo.class})
    public static RefMethod<Void> scheduleCreateService;
    @MethodParamsOverload(
            value1 = {IBinder.class, Intent.class, boolean.class},
            value2 = {IBinder.class, Intent.class, boolean.class, int.class},
            intRange1 = {0, AndroidVersionConstant.ICE_CREAM_SANDWICH_MR1_4_0_1},
            intRange2 = {-(AndroidVersionConstant.KITKAT_4_4),Integer.MAX_VALUE}
    )
    @MethodParams({IBinder.class, Intent.class, boolean.class})
    public static RefMethod<Void> scheduleBindService;
    @MethodParams({IBinder.class, Intent.class})
    public static RefMethod<Void> scheduleUnbindService;
    @MethodParamsOverload(
            value1 = {IBinder.class, int.class, int.class, Intent.class},
            value2 = {IBinder.class, ServiceInfo.class, CompatibilityInfo.class},
            valueReflect3 = {"android.os.IBinder", "android.content.pm.ParceledListSlice"},
            intRange1 = {0, AndroidVersionConstant.ICE_CREAM_SANDWICH_MR1_4_0_1},
            intRange2 = {-(AndroidVersionConstant.ICE_CREAM_SANDWICH_MR1_4_0_1),AndroidVersionConstant.Oreo_8},
            intRange3 = {-(AndroidVersionConstant.Oreo_8),Integer.MAX_VALUE}
    )
    @MethodParams({IBinder.class, int.class, int.class, Intent.class})
    public static RefMethod<Void> scheduleServiceArgs;
    @MethodParams({IBinder.class})
    public static RefMethod<Void> scheduleStopService;
    public static class Stub {
        public static Class<?> TYPE = RefClass.load(IApplicationThread.Stub.class, "android.app.IApplicationThread$Stub");
        @MethodParams({IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}
