package com.lody.virtual.helper.compat;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import com.kook.tools.util.FileTools;
import com.kook.tools.util.ParamsHook;
import com.kook.tools.util.RuntimeCommand;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import mirror.com.android.internal.content.NativeLibraryHelper;
import mirror.dalvik.system.VMRuntime;
import com.lody.virtual.helper.utils.VLog;
import static com.lody.virtual.server.pm.installer.PackageHelper.INSTALL_FAILED_NO_MATCHING_ABIS;
public class NativeLibraryHelperCompat {
    private static String TAG = NativeLibraryHelperCompat.class.getSimpleName();
    private static Map<String, Boolean> abiMap = new HashMap<>();
    public static int copyNativeBinaries(File apkFile, File sharedLibraryDir) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return copyNativeBinariesAfterL(apkFile, sharedLibraryDir);
        } else {
            return copyNativeBinariesBeforeL(apkFile, sharedLibraryDir);
        }
    }
    private static int copyNativeBinariesBeforeL(File apkFile, File sharedLibraryDir) {
        try {
            return Reflect.on(NativeLibraryHelper.TYPE).call("copyNativeBinariesIfNeededLI", apkFile, sharedLibraryDir)
                    .get();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }
    @TargetApi(Build.VERSION_CODES.O)
    public static File copyNativeBinariesL(File apkFile, File sharedLibraryDir) {
        VLog.d("copyNativeBinariesAfterL apkFile:" + apkFile.getAbsolutePath() + "    sharedLibraryDir:" + sharedLibraryDir.getAbsolutePath() + "   " + NativeLibraryHelper.Handle.TYPE);
        Object handle = NativeLibraryHelper.Handle.create.call(apkFile);
        if (handle == null) {
            return null;
        }
        boolean multiArch = (boolean) ParamsHook.invokeParameter(handle, "multiArch");
        VLog.i("multiArch:"+multiArch);
        long[] apkHandles = (long[]) ParamsHook.invokeParameter(handle, "apkHandles");
        for(long apkHandle:apkHandles){
            VLog.i("apkHandles apkHandle:"+apkHandle);
        }
        boolean extractNativeLibs = (boolean) ParamsHook.invokeParameter(handle, "extractNativeLibs");
        boolean debuggable  = (boolean) ParamsHook.invokeParameter(handle, "extractNativeLibs");
        VLog.i("handle extractNativeLibs:"+extractNativeLibs);
        VLog.i("handle debuggable:"+debuggable);
        String abi = null;
        String[] supportedAbis = Build.SUPPORTED_ABIS;
        int abiIndex = NativeLibraryHelper.findSupportedAbi.call(handle,supportedAbis);
        VLog.i("kook check abiIndex:" + abiIndex);
        if (abiIndex >= 0) {
            abi = Build.SUPPORTED_ABIS[abiIndex];
        }
        String instructionSet = VMRuntime.getInstructionSet.call(supportedAbis[abiIndex]);
        final File isaSubdir = new File(sharedLibraryDir, instructionSet);
        NativeLibraryHelper.createNativeLibrarySubdir.call(isaSubdir);
        VLog.i("isaSubdir  isaSubdir:" + isaSubdir.getAbsolutePath() + "    abi:" + abi);
        if (abi == null) {
            VLog.e(String.format("没有找到对应拷贝so 的API,这里可能导致的就是应用运行不起来,如微信, Not match any abi [%s].", apkFile.getPath()));
            VLog.e(TAG, "Not match any abi [%s].", apkFile.getPath());
            return null;
        }
        int copyRet = NativeLibraryHelper.copyNativeBinaries.call(handle, isaSubdir, abi);
        VLog.i("copyRet:" + copyRet);
        if (copyRet == 1) {
            return isaSubdir;
        }else {
            return null;
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static int copyNativeBinariesAfterL(File apkFile, File sharedLibraryDir) {
        try {
            VLog.d("copyNativeBinariesAfterL apkFile:" + apkFile.getAbsolutePath() + "    sharedLibraryDir:" + sharedLibraryDir.getAbsolutePath() + "   " + NativeLibraryHelper.Handle.TYPE);
            Object handle = NativeLibraryHelper.Handle.create.call(apkFile);
            if (handle == null) {
                return -1;
            }
            String abi = null;
            Set<String> abiSet = getABIsFromApk(apkFile.getAbsolutePath());
            if (abiSet == null || abiSet.isEmpty()) {
                return 0;
            }
            boolean is64Bit = VMRuntime.is64Bit.call(VMRuntime.getRuntime.call());
            if (is64Bit && isVM64(abiSet)) {
                if (Build.SUPPORTED_64_BIT_ABIS.length > 0) {
                    int abiIndex = NativeLibraryHelper.findSupportedAbi.call(handle, Build.SUPPORTED_64_BIT_ABIS);
                    if (abiIndex >= 0) {
                        abi = Build.SUPPORTED_64_BIT_ABIS[abiIndex];
                    }
                }
            } else {
                if (Build.SUPPORTED_32_BIT_ABIS.length > 0) {
                    int abiIndex = NativeLibraryHelper.findSupportedAbi.call(handle, Build.SUPPORTED_32_BIT_ABIS);
                    String instructionSet = VMRuntime.getInstructionSet.call(Build.SUPPORTED_32_BIT_ABIS[abiIndex]);
                    if (abiIndex >= 0) {
                        abi = Build.SUPPORTED_32_BIT_ABIS[abiIndex];
                    }
                }
            }
            if (abi == null) {
                VLog.e(TAG, "Not match any abi [%s].", apkFile.getPath());
                return -1;
            }
            return NativeLibraryHelper.copyNativeBinaries.call(handle, sharedLibraryDir, abi);
        } catch (Exception e) {
            VLog.d(TAG, "copyNativeBinaries with error : %s", e.getLocalizedMessage());
            VLog.printThrowable(e);
            e.printStackTrace();
        }
        return -1;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean isVM64(Set<String> supportedABIs) {
        if (Build.SUPPORTED_64_BIT_ABIS.length == 0) {
            return false;
        }
        if (supportedABIs == null || supportedABIs.isEmpty()) {
            return true;
        }
        for (String supportedAbi : supportedABIs) {
            if ("arm64-v8a".endsWith(supportedAbi) || "x86_64".equals(supportedAbi) || "mips64".equals(supportedAbi)) {
                return true;
            }
        }
        return false;
    }
    private static Set<String> getABIsFromApk(String apk) {
        try {
            ZipFile apkFile = new ZipFile(apk);
            Enumeration<? extends ZipEntry> entries = apkFile.entries();
            Set<String> supportedABIs = new HashSet<String>();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.contains("../")) {
                    continue;
                }
                if (name.startsWith("lib/") && !entry.isDirectory() && name.endsWith(".so")) {
                    String supportedAbi = name.substring(name.indexOf("/") + 1, name.lastIndexOf("/"));
                    supportedABIs.add(supportedAbi);
                }
            }
            return supportedABIs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void readAPKNativeLib(File apkFile, File sharedLibraryDir) {
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(apkFile);
            Enumeration enumeration = zipFile.entries();
            ZipEntry zipEntry = null;
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                String entryName = zipEntry.getName();
                if (!TextUtils.isEmpty(entryName) && entryName.contains(".so")) {
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    String nativeLibPath = sharedLibraryDir.getParent() + File.separator + zipEntry.getName();
                    String fileNameWithPath = FileTools.getFileNameWithParams(nativeLibPath, 2);
                    String parentPathName = FileTools.getFileNameWithParams(nativeLibPath, 4);
                    boolean containsKey = abiMap.containsKey(parentPathName);
                    if (containsKey && !abiMap.get(parentPathName)) {
                        VLog.i("这里 只会执行一次 ");
                        File file = new File(fileNameWithPath);
                        boolean mkdirs = file.mkdirs();
                        if (mkdirs) {
                            abiMap.put(parentPathName, true);
                        }
                    }else {
                    }
                    FileTools.inputstreamtofile(inputStream, new File(nativeLibPath));
                } else {
                    continue;
                }
            }
            RuntimeCommand.chmod("777",sharedLibraryDir.getAbsolutePath());
            VLog.i("copy complete ");
            zipFile.close();
        } catch (Exception e) {
            VLog.printException(e);
        }
    }
    public static final int NO_NATIVE_LIBRARIES = -114;
    public static final int INSTALL_FAILED_NO_MATCHING_ABIS = -113;
    public static int findSupportedAbi(Object handle, String[] supportedAbis) {
        try {
            VLog.i("=================findSupportedAbi =============== " + NativeLibraryHelper.TYPE.getClass());
            Class typeClass = Class.forName("com.android.internal.content.NativeLibraryHelper");
            Method[] declaredMethods = typeClass.getDeclaredMethods();
            Method nativeFindSupportedAbiMethod = null;
            for (Method method : declaredMethods) {
                VLog.i("method:" + method.getName());
                if (method.getName().equals("nativeFindSupportedAbi")) {
                    method.setAccessible(true);
                    nativeFindSupportedAbiMethod = method;
                }
            }
            int finalRes = NO_NATIVE_LIBRARIES;
            Class<?> handleClass = handle.getClass();
            Field[] declaredFields = handleClass.getDeclaredFields();
            long[] apkHandles = null;
            boolean debuggable = false;
            for (Field field : declaredFields) {
                if (field.getName().equals("apkHandles")) {
                    field.setAccessible(true);
                    apkHandles = (long[]) field.get(handle);
                }
                if (field.getName().equals("debuggable")) {
                    field.setAccessible(true);
                    debuggable = (boolean) field.get(handle);
                }
            }
            if (apkHandles == null) {
                VLog.i("apkHandles is null ,this is return");
                return 0;
            } else {
                String apkHandlesStr = "";
                for (long ah : apkHandles) {
                    apkHandlesStr += ah + "  ;";
                }
                VLog.i("apkHandles :" + apkHandlesStr);
            }
            if (nativeFindSupportedAbiMethod == null) {
                VLog.i("nativeFindSupportedAbiMethod is null ,this is return");
                return 0;
            }
            for (long apkHandle : apkHandles) {
                final int res = (int) nativeFindSupportedAbiMethod.invoke(NativeLibraryHelper.TYPE, apkHandle, supportedAbis, debuggable);
                VLog.i(" res :" + res);
                if (res == NO_NATIVE_LIBRARIES) {
                } else if (res == INSTALL_FAILED_NO_MATCHING_ABIS) {
                    if (finalRes < 0) {
                        finalRes = INSTALL_FAILED_NO_MATCHING_ABIS;
                    }
                } else if (res >= 0) {
                    if (finalRes < 0 || res < finalRes) {
                        finalRes = res;
                    }
                } else {
                    return res;
                }
            }
        } catch (Exception e) {
            VLog.printThrowable(e);
        }
        return 0;
    }
}
