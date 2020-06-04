package mirror.android.net.wifi;
import android.net.wifi.SupplicantState;
import com.kook.tools.util.AndroidVersionConstant;
import java.net.InetAddress;
import mirror.MethodParamsOverload;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefInt;
import mirror.RefObject;
public class WifiInfo {
    public static Class<?> TYPE = RefClass.load(WifiInfo.class, android.net.wifi.WifiInfo.class);
    public static RefConstructor<android.net.wifi.WifiInfo> ctor;
    public static RefObject<String> mMacAddress;
    public static RefInt mNetworkId;
    public static RefInt mLinkSpeed;
    public static RefInt mFrequency;
    public static RefInt mRssi;
    public static RefObject<SupplicantState> mSupplicantState;
    public static RefObject<InetAddress> mIpAddress;
    @MethodParamsOverload(intRange1 = {-(AndroidVersionConstant.JELLY_BEAN_MR1_4_2),Integer.MAX_VALUE},tips = "android 17 开始就是这个属性没有了 mSSID ")
    public static RefObject<Object> mWifiSsid;
    public static RefObject<String> mBSSID;
    @MethodParamsOverload(intRange1 = {0, AndroidVersionConstant.JELLY_BEAN_MR1_4_2},tips = "这个属性在android 17 之后就没有了、这个属性变成了 mWifiSsid")
    public static RefObject<String> mSSID;
}