package com.lody.virtual.client.hook.proxies.pm;
import android.content.Context;
import android.os.Build;
import android.os.IInterface;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.Inject;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.Reflect;
import java.lang.reflect.Method;
import mirror.android.app.ActivityThread;
@Inject(MethodProxies.class)
public final class PackageManagerStub extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {
    public PackageManagerStub() {
        super(new MethodInvocationStub<>(ActivityThread.sPackageManager.get()));
    }
    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        addMethodProxy(new ResultStaticMethodProxy("addPermissionAsync", true){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("PackageManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new ResultStaticMethodProxy("addPermission", true){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("PackageManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new ResultStaticMethodProxy("performDexOpt", true){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("PackageManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new ResultStaticMethodProxy("performDexOptIfNeeded", false){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("PackageManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new ResultStaticMethodProxy("performDexOptSecondary", true){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("PackageManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new ResultStaticMethodProxy("addOnPermissionsChangeListener", 0){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("PackageManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        addMethodProxy(new ResultStaticMethodProxy("removeOnPermissionsChangeListener", 0){
            @Override
            public Object call(Object who, Method method, Object... args) throws Throwable {
                VLog.e("PackageManagerStub proxy "+getMethodName());
                return  method.invoke(who, args);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            addMethodProxy(new ResultStaticMethodProxy("checkPackageStartable", 0){
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    VLog.e("PackageManagerStub proxy "+getMethodName());
                    return  method.invoke(who, args);
                }
            });
        }
        if (BuildCompat.isOreo()) {
            addMethodProxy(new ResultStaticMethodProxy("notifyDexLoad", 0){
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    VLog.e("PackageManagerStub proxy "+getMethodName());
                    return  method.invoke(who, args);
                }
            });
            addMethodProxy(new ResultStaticMethodProxy("notifyPackageUse", 0){
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    VLog.e("PackageManagerStub proxy "+getMethodName());
                    return  method.invoke(who, args);
                }
            });
            addMethodProxy(new ResultStaticMethodProxy("setInstantAppCookie", false){
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    VLog.e("PackageManagerStub proxy "+getMethodName());
                    return  method.invoke(who, args);
                }
            });
            addMethodProxy(new ResultStaticMethodProxy("isInstantApp", false){
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    VLog.e("PackageManagerStub proxy "+getMethodName());
                    return  method.invoke(who, args);
                }
            });
        }
    }
    @Override
    public void inject() throws Throwable {
        final IInterface hookedPM = getInvocationStub().getProxyInterface();
        ActivityThread.sPackageManager.set(hookedPM);
        try {
            Context systemContext = Reflect.on(ActivityThread.currentActivityThread.call()).call("getSystemContext").get();
            Object systemContextPm = Reflect.on(systemContext).field("mPackageManager").get();
            if(systemContextPm != null){
                Reflect.on(systemContext).field("mPackageManager").set("mPM",hookedPM);
            }
        }catch (Throwable e){
            e.printStackTrace();
            VLog.printThrowable(e);
        }
        BinderInvocationStub pmHookBinder = new BinderInvocationStub(getInvocationStub().getBaseInterface());
        pmHookBinder.copyMethodProxies(getInvocationStub());
    }
    @Override
    public boolean isEnvBad() {
        return getInvocationStub().getProxyInterface() != ActivityThread.sPackageManager.get();
    }
}
