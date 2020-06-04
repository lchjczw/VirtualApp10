package mirror.android.hardware.fingerprint;
import android.os.*;
import mirror.*;
public class IFingerprintService {
    public static Class<?> TYPE = RefClass.load(IFingerprintService.class, "android.hardware.fingerprint.IFingerprintService");
    public static class Stub {
        public static Class<?> TYPE = RefClass.load(Stub.class, "android.hardware.fingerprint.IFingerprintService$Stub");
        @MethodParams({IBinder.class})
        public static RefStaticMethod<IInterface> asInterface;
    }
}
