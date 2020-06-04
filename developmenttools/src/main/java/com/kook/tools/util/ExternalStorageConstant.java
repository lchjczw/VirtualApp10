package com.kook.tools.util;
import android.os.Environment;
import java.io.File;
public class ExternalStorageConstant {
    private static String AppName = "KookApp";
    public static String AppStorageDir = Environment.getExternalStorageDirectory().getPath() + File.separator + "KookApp";
    public static String PRODUCT_IMAGE_COMPARE_DIR = AppStorageDir + File.separator + "productImage";
    public static String PRODUCT_IMAGE_NAME = PRODUCT_IMAGE_COMPARE_DIR + File.separator + "product_image";
    public static String APP_DOWNLOAD = AppStorageDir + File.separator +"AppDownload";
    public static String PRODUCT_MAIN_PIC_DIR = AppStorageDir + File.separator +"product_mani_pic"+File.separator;
    public static String PRODUCT_BROWSER_SCREEN_CAP = AppStorageDir + File.separator +"screencap"+File.separator;
}
