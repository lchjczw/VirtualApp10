package mirror.android.app;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import java.io.File;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.MethodParams;
public class ContextImpl {
    public static Class<?> TYPE = RefClass.load(ContextImpl.class, "android.app.ContextImpl");
    @MethodParams({Context.class})
    public static RefObject<String> mBasePackageName;
    public static RefObject<Object> mPackageInfo;
    public static RefObject<PackageManager> mPackageManager;
    public static RefMethod<Context> getReceiverRestrictedContext;
    public static RefMethod<String> getFilesDir;
    public static RefObject<File>  mFilesDir;
    public static RefObject<ContentResolver> mContentResolver;
}
