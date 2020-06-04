package mirror.android.os;
import mirror.RefClass;
import mirror.MethodParams;
import mirror.RefStaticMethod;
public class Process {
    public static final int PROC_SPACE_TERM = (int)' ';
    public static final int PROC_COMBINE = 0x100;
    public static final int PROC_OUT_LONG = 0x2000;
    public static Class<?> TYPE = RefClass.load(Process.class, android.os.Process.class);
    @MethodParams({String.class})
    public static RefStaticMethod<Void> setArgV0;
    @MethodParams({String.class, int[].class,String[].class,long[].class,float[].class})
    public static RefStaticMethod<Boolean> readProcFile;
}
