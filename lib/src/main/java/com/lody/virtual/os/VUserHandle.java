package com.lody.virtual.os;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.util.SparseArray;
import com.lody.virtual.client.VClientImpl;
import java.io.PrintWriter;
public final class VUserHandle implements Parcelable {
    public static final int PER_USER_RANGE = 100000;
    public static final int USER_ALL = -1;
    public static final VUserHandle ALL = new VUserHandle(USER_ALL);
    public static final int USER_CURRENT = -2;
    public static final VUserHandle CURRENT = new VUserHandle(USER_CURRENT);
    public static final int USER_CURRENT_OR_SELF = -3;
    public static final VUserHandle CURRENT_OR_SELF = new VUserHandle(USER_CURRENT_OR_SELF);
    public static final int USER_NULL = -10000;
    public static final int USER_OWNER = 0;
    public static final VUserHandle OWNER = new VUserHandle(USER_OWNER);
    public static final boolean MU_ENABLED = true;
    public static final int FIRST_SHARED_APPLICATION_GID = 50000;
    public static final int LAST_SHARED_APPLICATION_GID = 59999;
    public static final int FIRST_ISOLATED_UID = 99000;
    public static final int LAST_ISOLATED_UID = 99999;
    public static final Parcelable.Creator<VUserHandle> CREATOR
            = new Parcelable.Creator<VUserHandle>() {
        public VUserHandle createFromParcel(Parcel in) {
            return new VUserHandle(in);
        }
        public VUserHandle[] newArray(int size) {
            return new VUserHandle[size];
        }
    };
    private static final SparseArray<VUserHandle> userHandles = new SparseArray<VUserHandle>();
    final int mHandle;
    public VUserHandle(int h) {
        mHandle = h;
    }
    public VUserHandle(Parcel in) {
        mHandle = in.readInt();
    }
    public static boolean isSameUser(int uid1, int uid2) {
        return getUserId(uid1) == getUserId(uid2);
    }
    public static boolean accept(int userId) {
        if (userId == USER_ALL || userId == myUserId()) {
            return true;
        }
        return false;
    }
    public static final boolean isSameApp(int uid1, int uid2) {
        return getAppId(uid1) == getAppId(uid2);
    }
    public static final boolean isIsolated(int uid) {
        if (uid > 0) {
            final int appId = getAppId(uid);
            return appId >= FIRST_ISOLATED_UID && appId <= LAST_ISOLATED_UID;
        } else {
            return false;
        }
    }
    public static boolean isApp(int uid) {
        if (uid > 0) {
            final int appId = getAppId(uid);
            return appId >= Process.FIRST_APPLICATION_UID && appId <= Process.LAST_APPLICATION_UID;
        } else {
            return false;
        }
    }
    public static int getUserId(int uid) {
        if (MU_ENABLED) {
            return uid / PER_USER_RANGE;
        } else {
            return 0;
        }
    }
    public static int getCallingUserId() {
        return getUserId(VBinder.getCallingUid());
    }
    public static VUserHandle getCallingUserHandle() {
        int userId = getUserId(VBinder.getCallingUid());
        VUserHandle userHandle = userHandles.get(userId);
        if (userHandle == null) {
            userHandle = new VUserHandle(userId);
            userHandles.put(userId, userHandle);
        }
        return userHandle;
    }
    public static int getUid(int userId, int appId) {
        if (MU_ENABLED) {
            return userId * PER_USER_RANGE + (appId % PER_USER_RANGE);
        } else {
            return appId;
        }
    }
    public static int getAppId(int uid) {
        return uid % PER_USER_RANGE;
    }
    public static int myAppId() {
        return getAppId(VClientImpl.get().getVUid());
    }
    public static int getAppIdFromSharedAppGid(int gid) {
        final int noUserGid = getAppId(gid);
        if (noUserGid < FIRST_SHARED_APPLICATION_GID ||
                noUserGid > LAST_SHARED_APPLICATION_GID) {
            throw new IllegalArgumentException(Integer.toString(gid) + " is not a shared app gid");
        }
        return (noUserGid + Process.FIRST_APPLICATION_UID) - FIRST_SHARED_APPLICATION_GID;
    }
    public static void formatUid(StringBuilder sb, int uid) {
        if (uid < Process.FIRST_APPLICATION_UID) {
            sb.append(uid);
        } else {
            sb.append('u');
            sb.append(getUserId(uid));
            final int appId = getAppId(uid);
            if (appId >= FIRST_ISOLATED_UID && appId <= LAST_ISOLATED_UID) {
                sb.append('i');
                sb.append(appId - FIRST_ISOLATED_UID);
            } else if (appId >= Process.FIRST_APPLICATION_UID) {
                sb.append('a');
                sb.append(appId - Process.FIRST_APPLICATION_UID);
            } else {
                sb.append('s');
                sb.append(appId);
            }
        }
    }
    public static String formatUid(int uid) {
        StringBuilder sb = new StringBuilder();
        formatUid(sb, uid);
        return sb.toString();
    }
    public static void formatUid(PrintWriter pw, int uid) {
        if (uid < Process.FIRST_APPLICATION_UID) {
            pw.print(uid);
        } else {
            pw.print('u');
            pw.print(getUserId(uid));
            final int appId = getAppId(uid);
            if (appId >= FIRST_ISOLATED_UID && appId <= LAST_ISOLATED_UID) {
                pw.print('i');
                pw.print(appId - FIRST_ISOLATED_UID);
            } else if (appId >= Process.FIRST_APPLICATION_UID) {
                pw.print('a');
                pw.print(appId - Process.FIRST_APPLICATION_UID);
            } else {
                pw.print('s');
                pw.print(appId);
            }
        }
    }
    public static int myUserId() {
        return getUserId(VClientImpl.get().getVUid());
    }
    public static void writeToParcel(VUserHandle h, Parcel out) {
        if (h != null) {
            h.writeToParcel(out, 0);
        } else {
            out.writeInt(USER_NULL);
        }
    }
    public static VUserHandle readFromParcel(Parcel in) {
        int h = in.readInt();
        return h != USER_NULL ? new VUserHandle(h) : null;
    }
    public static VUserHandle myUserHandle() {
        return new VUserHandle(myUserId());
    }
    public final boolean isOwner() {
        return this.equals(OWNER);
    }
    public int getIdentifier() {
        return mHandle;
    }
    @Override
    public String toString() {
        return "VUserHandle{" + mHandle + "}";
    }
    @Override
    public boolean equals(Object obj) {
        try {
            if (obj != null) {
                VUserHandle other = (VUserHandle)obj;
                return mHandle == other.mHandle;
            }
        } catch (ClassCastException e) {
        }
        return false;
    }
    @Override
    public int hashCode() {
        return mHandle;
    }
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mHandle);
    }
}
