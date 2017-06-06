package com.yiyicai.tvtrendapp.android_hybriddemo.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.yiyicai.tvtrendapp.android_hybriddemo.core.network.OnDownloadListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import okhttp3.ResponseBody;

/**
 * Created by vane on 16/6/14.
 */

public class FileUtil {
    static final String TAG = "FileUtil";
    /**
     * zip文件更新写入
     * @param outputfile
     * @param body
     * @return
     */
    public static boolean writeFile(File outputfile, ResponseBody body, OnDownloadListener listener) {
        boolean success = false;
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(outputfile);
                int read;
                byte[] fileReader = new byte[1024];
                while (-1 != (read = inputStream.read(fileReader))) {
                    outputStream.write(fileReader, 0, read);
                    DownloadProgress downloadProgress = DownloadProgress.getInstance();
                    downloadProgress.setCurrent(downloadProgress.getCurrent() + read);//当前下载量+新增下载量
                    int progress = (int) (downloadProgress.getCurrent() * 1.0f / downloadProgress.getTotal() * 100);//当前下载量/总下载量
                    // 下载中
                    listener.onDownloading(progress);
                }
                outputStream.flush();
                success = true;
            } catch (IOException e) {
                success = false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            success = false;
        }
        return success;
    }

    /**
     * 用于写入版本记录
     * @param outputfile
     * @param body
     * @return
     */
    public static boolean writeFile(File outputfile, String body) {
        boolean success = false;
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = new ByteArrayInputStream(body.getBytes());
                outputStream = new FileOutputStream(outputfile);
                int read;
                byte[] fileReader = new byte[1024];
                while (-1 != (read = inputStream.read(fileReader))) {
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                success = true;
            } catch (IOException e) {
                success = false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            success = false;
        }
        return success;
    }

    /**
     * 读取本地的配置文件
     * @param file
     * @return
     */
    public static String readFile(File file) {
        InputStreamReader reader = null;
        StringWriter writer = new StringWriter();
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            //将输入流写入输出流
            char[] buffer = new char[1024];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        //返回转换结果
        if (writer != null)
            return writer.toString();
        else return null;
    }

    public static File rebuildFile(File parent, String name) {
        final File target = new File(parent, name);
        if (target.exists()) {
            target.delete();
        }
        try {
            target.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 无条件删除指定目录中的文件
     *
     * @param path 目录路径
     * @return 删除文件个数
     */
    synchronized public static int clearFolder(File path) {
        int deletedItems = 0;
        File[] fileList = path.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    deletedItems += clearFolder(file);
                }
                if (file.delete()) {
                    deletedItems++;
                }
            }
        }
        return deletedItems;
    }

    /**
     * 无条件删除指定文件或文件夹
     *
     * @param path 目录路径
     * @return 删除文件个数
     */
    synchronized public static int deleteFile(File path) {
        int deletedItems = 0;
        File[] fileList = path.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    deletedItems += deleteFile(file);
                }
                if (file.delete()) {
                    deletedItems++;
                }
            }
            path.delete();//删除自身
        }
        return deletedItems;
    }

    public static boolean safeClose(Closeable c) {
        boolean ret = false;
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 解压一个压缩文档 到指定位置
     *
     * @param zipFileString 压缩包的名字
     * @param outPathString 指定的路径
     * @throws Exception
     */
    public static void unZip(String zipFileString, String outPathString) {
        Log.i(TAG, "unZip: 开始解压缩，压缩文件所在目录：" + zipFileString+"  解压缩文件夹目录：" + outPathString);
        java.util.zip.ZipInputStream inZip = null;
        try {
            Log.i(TAG, "unZip: 拿到压缩文件输入流");
            inZip = new java.util.zip.ZipInputStream(new FileInputStream(zipFileString));//获取zip输入流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ZipEntry zipEntry;
        String szName = "";
        try {
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    Log.i(TAG, "unZip: 当前解压缩文件为文件夹");
                    //如果是文件夹则创建目录
                    // get the folder name of the widget
                    szName = szName.substring(0, szName.length() - 1);
                    File folder = new File(outPathString + File.separator + szName);
                    
                    folder.mkdirs();
                } else {
                    Log.i(TAG, "unZip: 当前解压缩文件并非文件夹");
                    int index = szName.lastIndexOf(File.separator);//截取除开文件名的目录
                    File file = null;
                    if (index != -1) {
                        Log.i(TAG, "unZip: 创建解压缩文件目录");
                        File temp = new File(outPathString + File.separator + szName.substring(0, index));
                        temp.mkdirs();//创建输出目录
                    }
                    file = new File(outPathString + File.separator + szName);
                    file.createNewFile();//创建输出文件
                    // get the output stream of the file
                    FileOutputStream out = new FileOutputStream(file);//创建输出流
                    int len;
                    byte[] buffer = new byte[1024];
                    // read (len) bytes into buffer
                    while ((len = inZip.read(buffer)) != -1) {
                        // write (len) byte from buffer at the position 0
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                    Log.i(TAG, "unZip: 当前文件：" + file.getName()+"解压缩完毕，解压路径："+file.getAbsolutePath());
                }
            }//end of while
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inZip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//end of func

    public static int unZip1(String zipFile, String folderPath) {
        //public static void upZipFile() throws Exception{
        ZipFile zfile = null;
        try {
            zfile = new ZipFile(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("vane", "IOException 1=" + e.getMessage());
        }
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                Log.d("upZipFile", "1 ze.getName() = " + ze.getName());
                String dirstr = folderPath + ze.getName();
                //dirstr.trim();
                try {
                    dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("vane", "UnsupportedEncodingException=" + e.getMessage());
                }
                Log.d("upZipFile", "str = " + dirstr);
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            Log.d("upZipFile", "2 ze.getName() = " + ze.getName());
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("vane", "FileNotFoundException=" + e.getMessage());
            }
            InputStream is = null;
            try {
                is = new BufferedInputStream(zfile.getInputStream(ze));
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("vane", "IOException 2=" + e.getMessage());

            }
            int readLen = 0;
            try {
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("vane", "IOException 3=" + e.getMessage());
            } finally {
                try {
                    is.close();
                    os.close();
                    zfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("vane", "IOException 4=" + e.getMessage());
                }
            }
        }
        Log.d("upZipFile", "finishssssssssssssssssssss");
        return 0;
    }

    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        String lastDir = baseDir;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                lastDir += (dirs[i] + "/");
                File dir = new File(lastDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                    Log.d("getRealFileName", "create dir = " + (lastDir + "/" + dirs[i]));
                }
            }
            File ret = new File(lastDir, dirs[dirs.length - 1]);
            Log.d("upZipFile", "2ret = " + ret);
            return ret;
        } else {

            return new File(baseDir, absFileName);

        }

    }

    public static File getRootDir(Context context) {
        File file;
        if (isExternalStorageReadable() && isExternalStorageWritable()) {
            file = context.getExternalFilesDir(null);
        } else {
            file = context.getFilesDir();
        }
        return file;
//        return context.getFilesDir();
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
