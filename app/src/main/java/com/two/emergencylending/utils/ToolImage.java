package com.two.emergencylending.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.getlimtlibrary.builder.utils.MyLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by ZENG DONG YANG on 2016/10/27.
 * e-mail:zengdongyang@incamel.com
 */

public class ToolImage {
    /**
     * 拍照
     *
     * @param activity    调用时Activity
     * @param requestCode 请求码
     * @return 图片路径
     */
    public static String byCamera(Activity activity, Fragment fragment, int requestCode) {
        String sdStatus = Environment.getExternalStorageState();
        /* 检测sd是否可用 */
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(activity, "SD卡不可用！", Toast.LENGTH_SHORT).show();
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_hhmmss");

        String name = formatter.format(System.currentTimeMillis()) + ".png";
        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path + "/myImage/");
        /** 检测文件夹是否存在，不存在则创建文件夹 **/
        if (!file.exists() && !file.isDirectory())
            file.mkdirs();
        String fileName = file.getPath() + "/" + name;
        Uri uri = Uri.fromFile(new File(fileName));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else
            activity.startActivityForResult(intent, requestCode);
        return fileName;
    }

    public static String bitMapBase64(Bitmap.CompressFormat format, int quality, Bitmap bitMap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(format, quality, baos);
        byte[] byteArray = baos.toByteArray();
        String encodeToString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodeToString;

    }

    public static String fileBase64(String filePath) {
        String encodeToString = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffe = new byte[1024];
            while ((len = fis.read(buffe)) != -1) {
                baos.write(buffe, 0, len);
            }
            byte[] byteArray = baos.toByteArray();
            encodeToString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeToString;
    }

    /**
     * 保存到文件
     *
     * @param bitmap  图片
     * @param format  保存格式
     * @param quality 图片压缩比
     * @param file    保存目标文件
     */
    public static void compressBitmapToFile(Bitmap bitmap, Bitmap.CompressFormat format, int quality, File file) {
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(file);
            bitmap.compress(format, quality, b);// 把数据写入文件
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (b != null) {
                    b.flush();
                    b.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //图片按比例大小压缩方法（根据Bitmap图片压缩）
    public static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        android.util.Log.d("image", "image length--->" + baos.toByteArray().length / 1024);
        while (baos.toByteArray().length / 1024 > 1024 && options > 5) {
            baos.reset();
            options -= 5;
            MyLog.i("keey", "options:" + options);
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
//		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
//			baos.reset();// 重置baos即清空baos
//			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
//		}
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        android.util.Log.d("image", "image after length--->" + baos.toByteArray().length / 1024);
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        StreamUtils.closeInputStream(isBm);
        StreamUtils.closeOutputStream(baos);

        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 50;
        while (baos.toByteArray().length / 1024 > 100 && options > 5) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 质量压缩
     *
     * @param imagePath
     * @return
     */
    public static Bitmap compressImage(String imagePath) {
        // 设置参数  
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出  
        BitmapFactory.decodeFile(imagePath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2  
        int minLen = Math.min(height, width); // 原图的最小边长  
        if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）  
            float ratio = (float) minLen / 100.0f; // 计算像素压缩比例  
            inSampleSize = (int) ratio;
        }
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了  
        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例  
        Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件  
        Log.w("TAG", "size: " + bm.getByteCount() + " width: " + bm.getWidth() + " heigth:" + bm.getHeight()); // 输出图像数据
        return bm;
    }


}
