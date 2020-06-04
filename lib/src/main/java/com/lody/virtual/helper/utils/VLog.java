package com.lody.virtual.helper.utils;
import android.os.Bundle;
import android.util.Log;
import java.util.Set;
import com.kook.tools.util.DebugKook;
import com.lody.virtual.client.core.VirtualCore;
public class VLog {
	public static boolean OPEN_LOG = true;
	public static void i(String msg, Object... format) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.i(processName,String.format(msg, format));
		}
	}
	public static void i(String tag, String msg, Object... format) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.i(processName+":"+tag,String.format(msg, format));
		}
	}
	public static void d(String msg, Object... format) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.d(processName,String.format(msg, format));
		}
	}
	public static void d(String tag, String msg, Object... format) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.d(processName+":"+tag,String.format(msg, format));
		}
	}
	public static void w(String msg, Object... format) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.w(processName,String.format(msg, format));
		}
	}
	public static void w(String tag, String msg, Object... format) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.w(processName+":"+tag,String.format(msg, format));
		}
	}
	public static void e(String msg, Object... format) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.e(processName,String.format(msg, format));
		}
	}
	public static void e(String tag, String msg, Object... format) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.e(processName+":"+tag,String.format(msg, format));
		}
	}
	public static void v(String msg, Object... format) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.v(processName,String.format(msg, format));
		}
	}
	public static void v(String tag, String msg, Object... format) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.v(processName+":"+tag,String.format(msg, format));
		}
	}
	public static void printException(Exception e) {
		if (OPEN_LOG) {
			String processName = VirtualCore.get().getProcessName();
			DebugKook.printException(processName,e);
		}
	}
	public static void printThrowable(Throwable throwable){
        String stackTraceString = Log.getStackTraceString(throwable);
		String processName = VirtualCore.get().getProcessName();
		android.util.Log.e("kook","printThrowable e:" + stackTraceString);
        //DebugKook.e(processName,"printThrowable e:" + stackTraceString);
    }
	public static String toString(Bundle bundle){
		if(bundle==null)return null;
		if(Reflect.on(bundle).get("mParcelledData")!=null){
			Set<String> keys=bundle.keySet();
			StringBuilder stringBuilder=new StringBuilder("Bundle[");
			if(keys!=null) {
				for (String key : keys) {
					stringBuilder.append(key);
					stringBuilder.append("=");
					stringBuilder.append(bundle.get(key));
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("]");
			return stringBuilder.toString();
		}
		return bundle.toString();
	}
	public static String getStackTraceString(Throwable tr) {
		return Log.getStackTraceString(tr);
	}
	public static void printStackTrace(String tag) {
		Log.e(tag, getStackTraceString(new Exception()));
		DebugKook.e(getStackTraceString(new Exception()));
	}
	public static void e(String tag, Throwable e) {
		Log.e(tag, getStackTraceString(e));
		DebugKook.e(getStackTraceString(e));
	}
}
