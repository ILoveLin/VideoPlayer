package com.company.myapplication.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;


import com.shuyu.gsyvideoplayer.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;


/**
 * Created by shuyu on 2016/11/11.
 */

public class JumpUtils {

    /**
     * 跳到可控制
     *
     * @param activity
     */
//    public static void gotoControl(Activity activity) {
//        Intent intent = new Intent(activity, DetailControlActivity.class);
//        activity.startActivity(intent);
//    }
    public static void setViewHeight(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (null == layoutParams)
            return;
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void saveBitmap(Bitmap bitmap) throws FileNotFoundException {
        if (bitmap != null) {
            File file = new File(FileUtils.getPath(), "GSY-" + System.currentTimeMillis() + ".jpg");
            OutputStream outputStream;
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            bitmap.recycle();
        }
    }

}
