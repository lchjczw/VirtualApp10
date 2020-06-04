package com.lody.virtual.client.core;
import android.content.Context;
import android.os.Build;
import com.kook.tools.util.EmulatorDetector;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.delegate.AppInstrumentation;
import com.lody.virtual.client.hook.proxies.account.AccountManagerStub;
import com.lody.virtual.client.hook.proxies.alarm.AlarmManagerStub;
import com.lody.virtual.client.hook.proxies.am.ActivityManagerStub;
import com.lody.virtual.client.hook.proxies.am.HCallbackStub;
import com.lody.virtual.client.hook.proxies.appops.AppOpsManagerStub;
import com.lody.virtual.client.hook.proxies.appwidget.AppWidgetManagerStub;
import com.lody.virtual.client.hook.proxies.audio.AudioManagerStub;
import com.lody.virtual.client.hook.proxies.backup.BackupManagerStub;
import com.lody.virtual.client.hook.proxies.bluetooth.BluetoothStub;
import com.lody.virtual.client.hook.proxies.clipboard.ClipBoardStub;
import com.lody.virtual.client.hook.proxies.connectivity.ConnectivityStub;
import com.lody.virtual.client.hook.proxies.content.ContentServiceStub;
import com.lody.virtual.client.hook.proxies.context_hub.ContextHubServiceStub;
import com.lody.virtual.client.hook.proxies.devicepolicy.DevicePolicyManagerStub;
import com.lody.virtual.client.hook.proxies.display.DisplayStub;
import com.lody.virtual.client.hook.proxies.dropbox.DropBoxManagerStub;
import com.lody.virtual.client.hook.proxies.fingerprint.FingerprintManagerStub;
import com.lody.virtual.client.hook.proxies.graphics.GraphicsStatsStub;
import com.lody.virtual.client.hook.proxies.imms.MmsStub;
import com.lody.virtual.client.hook.proxies.input.InputMethodManagerStub;
import com.lody.virtual.client.hook.proxies.isms.ISmsStub;
import com.lody.virtual.client.hook.proxies.isub.ISubStub;
import com.lody.virtual.client.hook.proxies.job.JobServiceStub;
import com.lody.virtual.client.hook.proxies.libcore.LibCoreStub;
import com.lody.virtual.client.hook.proxies.location.LocationManagerStub;
import com.lody.virtual.client.hook.proxies.media.router.MediaRouterServiceStub;
import com.lody.virtual.client.hook.proxies.media.session.SessionManagerStub;
import com.lody.virtual.client.hook.proxies.mount.MountServiceStub;
import com.lody.virtual.client.hook.proxies.network.NetworkManagementStub;
import com.lody.virtual.client.hook.proxies.notification.NotificationManagerStub;
import com.lody.virtual.client.hook.proxies.persistent_data_block.PersistentDataBlockServiceStub;
import com.lody.virtual.client.hook.proxies.phonesubinfo.PhoneSubInfoStub;
import com.lody.virtual.client.hook.proxies.pm.PackageManagerStub;
import com.lody.virtual.client.hook.proxies.power.PowerManagerStub;
import com.lody.virtual.client.hook.proxies.restriction.RestrictionStub;
import com.lody.virtual.client.hook.proxies.search.SearchManagerStub;
import com.lody.virtual.client.hook.proxies.shortcut.ShortcutServiceStub;
import com.lody.virtual.client.hook.proxies.telephony.TelephonyRegistryStub;
import com.lody.virtual.client.hook.proxies.telephony.TelephonyStub;
import com.lody.virtual.client.hook.proxies.usage.UsageStatsManagerStub;
import com.lody.virtual.client.hook.proxies.user.UserManagerStub;
import com.lody.virtual.client.hook.proxies.vibrator.VibratorStub;
import com.lody.virtual.client.hook.proxies.view.AutoFillManagerStub;
import com.lody.virtual.client.hook.proxies.wifi.WifiManagerStub;
import com.lody.virtual.client.hook.proxies.wifi_scanner.WifiScannerStub;
import com.lody.virtual.client.hook.proxies.window.WindowManagerStub;
import com.lody.virtual.client.interfaces.IInjector;
import java.util.HashMap;
import java.util.Map;
import com.lody.virtual.helper.utils.VLog;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.N;
public final class InvocationStubManager {
    private static InvocationStubManager sInstance = new InvocationStubManager();
    private static boolean sInit;
	private Map<Class<?>, IInjector> mInjectors = new HashMap<>(13);
	private InvocationStubManager() {
	}
	public static InvocationStubManager getInstance() {
		return sInstance;
	}
	void injectAll() throws Throwable {
		for (IInjector injector : mInjectors.values()) {
			injector.inject();
		}
		VLog.i("这里的 AppInstrumentation 注入是比较重要的一环，他是生成以及 控制一个应用的关键组件，他会new activity ，也会new application");
		addInjector(AppInstrumentation.getDefault());
		if (VirtualCore.get().isServerProcess()) {
			VLog.i(" ====》  VA 框架服务 注入 完成");
		}
		if (VirtualCore.get().isVAppProcess()) {
			VLog.i(" ====》  虚拟应用 framework 注入 完成");
		}
	}
	public boolean isInit() {
		return sInit;
	}
	public void init(Context context) throws Throwable {
		if (isInit()) {
			throw new IllegalStateException("InvocationStubManager Has been initialized.");
		}
		injectInternal(context);
		sInit = true;
	}
	private void injectInternal(Context context) throws Throwable {
		if (VirtualCore.get().isMainProcess()) {
			return;
		}
		if (VirtualCore.get().isServerProcess()) {
			addInjector(new ActivityManagerStub());
			addInjector(new PackageManagerStub());
			VLog.i(" ====》  VA 框架服务 内部初始化 完成");
			return;
		}
		if (VirtualCore.get().isVAppProcess()) {
			addInjector(new LibCoreStub());
			addInjector(new ActivityManagerStub());
			addInjector(new PackageManagerStub());
			addInjector(HCallbackStub.getDefault());
			addInjector(new ISmsStub());
			addInjector(new ISubStub());
			addInjector(new DropBoxManagerStub());
			addInjector(new NotificationManagerStub());
			addInjector(new LocationManagerStub());
			addInjector(new WindowManagerStub());
			addInjector(new ClipBoardStub());
			addInjector(new MountServiceStub());
			addInjector(new BackupManagerStub());
			addInjector(new TelephonyStub());
			addInjector(new TelephonyRegistryStub());
			addInjector(new PhoneSubInfoStub());
			addInjector(new PowerManagerStub());
			addInjector(new AppWidgetManagerStub());
			addInjector(new AccountManagerStub());
			addInjector(new AudioManagerStub());
			addInjector(new SearchManagerStub());
			addInjector(new ContentServiceStub());
			addInjector(new ConnectivityStub());
			if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR2) {
				addInjector(new VibratorStub());
				addInjector(new WifiManagerStub());
				if (!EmulatorDetector.isEmulator(context)) {
					addInjector(new BluetoothStub());
				}
				addInjector(new ContextHubServiceStub());
			}
			if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR1) {
				addInjector(new UserManagerStub());
			}
			if (Build.VERSION.SDK_INT >= JELLY_BEAN_MR1) {
				addInjector(new DisplayStub());
			}
			if (Build.VERSION.SDK_INT >= LOLLIPOP) {
				if (!EmulatorDetector.isEmulator(context)) {
					addInjector(new PersistentDataBlockServiceStub());
				}
				addInjector(new InputMethodManagerStub());
				addInjector(new MmsStub());
				addInjector(new SessionManagerStub());
				addInjector(new JobServiceStub());
				addInjector(new RestrictionStub());
			}
			if (Build.VERSION.SDK_INT >= KITKAT) {
				addInjector(new AlarmManagerStub());
				addInjector(new AppOpsManagerStub());
				addInjector(new MediaRouterServiceStub());
			}
			if (Build.VERSION.SDK_INT >= LOLLIPOP_MR1) {
				addInjector(new GraphicsStatsStub());
				addInjector(new UsageStatsManagerStub());
			}
			if (Build.VERSION.SDK_INT >= M) {
				addInjector(new FingerprintManagerStub());
				addInjector(new NetworkManagementStub());
			}
			if (Build.VERSION.SDK_INT >= N) {
                addInjector(new WifiScannerStub());
				if (!EmulatorDetector.isEmulator(context)) {
					addInjector(new ShortcutServiceStub()); 
				}
                addInjector(new DevicePolicyManagerStub());
            }
            if (Build.VERSION.SDK_INT >= 26) {
				addInjector(new AutoFillManagerStub());
			}
			VLog.i(" ====》 虚拟应用 内部framework 初始化 完成");
		}
	}
	private void addInjector(IInjector IInjector) {
		mInjectors.put(IInjector.getClass(), IInjector);
	}
	public <T extends IInjector> T findInjector(Class<T> clazz) {
		return (T) mInjectors.get(clazz);
	}
	public <T extends IInjector> void checkEnv(Class<T> clazz) {
		IInjector IInjector = findInjector(clazz);
		if (IInjector != null && IInjector.isEnvBad()) {
			try {
				IInjector.inject();
			} catch (Throwable e) {
				e.printStackTrace();
				VLog.printThrowable(e);
			}
		}
	}
	public <T extends IInjector, H extends MethodInvocationStub> H getInvocationStub(Class<T> injectorClass) {
		T injector = findInjector(injectorClass);
		if (injector != null && injector instanceof MethodInvocationProxy) {
			return (H) ((MethodInvocationProxy) injector).getInvocationStub();
		}
		return null;
	}
}