package mirror.dalvik.system;
import com.kook.tools.util.AndroidVersionConstant;
import java.io.File;
import mirror.MethodParamsOverload;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.MethodParams;
import mirror.RefStaticMethod;
public class VMRuntime {
        public static Class<?> TYPE = RefClass.load(VMRuntime.class, "dalvik.system.VMRuntime");
        public static RefStaticMethod<Object> getRuntime;
        @MethodParams({int.class})
        public static RefMethod<Void> setTargetSdkVersion;
        public static RefMethod<Boolean> is64Bit;
        public static RefStaticMethod<String> getCurrentInstructionSet;
        @MethodParamsOverload(intRange1 = {AndroidVersionConstant.Lollipop_5,Integer.MAX_VALUE})
        public static RefStaticMethod<String> getInstructionSet;
}
