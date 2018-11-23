package com.astana.learnopengl.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author cpy
 * @Description:
 * @version:
 * @date: 2018/11/23
 */
public class BitmapUtils {

    private static final String TAG = "BitmapUtils";

    public static boolean isAvailableBitmap(Bitmap bmp) {
        return bmp != null && !bmp.isRecycled();
    }

    public static void release(Bitmap bmp) {
        if (isAvailableBitmap(bmp)) {
            bmp.recycle();
            bmp = null;
        }
    }

    public static Bitmap loadBitmapFromAssetFile(Context context, String filePath, int maxWidth, int maxHeight) {
        Bitmap bmp = null;
        InputStream stream = null;

        try {
            stream = getInputStreamFromAsset(context, filePath);
            if (maxHeight != -1 && maxWidth != -1) {
                bmp = loadBitmapFromStream(stream, maxWidth, maxHeight);
            } else {
                bmp = loadBitmapFromStream(stream);
            }
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }

        }

        return bmp;
    }

    /**
     * 获取用于读取asset目录下文件的输入流
     * @param context 上下文
     * @param assetFilePath 文件路径
     * @return 用于读取asset目录下文件的输入流
     * @throws IOException
     */
    public static InputStream getInputStreamFromAsset(Context context, final String assetFilePath)
            throws IOException {
        return context.getAssets().open(assetFilePath);
    }

    public static Bitmap loadBitmapFromStream(InputStream stream) {
        if (stream == null) {
            return null;
        } else {
            Bitmap bmp = null;

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inPurgeable = true;
                options.inScaled = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bmp = BitmapFactory.decodeStream(stream, new Rect(), options);
            } catch (OutOfMemoryError var3) {
                var3.printStackTrace();
            }

            return bmp;
        }
    }

    public static Bitmap loadBitmapFromStream(InputStream stream, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = null;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = true;
        Rect rect = new Rect();
        BitmapFactory.decodeStream(stream, rect, options);

        try {
            stream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        options.inSampleSize = computeSampleSize(options, -1, maxWidth * maxHeight);
        Log.d("BitmapUtil", "options.inSampleSize = " + options.inSampleSize);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;

        try {
            bitmap = BitmapFactory.decodeStream(stream, rect, options);
        } catch (OutOfMemoryError var7) {
            var7.printStackTrace();
        }

        return bitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            for(roundedSize = 1; roundedSize < initialSize; roundedSize <<= 1) {
                ;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = (double)options.outWidth;
        double h = (double)options.outHeight;
        int lowerBound = maxNumOfPixels == -1 ? 1 : (int)Math.ceil(Math.sqrt(w * h / (double)maxNumOfPixels));
        int upperBound = minSideLength == -1 ? 128 : (int)Math.min(Math.floor(w / (double)minSideLength), Math.floor(h / (double)minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        } else if (maxNumOfPixels == -1 && minSideLength == -1) {
            return 1;
        } else {
            return minSideLength == -1 ? lowerBound : upperBound;
        }
    }
}
