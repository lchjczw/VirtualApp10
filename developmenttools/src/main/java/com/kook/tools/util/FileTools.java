package com.kook.tools.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import com.kook.tools.util.DebugKook;

/**
 * Created by kook on 19-6-27.
 */

public class FileTools {

    /** 删除文件，可以是文件或文件夹
     * @param delFile 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile())
                return deleteSingleFile(delFile);
            else
                return deleteDirectory(delFile);
        }
    }

    /** 删除单个文件
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    private static boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /*将字符串保存到文件中*/
    public static boolean saveAsFileWriter(String content) {
        String fileName = "/response.text";
        String filePath = Environment.getExternalStoragePublicDirectory("") + fileName;
        File file = new File(filePath);
        file.deleteOnExit();

        FileWriter fwriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(filePath, false);
            fwriter.write(content);
            DebugKook.e("文件已经保存"+filePath);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            DebugKook.e("IOException:" + ex.toString());
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }


    //保存一张图片到
    public static String saveBitmap(Bitmap bm,String imageFile){
        imageFile = ExternalStorageConstant.PRODUCT_IMAGE_COMPARE_DIR+File.separator+ imageFile;
        String filename = imageFile.substring(imageFile.lastIndexOf(File.separator) + 1);
        String filepath = imageFile.substring(0,imageFile.lastIndexOf(File.separator));

        final File path = new File(filepath);
        if (!path.exists()){
            path.mkdirs();
        }
        final File file = new File(filepath, filename);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            //DebugKook.e( "已经保存 "+file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            DebugKook.printException(e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            DebugKook.printException(e);
            return null;
        } catch (Exception e){
            DebugKook.printException(e);
            return null;
        }finally {
        }
        return file.getAbsolutePath();
    }


    /**
     * 保存方法
     */
    public static void saveBitmap(final Context context, Bitmap bm,boolean updateAlum) {
        //DebugKook.e("保存图片");
        String fileName = "save.png";
        final File file = new File(Environment.getExternalStoragePublicDirectory(""), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            DebugKook.e( "已经保存 "+file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            DebugKook.e("保存 FileNotFoundException " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            DebugKook.e("保存 IOException " + e.toString());
        }

        if (updateAlum) {
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

                String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA};
                Cursor cursor = context.getContentResolver().query(
                        MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,//指定缩略图数据库的Uri
                        projection,//指定所要查询的字段
                        MediaStore.Images.Thumbnails._ID + " = ?",//查询条件
                        new String[]{"123"}, //查询条件中问号对应的值
                        null);
                while (cursor.moveToNext()) {
                    DebugKook.e("  查看地址:" + cursor.getString(1));
                }

                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                DebugKook.e("二维码文件不存在" + file.getAbsolutePath() + "    e:" + e.toString());
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //删除文件后更新数据库  通知媒体库更新文件夹,！！！！！filepath（文件夹路径）要求尽量精确，以防删错
                    String where = MediaStore.Audio.Media.DATA + " like \"" + file.getAbsolutePath() + "%" + "\"";
                    int i = context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null);
                    if (i > 0) {
                        DebugKook.e("媒体库更新成功！");
                    }
                }
            }, 15000);   //5秒
        }
    }



    public static String readFile(Context context, String fileName) {
        String content = "";
        try {
            InputStream instream = new FileInputStream(fileName);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream, "UTF-8");
                BufferedReader buffreader = new BufferedReader(inputreader);
                // 一次读取一个字符数组
                char[] chars = new char[1024];
                int len = 0;
                while ((len = inputreader.read(chars)) != -1) {
                    content += new String(chars, 0, len);
                }
                instream.close();        //关闭输入流
            }
        } catch (FileNotFoundException e) {
            DebugKook.d("The File doesn't not exist.");
            DebugKook.printException(e);
        } catch (IOException e) {
            DebugKook.d(e.getMessage());
        }
        return content;
    }




    public static int copyDir(String fromFile, String toFile) {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!root.exists()) {
            return -1;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();

        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                copyDir(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            } else//如果当前项为文件则进行文件拷贝
            {
                copyFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return 0;
    }

    /**
     * 根据路径获取文件名
     * @param path 路径参数
     * @param suffix 是否需要后缀
     * @return 文件名字符串
     */
    public static String getFileNameByPath(String path,boolean suffix) {
        // 判空操作必须要有 , 处理方式不唯一 , 根据实际情况可选其一 。

        if(path == null) {
            DebugKook.e("path is null");
            return "";
        }

        int start=path.lastIndexOf("/");
        int end= suffix?path.length():path.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return path.substring(start+1,end);//包含头不包含尾 , 故:头 + 1
        }else{
            return "";
            //return null; 返回 null 还是 "" 根据情况抉择吧
        }
    }

    public static String getFileDirByPath(String path){
        // 判空操作必须要有 , 处理方式不唯一 , 根据实际情况可选其一 。

        if(path == null) {
            DebugKook.e("path is null");
            return "";
        }
        int start=0;
        int end= path.lastIndexOf("/");
        if(start!=-1 && end!=-1){
            return path.substring(start,end+1);//包含头不包含尾 , 故:头 + 1
        }else{
            return "";
            //return null; 返回 null 还是 "" 根据情况抉择吧
        }
    }




    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            DebugKook.e("复制整个文件夹内容操作出错");
            DebugKook.printException(e);
        }

    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {

            DebugKook.e("复制单个文件操作出错");
            DebugKook.printException(e);

        }
    }

    /**
     * @param path 文件绝对路径
     * @param type 需要获取数据的类型,需要类型在家
     *             1，文件名
     *             2，文件所在目录
     *             3、文件后缀
     *             4、当前文件的上一级目录名字
     *             <p>
     *             获取文件名及后缀
     */
    public static String getFileNameWithParams(String path, int type) {
        if (TextUtils.isEmpty(path) || type <= 0 || type > 4) {
            throw new RuntimeException(" 传入参数异常 ");
        }

        int start = path.lastIndexOf("/");
        if (start != -1) {
            if (type == 1) {
                return path.substring(start + 1);
            } else if (type == 2) {
                return path.substring(0, start + 1);
            } else if (type == 3) {
                int index = path.lastIndexOf(".");
                return path.substring(index + 1);
            } else if (type == 4) {

                String substring = path.substring(0, start);
                int indexOf = substring.lastIndexOf("/");
                //DebugKook.e("获取父目录名字"+ substring+"    ==="+substring.substring(indexOf+1));
                if (indexOf != -1) {
                    return substring.substring(indexOf + 1);
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } else {
            return "";
        }
    }


    /**将一个输入流 写入一个文件*/
    public static void inputstreamtofile(InputStream ins, File file) {
        try {
            if (file.exists()) {
                file.deleteOnExit();
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            DebugKook.printException(e);
        }
    }
}
