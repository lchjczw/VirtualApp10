package mirror.android.app;
import java.io.File;
import mirror.RefClass;
import mirror.RefObject;
public class ContextImplOreo {
    public static Class<?> TYPE = RefClass.load(ContextImplOreo.class, "android.app.ContextImpl");
    public static RefObject<String> mOpPackageName;
}
