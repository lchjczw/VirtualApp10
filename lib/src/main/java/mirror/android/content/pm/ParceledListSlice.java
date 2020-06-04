package mirror.android.content.pm;
import android.os.Parcelable;
import com.kook.tools.util.AndroidVersionConstant;
import java.util.List;
import mirror.MethodParams;
import mirror.MethodParamsOverload;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefMethod;
import mirror.RefStaticObject;
public class ParceledListSlice {
    public static RefStaticObject<Parcelable.Creator> CREATOR;
    public static Class<?> TYPE = RefClass.load(ParceledListSlice.class, "android.content.pm.ParceledListSlice");
    @MethodParamsOverload(
            value1 = {List.class},
            intRange1 = {AndroidVersionConstant.JELLY_BEAN_MR2_4_3,Integer.MAX_VALUE}
    )
    @MethodParams({List.class})
    public static RefConstructor<Parcelable> ctor;
    public static RefMethod<List<?>> getList;
    @MethodParamsOverload(intRange1 = {0, AndroidVersionConstant.Lollipop_5})
    public static RefMethod<Boolean> append;
    @MethodParamsOverload(intRange1 = {0,AndroidVersionConstant.JELLY_BEAN_MR2_4_3})
    public static RefMethod<Void> setLastSlice;
}
