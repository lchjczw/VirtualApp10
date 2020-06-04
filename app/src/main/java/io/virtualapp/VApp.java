package io.virtualapp;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import com.flurry.android.FlurryAgent;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.VASettings;
import com.lody.virtual.helper.utils.VLog;
import com.yc.nonsdk.NonSdkManager;
import io.virtualapp.delegate.MyAppRequestListener;
import io.virtualapp.delegate.MyComponentDelegate;
import io.virtualapp.delegate.MyPhoneInfoDelegate;
import io.virtualapp.delegate.MyTaskDescriptionDelegate;
import jonathanfinerty.once.Once;
import mirror.Reflection;


public class VApp extends MultiDexApplication {
    private static VApp gApp;
    private SharedPreferences mPreferences;
    public static VApp getApp() {
        return gApp;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mPreferences = base.getSharedPreferences("va", Context.MODE_MULTI_PROCESS);
        VASettings.ENABLE_IO_REDIRECT = true;
        VASettings.ENABLE_INNER_SHORTCUT = false;
        NonSdkManager.getInstance().visibleAllApi();
        try {
            Reflection.unseal(base);
            VirtualCore.get().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
            VLog.printThrowable(e);
        }
    }
    @Override
    public void onCreate() {
        gApp = this;
        super.onCreate();
        virtualCore = VirtualCore.get();
        android.util.Log.e("kook","virtualInitializer = " +( virtualInitializer));
        virtualCore.initialize(virtualInitializer);
    }
    VirtualCore virtualCore = null;
    VirtualCore.VirtualInitializer virtualInitializer = new VirtualCore.VirtualInitializer() {
        @Override
        public void onMainProcess() {
            Once.initialise(VApp.this);
                /*new FlurryAgent.Builder()  //数据统计的一个入口点
                        .withLogEnabled(true)
                        .withListener(() -> {
                            // nothing
                        })
                        .build(VApp.this, "48RJJP7ZCZZBB6KMMWW5");*/
        }
        @Override
        public void onVirtualProcess() {
            virtualCore.setComponentDelegate(new MyComponentDelegate());
            virtualCore.setPhoneInfoDelegate(new MyPhoneInfoDelegate());
            virtualCore.setTaskDescriptionDelegate(new MyTaskDescriptionDelegate());
        }
        @Override
        public void onServerProcess() {
            virtualCore.setAppRequestListener(new MyAppRequestListener(VApp.this));
            virtualCore.addVisibleOutsidePackage("com.tencent.mobileqq");
            virtualCore.addVisibleOutsidePackage("com.tencent.mobileqqi");
            virtualCore.addVisibleOutsidePackage("com.tencent.minihd.qq");
            virtualCore.addVisibleOutsidePackage("com.tencent.qqlite");
            virtualCore.addVisibleOutsidePackage("com.facebook.katana");
            virtualCore.addVisibleOutsidePackage("com.whatsapp");
            virtualCore.addVisibleOutsidePackage("com.tencent.mm");
            virtualCore.addVisibleOutsidePackage("com.immomo.momo");
        }
    };
    public static SharedPreferences getPreferences() {
        return getApp().mPreferences;
    }
}
