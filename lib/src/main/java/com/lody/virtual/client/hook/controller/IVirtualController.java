package com.lody.virtual.client.hook.controller;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
public interface IVirtualController {
    IVirtualController EMPTY = new IVirtualController() {
        @Override
        public void onCreateController(Application application) {
        }
        @Override
        public void controllerActivityCreate(Activity activity) {
        }
        @Override
        public void controllerActivityResume(Activity activity) {
        }
        @Override
        public void controllerActivityDestroy(Activity activity) {
        }
        @Override
        public void controllerActivityPause(Activity activity) {
        }
    };
    void onCreateController(Application application);
    void controllerActivityCreate(Activity activity);
    void controllerActivityResume(Activity activity);
    void controllerActivityDestroy(Activity activity);
    void controllerActivityPause(Activity activity);
}
