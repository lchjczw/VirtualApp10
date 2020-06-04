package com.lody.virtual.server.am;
import android.app.IServiceConnection;
final class ConnectionRecord {
    final AppBindRecord binding;    
    final IServiceConnection conn;  
    final int flags;                
    boolean serviceDead;            
    ConnectionRecord(AppBindRecord _binding,
               IServiceConnection _conn, int _flags) {
        binding = _binding;
        conn = _conn;
        flags = _flags;
    }
}
