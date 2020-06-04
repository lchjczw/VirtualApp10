package com.lody.virtual.client.hook.delegate;
import android.app.Application;
import android.content.Intent;
import android.app.Activity;
public interface ComponentDelegate {
    ComponentDelegate EMPTY = new ComponentDelegate() {
        @Override
        public void beforeActivityCreate(Activity activity) {
        }
        @Override
        public void beforeActivityResume(Activity activity) {
        }
        @Override
        public void beforeActivityPause(Activity activity) {
        }
        @Override
        public void beforeActivityDestroy(Activity activity) {
        }
        @Override
        public void afterActivityCreate(Activity activity) {
        }
        @Override
        public void afterActivityResume(Activity activity) {
        }
        @Override
        public void afterActivityPause(Activity activity) {
        }
        @Override
        public void afterActivityDestroy(Activity activity) {
        }
        @Override
        public void onSendBroadcast(Intent intent) {
        }
        @Override
        public void beforeApplicationCreate(Application application) {
        }
        @Override
        public void afterApplicationCreate(Application application) {
        }
    };
    void beforeApplicationCreate(Application application);
    void afterApplicationCreate(Application application);
    void beforeActivityCreate(Activity activity);
    void beforeActivityResume(Activity activity);
    void beforeActivityPause(Activity activity);
    void beforeActivityDestroy(Activity activity);
    void afterActivityCreate(Activity activity);
    void afterActivityResume(Activity activity);
    void afterActivityPause(Activity activity);
    void afterActivityDestroy(Activity activity);
    void onSendBroadcast(Intent intent);
}
