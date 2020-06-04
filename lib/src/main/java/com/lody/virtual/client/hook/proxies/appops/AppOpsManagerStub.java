package com.lody.virtual.client.hook.proxies.appops;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import com.kook.tools.util.AndroidVersionConstant;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;
import mirror.android.app.ActivityManager;
import mirror.android.os.ServiceManager;
import mirror.com.android.internal.app.IAppOpsService;
@TargetApi(Build.VERSION_CODES.KITKAT)
public class AppOpsManagerStub extends BinderInvocationProxy {
    public AppOpsManagerStub() {
        super(IAppOpsService.Stub.asInterface, Context.APP_OPS_SERVICE);
    }
    @Override
    public void inject() throws Throwable {
        VLog.i(" AppOpsManagerStub inject");
        if (Build.VERSION.SDK_INT == AndroidVersionConstant.Q_10) {
            VLog.i(" AppOpsManagerStub代理对象 代理  AppOpsManager.mService 中的对象 ");
        }
    }
    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        VLog.e("============== AppOpsManagerStub onBindMethods ==================   isVAppProcess:"+ VirtualCore.get().isVAppProcess());
        addMethodProxy(new BaseMethodProxy("checkOperation", 1, 2){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new BaseMethodProxy("noteOperation", 1, 2){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new BaseMethodProxy("startOperation", 2, 3){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new BaseMethodProxy("finishOperation", 2, 3){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new BaseMethodProxy("startWatchingMode", -1, 1){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new BaseMethodProxy("getOpsForPackage", 0, 1){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new BaseMethodProxy("setMode", 1, 2){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new BaseMethodProxy("checkAudioOperation", 2, 3){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new BaseMethodProxy("setAudioRestriction", 2, -1){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new ReplaceLastPkgMethodProxy("resetAllModes"){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new MethodProxy() {
            @Override
            public String getMethodName() {
                return "noteProxyOperation";
            }
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("AppOpsManagerStub proxy "+getMethodName());
                return 0;
            }
        });
        addMethodProxy(new StaticMethodProxy("checkPackage"){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("================老子就只需要这里能走过去而已=======================");
                Object mode = method.invoke(who, args);
                return mode;
            }
        });
    }
    private class BaseMethodProxy extends StaticMethodProxy {
        final int pkgIndex;
        final int uidIndex;
        BaseMethodProxy(String name, int uidIndex, int pkgIndex) {
            super(name);
            this.pkgIndex = pkgIndex;
            this.uidIndex = uidIndex;
        }
        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            VLog.e("====================== AppOpsManagerStub call ============================ "+method.getName());
            return super.call(who, method, args);
        }
        @Override
        public boolean beforeCall(Object who, Method method, Object... args) {
            VLog.e("======================checkPackage============================1 ===");
            if (pkgIndex != -1 && args.length > pkgIndex && args[pkgIndex] instanceof String) {
                args[pkgIndex] = getHostPkg();
            }
            if (uidIndex != -1 && args[uidIndex] instanceof Integer) {
                args[uidIndex] = getRealUid();
            }
            return true;
        }
    }
}
