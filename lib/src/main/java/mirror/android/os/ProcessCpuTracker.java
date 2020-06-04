package mirror.android.os;
import android.os.IBinder;
import android.os.IInterface;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;
import mirror.android.os.Process;
import static mirror.android.os.Process.PROC_COMBINE;
import static mirror.android.os.Process.PROC_OUT_LONG;
import static mirror.android.os.Process.PROC_SPACE_TERM;
public class ProcessCpuTracker {
    public static final int[] SYSTEM_CPU_FORMAT = new int[] {
            PROC_SPACE_TERM|PROC_COMBINE,
            PROC_SPACE_TERM|PROC_OUT_LONG,                  
            PROC_SPACE_TERM|PROC_OUT_LONG,                  
            PROC_SPACE_TERM|PROC_OUT_LONG,                  
            PROC_SPACE_TERM|PROC_OUT_LONG,                  
            PROC_SPACE_TERM|PROC_OUT_LONG,                  
            PROC_SPACE_TERM|PROC_OUT_LONG,                  
            PROC_SPACE_TERM|PROC_OUT_LONG                   
    };
    public static final long[] mSystemCpuData = new long[7];
    public static Class<?> TYPE = RefClass.load(ProcessCpuTracker.class, "com.android.internal.os.ProcessCpuTracker");
    public static class Stats {
        public static Class<?> TYPE = RefClass.load(ProcessCpuTracker.Stats.class, "com.android.internal.os.ProcessCpuTracker$Stats");
    }
}
