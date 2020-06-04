package com.lody.virtual.client.ipc;
import android.os.RemoteException;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.helper.ipcbus.IPCSingleton;
import com.lody.virtual.remote.vloc.VCell;
import com.lody.virtual.remote.vloc.VLocation;
import com.lody.virtual.server.interfaces.IVirtualLocationManager;
import java.util.List;
import com.lody.virtual.helper.utils.VLog;
public class VirtualLocationManager {
    private static final VirtualLocationManager sInstance = new VirtualLocationManager();
    private IPCSingleton<IVirtualLocationManager> singleton = new IPCSingleton<>(IVirtualLocationManager.class);
    public static final int MODE_CLOSE = 0;
    public static final int MODE_USE_GLOBAL = 1;
    public static final int MODE_USE_SELF = 2;
    public static VirtualLocationManager get() {
        return sInstance;
    }
    public IVirtualLocationManager getService() {
        return singleton.get();
    }
    public int getMode(int userId, String pkg) {
        try {
            return getService().getMode(userId, pkg);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }
    public int getMode() {
        return getMode(MethodProxy.getAppUserId(), MethodProxy.getAppPkg());
    }
    public void setMode(int userId, String pkg, int mode) {
        try {
            getService().setMode(userId, pkg, mode);
        } catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }
    public void setCell(int userId, String pkg, VCell cell) {
        try {
            getService().setCell(userId, pkg, cell);
        } catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }
    public void setAllCell(int userId, String pkg, List<VCell> cell) {
        try {
            getService().setAllCell(userId, pkg, cell);
        } catch (RemoteException e) {
            VLog.printException(e);
            VirtualRuntime.crash(e);
        }
    }
    public void setNeighboringCell(int userId, String pkg, List<VCell> cell) {
        try {
            getService().setNeighboringCell(userId, pkg, cell);
        } catch (RemoteException e) {
            VLog.printException(e);
            VirtualRuntime.crash(e);
        }
    }
    public VCell getCell(int userId, String pkg) {
        try {
            return getService().getCell(userId, pkg);
        } catch (RemoteException e) {
            VLog.printException(e);
            return VirtualRuntime.crash(e);
        }
    }
    public List<VCell> getAllCell(int userId, String pkg) {
        try {
            return getService().getAllCell(userId, pkg);
        } catch (RemoteException e) {
            VLog.printException(e);
            return VirtualRuntime.crash(e);
        }
    }
    public List<VCell> getNeighboringCell(int userId, String pkg) {
        try {
            return getService().getNeighboringCell(userId, pkg);
        } catch (RemoteException e) {
            VLog.printException(e);
            return VirtualRuntime.crash(e);
        }
    }
    public void setGlobalCell(VCell cell) {
        try {
            getService().setGlobalCell(cell);
        } catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }
    public void setGlobalAllCell(List<VCell> cell) {
        try {
            getService().setGlobalAllCell(cell);
        } catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }
    public void setGlobalNeighboringCell(List<VCell> cell) {
        try {
            getService().setGlobalNeighboringCell(cell);
        } catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }
    public void setLocation(int userId, String pkg, VLocation loc) {
        try {
            getService().setLocation(userId, pkg, loc);
        } catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }
    public VLocation getLocation(int userId, String pkg) {
        try {
            VLocation vLocation = getService().getLocation(userId, pkg);
            if (vLocation == null){
                VLog.e("getLocation vLocation is null "+pkg);
                return null;
            }
            VLog.e("getLocation "+vLocation.toString());
            return vLocation;
        }catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        } catch (Exception e){
            VLog.printException(e);
            return null;
        }
    }
    public VLocation getLocation() {
        return getLocation(MethodProxy.getAppUserId(), MethodProxy.getAppPkg());
    }
    public void setGlobalLocation(VLocation loc) {
        try {
            getService().setGlobalLocation(loc);
        } catch (RemoteException e) {
            VirtualRuntime.crash(e);
        }
    }
    public VLocation getGlobalLocation() {
        try {
            return getService().getGlobalLocation();
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }
}
