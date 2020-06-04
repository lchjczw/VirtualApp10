package com.lody.virtual.helper.compat;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.IActivityManager;
public class ActivityManagerCompat {
	public static final int SERVICE_DONE_EXECUTING_ANON = 0;
	public static final int SERVICE_DONE_EXECUTING_START = 1;
	public static final int SERVICE_DONE_EXECUTING_STOP = 2;
	public static final int START_INTENT_NOT_RESOLVED = -1;
	public static final int START_NOT_CURRENT_USER_ACTIVITY = -8;
	public static final int START_TASK_TO_FRONT = 2;
	public static final int INTENT_SENDER_BROADCAST = 1;
	public static final int INTENT_SENDER_ACTIVITY = 2;
	public static final int INTENT_SENDER_ACTIVITY_RESULT = 3;
	public static final int INTENT_SENDER_SERVICE = 4;
	public static final int USER_OP_SUCCESS = 0;
	public static boolean finishActivity(IBinder token, int code, Intent data) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return IActivityManager.finishActivity.call(
					ActivityManagerNative.getDefault.call(),
					token, code, data, 0);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return IActivityManager.finishActivity.call(
					ActivityManagerNative.getDefault.call(),
					token, code, data, false);
		} else {
			IActivityManager.finishActivity.call(
					ActivityManagerNative.getDefault.call(),
					token, code, data
			);
		}
		return false;
	}
}
