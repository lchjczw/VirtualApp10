package com.lody.virtual.os;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.helper.ipcbus.IPCBus;
import com.lody.virtual.server.interfaces.IUserManager;
import java.util.List;
public class VUserManager {
    private static String TAG = "VUserManager";
    private final IUserManager mService;
    public static final String DISALLOW_MODIFY_ACCOUNTS = "no_modify_accounts";
    public static final String DISALLOW_CONFIG_WIFI = "no_config_wifi";
    public static final String DISALLOW_INSTALL_APPS = "no_install_apps";
    public static final String DISALLOW_UNINSTALL_APPS = "no_uninstall_apps";
    public static final String DISALLOW_SHARE_LOCATION = "no_share_location";
    public static final String DISALLOW_INSTALL_UNKNOWN_SOURCES = "no_install_unknown_sources";
    public static final String DISALLOW_CONFIG_BLUETOOTH = "no_config_bluetooth";
    public static final String DISALLOW_USB_FILE_TRANSFER = "no_usb_file_transfer";
    public static final String DISALLOW_CONFIG_CREDENTIALS = "no_config_credentials";
    public static final String DISALLOW_REMOVE_USER = "no_remove_user";
    private static VUserManager sInstance = null;
    public synchronized static VUserManager get() {
        if (sInstance == null) {
            IUserManager remote = IPCBus.get(IUserManager.class);
            sInstance = new VUserManager(remote);
        }
        return sInstance;
    }
    public VUserManager(IUserManager service) {
        mService = service;
    }
    public static boolean supportsMultipleUsers() {
        return getMaxSupportedUsers() > 1;
    }
    public int getUserHandle() {
        return VUserHandle.myUserId();
    }
    public String getUserName() {
        try {
            return mService.getUserInfo(getUserHandle()).name;
        } catch (RemoteException re) {
            VLog.printException(re);
            Log.w(TAG, "Could not get user name", re);
            return "";
        }
    }
    public boolean isUserAGoat() {
        return false;
    }
    public VUserInfo getUserInfo(int handle) {
        try {
            return mService.getUserInfo(handle);
        } catch (RemoteException re) {
            VLog.printException(re);
            Log.w(TAG, "Could not get user info", re);
            return null;
        }
    }
    public long getSerialNumberForUser(VUserHandle user) {
        return getUserSerialNumber(user.getIdentifier());
    }
    public VUserHandle getUserForSerialNumber(long serialNumber) {
        int ident = getUserHandle((int)serialNumber);
        return ident >= 0 ? new VUserHandle(ident) : null;
    }
    public VUserInfo createUser(String name, int flags) {
        try {
            return mService.createUser(name, flags);
        } catch (RemoteException re) {
            VLog.printException(re);
            Log.w(TAG, "Could not create a user", re);
            return null;
        }
    }
    public int getUserCount() {
        List<VUserInfo> users = getUsers();
        return users != null ? users.size() : 1;
    }
    public List<VUserInfo> getUsers() {
        try {
            return mService.getUsers(false);
        } catch (RemoteException re) {
            VLog.printException(re);
            Log.w(TAG, "Could not get user list", re);
            return null;
        }
    }
    public List<VUserInfo> getUsers(boolean excludeDying) {
        try {
            return mService.getUsers(excludeDying);
        } catch (RemoteException re) {
            VLog.printException(re);
            Log.w(TAG, "Could not get user list", re);
            return null;
        }
    }
    public boolean removeUser(int handle) {
        try {
            return mService.removeUser(handle);
        } catch (RemoteException re) {
            VLog.printException(re);
            Log.w(TAG, "Could not remove user ", re);
            return false;
        }
    }
    public void setUserName(int handle, String name) {
        try {
            mService.setUserName(handle, name);
        } catch (RemoteException re) {
            VLog.printException(re);
            Log.w(TAG, "Could not set the user name ", re);
        }
    }
    public void setUserIcon(int handle, Bitmap icon) {
        try {
            mService.setUserIcon(handle, icon);
        } catch (RemoteException re) {
            VLog.printException(re);
            Log.w(TAG, "Could not set the user icon ", re);
        }
    }
    public Bitmap getUserIcon(int handle) {
        try {
            return mService.getUserIcon(handle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get the user icon ", re);
            return null;
        }
    }
    public void setGuestEnabled(boolean enable) {
        try {
            mService.setGuestEnabled(enable);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not change guest account availability to " + enable);
        }
    }
    public boolean isGuestEnabled() {
        try {
            return mService.isGuestEnabled();
        } catch (RemoteException re) {
            Log.w(TAG, "Could not retrieve guest enabled state");
            return false;
        }
    }
    public void wipeUser(int handle) {
        try {
            mService.wipeUser(handle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not wipe user " + handle);
        }
    }
    public static int getMaxSupportedUsers() {
        return Integer.MAX_VALUE;
    }
    public int getUserSerialNumber(int handle) {
        try {
            return mService.getUserSerialNumber(handle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get serial number for user " + handle);
        }
        return -1;
    }
    public int getUserHandle(int userSerialNumber) {
        try {
            return mService.getUserHandle(userSerialNumber);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get VUserHandle for user " + userSerialNumber);
        }
        return -1;
    }
}
