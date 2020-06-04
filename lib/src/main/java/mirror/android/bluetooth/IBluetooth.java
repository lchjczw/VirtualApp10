package mirror.android.bluetooth;
import android.os.IBinder;
import android.os.IInterface;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;
public class IBluetooth {
    public static Class<?> TYPE = RefClass.load(IBluetooth.class, "android.bluetooth.IBluetooth");
    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.bluetooth.IBluetooth$Stub");
        @MethodParams({IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}
