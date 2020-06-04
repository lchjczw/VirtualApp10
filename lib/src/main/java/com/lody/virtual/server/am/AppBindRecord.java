package com.lody.virtual.server.am;
import java.util.HashSet;
final class AppBindRecord {
    final ServiceRecord service;    
    final ServiceRecord.IntentBindRecord intent;  
    final ProcessRecord client;     
    final HashSet<ConnectionRecord> connections = new HashSet<ConnectionRecord>();
    AppBindRecord(ServiceRecord _service, ServiceRecord.IntentBindRecord _intent,
            ProcessRecord _client) {
        service = _service;
        intent = _intent;
        client = _client;
    }
}
