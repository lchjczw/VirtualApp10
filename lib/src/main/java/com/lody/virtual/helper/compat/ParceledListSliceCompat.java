package com.lody.virtual.helper.compat;
import android.os.Build;
import com.kook.tools.util.AndroidVersionConstant;
import java.lang.reflect.Method;
import java.util.List;
import mirror.android.content.pm.ParceledListSlice;
public class ParceledListSliceCompat {
	public static boolean isReturnParceledListSlice(Method method) {
		return method != null && method.getReturnType() == ParceledListSlice.TYPE;
	}
	public static  Object create(List list) {
		Object slice;
		if (Build.VERSION.SDK_INT > AndroidVersionConstant.JELLY_BEAN_MR2_4_3){
			slice = ParceledListSlice.ctor.newInstance(list);
		}else {
			slice = ParceledListSlice.ctor.newInstance();
			for (Object item : list) {
				if (ParceledListSlice.append != null) {
					ParceledListSlice.append.call(slice, item);
				}
			}
			if (ParceledListSlice.setLastSlice != null) {
				ParceledListSlice.setLastSlice.call(slice, true);
			}
		}
		return slice;
	}
}
