package com.kook.tools.util;
import android.os.Build;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class DebugKook {
    public static Boolean LOG_SWITCH = true; 
    private static Boolean LOG_WRITE_TO_FILE = false;
    private static char LOG_TYPE = 'v';
    public static String LOG_PATH_SDCARD_DIR = "/sdcard/Theme";
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 0;
    private static String LOG_FILE_NAME = "";
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");
    public static String TAG = "VirtualApp";
    public static boolean DEBUG = true;
    public static void w(Object msg) { 
        log(TAG, msg.toString(), 'w');
    }
    public static void w(String tag, Object msg) { 
        log(tag, msg.toString(), 'w');
    }
    public static void e(Object msg) { 
        log(TAG, msg.toString(), 'e');
    }
    public static void e(String tag, Object msg) { 
        log(tag, msg.toString(), 'e');
    }
    public static void d(Object msg) {
        log(TAG, msg.toString(), 'd');
    }
    public static void d(String tag, Object msg) {
        log(tag, msg.toString(), 'd');
    }
    public static void i(Object msg) {
        log(TAG, msg.toString(), 'i');
    }
    public static void i(String tag, Object msg) {
        log(tag, msg.toString(), 'i');
    }
    public static void v(Object msg) {
        log(TAG, msg.toString(), 'v');
    }
    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }
    public static void w(String text) {
        log(TAG, text, 'w');
    }
    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }
    public static void e(String text) {
        log(TAG, text, 'e');
    }
    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }
    public static void d(String text) {
        log(TAG, text, 'd');
    }
    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }
    public static void i(String text) {
        log(TAG, text, 'i');
    }
    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }
    public static void v(String text) {
        log(TAG, text, 'v');
    }
    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }
    private static void log(String subtag, String msg, char level) {
        if (LOG_SWITCH) {
            if (TAG.equals(subtag)){
            }
            if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) { 
                Log.e(TAG," < "+subtag+" > "+ msg);
            } else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.w(TAG," < "+subtag+" > "+ msg);
            } else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.d(TAG," < "+subtag+" > "+ msg);
            } else if ('i' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.i(TAG," < "+subtag+" > "+ msg);
            } else {
                Log.v(TAG," < "+subtag+" > "+ msg);
            }
            if (LOG_WRITE_TO_FILE){
                writeLogtoFile(String.valueOf(level), subtag, msg);
            }
        }
    }
    private static void writeLogtoFile(String mylogtype, String tag, String text) {
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype + "    " + tag + "    " + text;
        File fileDir = new File(LOG_PATH_SDCARD_DIR);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        File file = new File(LOG_PATH_SDCARD_DIR, needWriteFiel + LOG_FILE_NAME);
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"log 写入异常:"+e.toString());
        }
    }
    public static void delFile() {
        String needDelFiel = logfile.format(getDateBefore());
        File file = new File(LOG_PATH_SDCARD_DIR, needDelFiel + LOG_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }
    public static void printException(String subtag,Exception e){
        StackTraceElement[] stackTrace = e.getStackTrace();
        Throwable cause = e.getCause();
        DebugKook.d("Build.VERSION.SDK_INT = "+ Build.VERSION.SDK_INT);
        if (cause != null) {
            String stackTraceString = Log.getStackTraceString(cause);
            DebugKook.e(subtag,"异常 cause:" + stackTraceString);
        }else {
            DebugKook.e(subtag, "异常:" + e.toString() + "     cause is null ?" + (cause == null));
            for (int i = 0; i < stackTrace.length; i++) {
                DebugKook.e(subtag, "Exception e:" + stackTrace[i].toString());
            }
        }
    }
    public static void printException(Exception e){
        StackTraceElement[] stackTrace = e.getStackTrace();
        Throwable cause = e.getCause();
        DebugKook.d("Build.VERSION.SDK_INT = "+ Build.VERSION.SDK_INT);
        if (cause != null) {
            String stackTraceString = Log.getStackTraceString(cause);
            DebugKook.e(TAG,"异常 cause:" + stackTraceString);
        }else {
            DebugKook.e(TAG, "异常:" + e.toString() + "     cause is null ?" + (cause == null));
            for (int i = 0; i < stackTrace.length; i++) {
                DebugKook.e(TAG, "Exception e:" + stackTrace[i].toString());
            }
        }
    }
    public static void printThrowable(Throwable throwable){
        String stackTraceString = Log.getStackTraceString(throwable);
        DebugKook.d("Build.VERSION.SDK_INT = "+ Build.VERSION.SDK_INT);
        DebugKook.e(TAG,"printThrowable e:" + stackTraceString);
    }
    public static void printThrowable(String subtag,Throwable throwable){
        String stackTraceString = Log.getStackTraceString(throwable);
        DebugKook.d("Build.VERSION.SDK_INT = "+ Build.VERSION.SDK_INT);
        DebugKook.e(subtag,"printThrowable e:" + stackTraceString);
    }
}