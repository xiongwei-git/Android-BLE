package cn.com.heaton.blelibrary.ble.request;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.com.heaton.blelibrary.ble.Ble;
import cn.com.heaton.blelibrary.ble.BleRequestImpl;
import cn.com.heaton.blelibrary.ble.callback.wrapper.BleWrapperCallback;
import cn.com.heaton.blelibrary.ble.model.BleDevice;
import cn.com.heaton.blelibrary.ble.annotation.Implement;
import cn.com.heaton.blelibrary.ble.callback.BleNotifyCallback;
import cn.com.heaton.blelibrary.ble.callback.wrapper.NotifyWrapperCallback;

/**
 * Created by LiuLei on 2017/10/23.
 */
@Implement(NotifyRequest.class)
public class NotifyRequest<T extends BleDevice> implements NotifyWrapperCallback<T> {

    private static final String TAG = "NotifyRequest";
    //    private BleNotifyCallback<T> notifyCallback;
    private BleWrapperCallback<T> bleWrapperCallback;
    private Map<String, BleNotifyCallback<T>> notifyCallbackMap = new HashMap<>();

    protected NotifyRequest() {
        bleWrapperCallback = Ble.options().bleWrapperCallback;
    }

    public void notify(T device, boolean enable, BleNotifyCallback<T> callback) {
//        notifyCallback = callback;
        String address = ((BleDevice) device).getBleAddress();
        notifyCallbackMap.put(address, callback);
        BleRequestImpl bleRequest = BleRequestImpl.getBleRequest();
        bleRequest.setCharacteristicNotification(device.getBleAddress(), enable);
    }

    public void notifyByUuid(T device, boolean enable, UUID serviceUUID, UUID characteristicUUID, BleNotifyCallback<T> callback) {
//        notifyCallback = callback;
        String address = ((BleDevice) device).getBleAddress();
        notifyCallbackMap.put(address, callback);
        BleRequestImpl bleRequest = BleRequestImpl.getBleRequest();
        bleRequest.setCharacteristicNotificationByUuid(device.getBleAddress(), enable, serviceUUID, characteristicUUID);
    }

    @Deprecated
    public void cancelNotify(T device, BleNotifyCallback<T> callback) {
//        notifyCallback = callback;
        String address = ((BleDevice) device).getBleAddress();
        notifyCallbackMap.remove(address);
        BleRequestImpl bleRequest = BleRequestImpl.getBleRequest();
        bleRequest.setCharacteristicNotification(device.getBleAddress(), false);
    }

    @Override
    public void onChanged(final T device, final BluetoothGattCharacteristic characteristic) {
//        if (null != notifyCallback) {
//            notifyCallback.onChanged(device, characteristic);
//        }
        String address = ((BleDevice) device).getBleAddress();
        if (notifyCallbackMap.containsKey(address)) {
            notifyCallbackMap.get(address).onChanged(device, characteristic);
        }
        if (bleWrapperCallback != null) {
            bleWrapperCallback.onChanged(device, characteristic);
        }
    }

    @Override
    public void onNotifySuccess(final T device) {
//        if (null != notifyCallback) {
//            notifyCallback.onNotifySuccess(device);
//        }
        String address = ((BleDevice) device).getBleAddress();
        if (notifyCallbackMap.containsKey(address)) {
            notifyCallbackMap.get(address).onNotifySuccess(device);
        }

        if (bleWrapperCallback != null) {
            bleWrapperCallback.onNotifySuccess(device);
        }
    }

    @Override
    public void onNotifyCanceled(T device) {
//        if (null != notifyCallback) {
//            notifyCallback.onNotifyCanceled(device);
//        }
        String address = ((BleDevice) device).getBleAddress();
        if (notifyCallbackMap.containsKey(address)) {
            notifyCallbackMap.get(address).onNotifyCanceled(device);
        }
        if (bleWrapperCallback != null) {
            bleWrapperCallback.onNotifyCanceled(device);
        }
    }

    public BleNotifyCallback<T> getNotify(T device) {
        String address = ((BleDevice) device).getBleAddress();
        if (notifyCallbackMap.containsKey(address)) {
            return notifyCallbackMap.get(address);
        }
        return null;
    }
}
