package com.lody.virtual.helper.compat;
import android.os.IBinder;
import android.os.IInterface;
import mirror.android.app.ApplicationThreadNative;
import mirror.android.app.IApplicationThread;
import mirror.android.app.IApplicationThreadOreo;
public class ApplicationThreadCompat {
    public static IInterface asInterface(IBinder binder) {
        if (BuildCompat.isOreo()) {
            return IApplicationThread.Stub.asInterface.call(binder);
        }
        return ApplicationThreadNative.asInterface.call(binder);
    }
}
