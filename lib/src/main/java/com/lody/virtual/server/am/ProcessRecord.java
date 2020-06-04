package com.lody.virtual.server.am;
import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.os.ConditionVariable;
import android.os.IInterface;
import com.lody.virtual.client.IVClient;
import com.lody.virtual.os.VUserHandle;
import java.util.HashSet;
import java.util.Set;
final class ProcessRecord extends Binder implements Comparable<ProcessRecord> {
	final ConditionVariable lock = new ConditionVariable();
	public final ApplicationInfo info; 
	final public String processName; 
	final Set<String> pkgList = new HashSet<>(); 
	public IVClient client;
	IInterface appThread;
	public int pid;
	public int vuid;
	public int vpid;
	public int userId;
	boolean doneExecuting;
    int priority;
	public ProcessRecord(ApplicationInfo info, String processName, int vuid, int vpid) {
		this.info = info;
		this.vuid = vuid;
		this.vpid = vpid;
		this.userId = VUserHandle.getUserId(vuid);
		this.processName = processName;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProcessRecord record = (ProcessRecord) o;
		return processName != null ? processName.equals(record.processName) : record.processName == null;
	}
    @Override
    public int compareTo(ProcessRecord another) {
        return this.priority - another.priority;
    }
	@Override
	public String toString() {
		return "ProcessRecord{" +
				"lock=" + lock +
				", info=" + info +
				", processName='" + processName + '\'' +
				", pkgList=" + pkgList +
				", client=" + client +
				", appThread=" + appThread +
				", pid=" + pid +
				", vuid=" + vuid +
				", vpid=" + vpid +
				", userId=" + userId +
				", doneExecuting=" + doneExecuting +
				", priority=" + priority +
				'}';
	}
}
