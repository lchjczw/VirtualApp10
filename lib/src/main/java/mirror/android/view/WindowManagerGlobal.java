package mirror.android.view;
import android.os.IInterface;
import mirror.RefClass;
import mirror.RefObject;
import mirror.RefStaticObject;
public class WindowManagerGlobal {
    public static Class<?> TYPE = RefClass.load(WindowManagerGlobal.class, "android.view.WindowManagerGlobal");
    public static RefStaticObject<IInterface> sWindowManagerService;
    public static RefObject<IInterface> mViews;
}
