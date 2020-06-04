package com.lody.virtual.client.hook.proxies.wifi_scanner;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
public class WifiScannerStub extends BinderInvocationProxy {
    public WifiScannerStub() {
        super(new GhostWifiScannerImpl(), "wifiscanner");
    }
}
