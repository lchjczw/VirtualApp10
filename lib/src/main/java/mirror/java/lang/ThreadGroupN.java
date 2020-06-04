package mirror.java.lang;
import android.os.IInterface;
import mirror.RefClass;
import mirror.RefInt;
import mirror.RefObject;
import mirror.RefStaticInt;
import mirror.RefStaticObject;
public class ThreadGroupN {
    public static Class<?> Class = RefClass.load(ThreadGroupN.class, java.lang.ThreadGroup.class);
    public static RefInt ngroups;
    public static RefObject<java.lang.ThreadGroup[]> groups;
    public static RefObject<java.lang.ThreadGroup> parent;
}