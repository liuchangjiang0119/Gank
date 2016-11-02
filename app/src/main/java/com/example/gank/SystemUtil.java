package com.example.gank;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dell on 2016/10/18.
 */

public class SystemUtil {
    public static void saveImage(Context mContext,String url, Bitmap bitmap){
        String filename = url.substring(url.lastIndexOf("/"),url.lastIndexOf("."))+".png";
        File dir = new File(mContext.getExternalCacheDir().getAbsolutePath()+"/data");
        File imageFile = new File(dir,filename);
        if (!dir.exists()){
            dir.mkdir();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            if(isSuccess){
                Toast.makeText(mContext,"下载成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(mContext,"下载失败",Toast.LENGTH_LONG).show();
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copy(String url,Context context){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("url",url);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static boolean isNetConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null){
            return false;

        }
        return networkInfo.isAvailable();

    }
}
