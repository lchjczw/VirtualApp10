package com.lody.virtual.helper.utils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
public class AtomicFile {
    private final File mBaseName;
    private final File mBackupName;
    public AtomicFile(File baseName) {
        mBaseName = baseName;
        mBackupName = new File(baseName.getPath() + ".bak");
    }
    public File getBaseFile() {
        return mBaseName;
    }
    public void delete() {
        mBaseName.delete();
        mBackupName.delete();
    }
    public FileOutputStream startWrite() throws IOException {
        if (mBaseName.exists()) {
            if (!mBackupName.exists()) {
                if (!mBaseName.renameTo(mBackupName)) {
                    Log.w("AtomicFile", "Couldn't rename file " + mBaseName
                            + " to backup file " + mBackupName);
                }
            } else {
                mBaseName.delete();
            }
        }
        FileOutputStream str = null;
        try {
            str = new FileOutputStream(mBaseName);
        } catch (FileNotFoundException e) {
            File parent = mBaseName.getParentFile();
            if (!parent.mkdir()) {
                throw new IOException("Couldn't create directory " + mBaseName);
            }
            try {
                str = new FileOutputStream(mBaseName);
            } catch (FileNotFoundException e2) {
                throw new IOException("Couldn't create " + mBaseName);
            }
        }
        return str;
    }
    public void finishWrite(FileOutputStream str) {
        if (str != null) {
            sync(str);
            try {
                str.close();
                mBackupName.delete();
            } catch (IOException e) {
                Log.w("AtomicFile", "finishWrite: Got exception:", e);
            }
        }
    }
    public void failWrite(FileOutputStream str) {
        if (str != null) {
            sync(str);
            try {
                str.close();
                mBaseName.delete();
                mBackupName.renameTo(mBaseName);
            } catch (IOException e) {
                Log.w("AtomicFile", "failWrite: Got exception:", e);
            }
        }
    }
    public FileInputStream openRead() throws FileNotFoundException {
        if (mBackupName.exists()) {
            mBaseName.delete();
            mBackupName.renameTo(mBaseName);
        }
        return new FileInputStream(mBaseName);
    }
    public byte[] readFully() throws IOException {
        FileInputStream stream = openRead();
        try {
            int pos = 0;
            int avail = stream.available();
            byte[] data = new byte[avail];
            while (true) {
                int amt = stream.read(data, pos, data.length - pos);
                if (amt <= 0) {
                    return data;
                }
                pos += amt;
                avail = stream.available();
                if (avail > data.length - pos) {
                    byte[] newData = new byte[pos + avail];
                    System.arraycopy(data, 0, newData, 0, pos);
                    data = newData;
                }
            }
        } finally {
            stream.close();
        }
    }
    public void truncate() throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(mBaseName);
            fos.getFD().sync();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new IOException("Couldn't append " + mBaseName);
        } catch (IOException e) {
        }
    }
    @Deprecated public FileOutputStream openAppend() throws IOException {
        try {
            return new FileOutputStream(mBaseName, true);
        } catch (FileNotFoundException e) {
            throw new IOException("Couldn't append " + mBaseName);
        }
    }
    static boolean sync(FileOutputStream stream) {
        try {
            if (stream != null) {
                stream.getFD().sync();
            }
            return true;
        } catch (IOException e) {
        }
        return false;
    }
}