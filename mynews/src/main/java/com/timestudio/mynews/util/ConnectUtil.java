package com.timestudio.mynews.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ShenGqiang on 2016/10/24.
 */

public class ConnectUtil {

    //静态常量 连接地址和版本号
    public static final String APPCONET = "http://118.244.212.82:9092/newsClient/";
    public static final String APP_VER = "ver=1";


    /**
     * @description 检测用户名是否符合规格
     * @param name 用户名
     * @return 返回结果，true为符合，反之。
     */
    public boolean inspectUserName(String name) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * @desciprion 检测用户密码是否符合规格
     * @param password 用户密码
     * @return 返回结果，true为符合，反之。
     */
    public boolean inspectPassWord(String password) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]{6,15}");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * @description 检测用户填写的邮箱是否符合规范
     * @param emails 用户填写的邮箱地址
     * @return 返回结果，true为符合，反之。
     */
    public boolean inspectEmails(String emails) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]{2,}@(163,qq,126,sina)/.(com)");
        Matcher matcher = pattern.matcher(emails);
        return matcher.matches();
    }

    /**
     * @description 判断是否有网络
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        //判断有无网络
        if (info == null || !info.isAvailable()) {
            return false;
        }
        return true;
    }

    public static Bitmap makeBitmapCircle(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height/2;
        // 宽高相同
        if (width > height) {
            left = (width - height)/2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width)/2;
            right = width;
            bottom = top + width;
            roundPx = width/2;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        // 创建一个矩形
        RectF rectF = new RectF(rect);
        // 抗锯齿
        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
