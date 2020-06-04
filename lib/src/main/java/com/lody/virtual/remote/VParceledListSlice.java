package com.lody.virtual.remote;
import java.util.ArrayList;
import java.util.List;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
public class VParceledListSlice<T extends Parcelable> implements Parcelable {
	@SuppressWarnings("unchecked")
	public static final Parcelable.ClassLoaderCreator<VParceledListSlice> CREATOR = new Parcelable.ClassLoaderCreator<VParceledListSlice>() {
		public VParceledListSlice createFromParcel(Parcel in) {
			return new VParceledListSlice(in, null);
		}
		@Override
		public VParceledListSlice createFromParcel(Parcel in, ClassLoader loader) {
			return new VParceledListSlice(in, loader);
		}
		public VParceledListSlice[] newArray(int size) {
			return new VParceledListSlice[size];
		}
	};
	private static final int MAX_IPC_SIZE = 256 * 1024;
	private static final int MAX_FIRST_IPC_SIZE = MAX_IPC_SIZE / 2;
	private static String TAG = "ParceledListSlice";
	private static boolean DEBUG = false;
	private final List<T> mList;
	public VParceledListSlice(List<T> list) {
		mList = list;
	}
	private VParceledListSlice(Parcel p, ClassLoader loader) {
		final int N = p.readInt();
		mList = new ArrayList<T>(N);
		if (DEBUG)
			Log.d(TAG, "Retrieving " + N + " items");
		if (N <= 0) {
			return;
		}
		Class<?> listElementClass = null;
		int i = 0;
		while (i < N) {
			if (p.readInt() == 0) {
				break;
			}
			final T parcelable = p.readParcelable(loader);
			if (listElementClass == null) {
				listElementClass = parcelable.getClass();
			} else {
				verifySameType(listElementClass, parcelable.getClass());
			}
			mList.add(parcelable);
			if (DEBUG)
				Log.d(TAG, "Read inline #" + i + ": " + mList.get(mList.size() - 1));
			i++;
		}
		if (i >= N) {
			return;
		}
		final IBinder retriever = p.readStrongBinder();
		while (i < N) {
			if (DEBUG)
				Log.d(TAG, "Reading more @" + i + " of " + N + ": retriever=" + retriever);
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			data.writeInt(i);
			try {
				retriever.transact(IBinder.FIRST_CALL_TRANSACTION, data, reply, 0);
			} catch (RemoteException e) {
				Log.w(TAG, "Failure retrieving array; only received " + i + " of " + N, e);
				return;
			}
			while (i < N && reply.readInt() != 0) {
				final T parcelable = reply.readParcelable(loader);
				verifySameType(listElementClass, parcelable.getClass());
				mList.add(parcelable);
				if (DEBUG)
					Log.d(TAG, "Read extra #" + i + ": " + mList.get(mList.size() - 1));
				i++;
			}
			reply.recycle();
			data.recycle();
		}
	}
	private static void verifySameType(final Class<?> expected, final Class<?> actual) {
		if (!actual.equals(expected)) {
			throw new IllegalArgumentException(
					"Can't unparcel type " + actual.getName() + " in list of type " + expected.getName());
		}
	}
	public List<T> getList() {
		return mList;
	}
	@Override
	public int describeContents() {
		int contents = 0;
		for (int i = 0; i < mList.size(); i++) {
			contents |= mList.get(i).describeContents();
		}
		return contents;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		final int N = mList.size();
		final int callFlags = flags;
		dest.writeInt(N);
		if (DEBUG)
			Log.d(TAG, "Writing " + N + " items");
		if (N > 0) {
			final Class<?> listElementClass = mList.get(0).getClass();
			int i = 0;
			while (i < N && dest.dataSize() < MAX_FIRST_IPC_SIZE) {
				dest.writeInt(1);
				final T parcelable = mList.get(i);
				verifySameType(listElementClass, parcelable.getClass());
				dest.writeParcelable(parcelable, callFlags);
				if (DEBUG)
					Log.d(TAG, "Wrote inline #" + i + ": " + mList.get(i));
				i++;
			}
			if (i < N) {
				dest.writeInt(0);
				Binder retriever = new Binder() {
					@Override
					protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
							throws RemoteException {
						if (code != FIRST_CALL_TRANSACTION) {
							return super.onTransact(code, data, reply, flags);
						}
						int i = data.readInt();
						if (DEBUG)
							Log.d(TAG, "Writing more @" + i + " of " + N);
						while (i < N && reply.dataSize() < MAX_IPC_SIZE) {
							reply.writeInt(1);
							final T parcelable = mList.get(i);
							verifySameType(listElementClass, parcelable.getClass());
							reply.writeParcelable(parcelable, callFlags);
							if (DEBUG)
								Log.d(TAG, "Wrote extra #" + i + ": " + mList.get(i));
							i++;
						}
						if (i < N) {
							if (DEBUG)
								Log.d(TAG, "Breaking @" + i + " of " + N);
							reply.writeInt(0);
						}
						return true;
					}
				};
				if (DEBUG)
					Log.d(TAG, "Breaking @" + i + " of " + N + ": retriever=" + retriever);
				dest.writeStrongBinder(retriever);
			}
		}
	}
}