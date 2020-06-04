package com.lody.virtual.client.hook.delegate;
public interface PhoneInfoDelegate {
    String getDeviceId(String oldDeviceId, int userId);
    String getBluetoothAddress(String oldBluetoothAddress, int userId);
    String getMacAddress(String oldMacAddress, int userId);
}
