package com.vtec.terrassteel.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BitmapUtil {

    private BitmapUtil() {
    }


    public static Bitmap getSampleBitmap(Context context, Uri uri) throws IOException {
        final ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(uri, "r");
        BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(fd.getFileDescriptor(), false);
        int width = bitmapRegionDecoder.getWidth();
        int height = bitmapRegionDecoder.getHeight();
        Rect rect = new Rect(0, height / 2 - width / 2, width, height / 2 + width / 2);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        return bitmapRegionDecoder.decodeRegion(rect, options);
    }

    public static byte[] decodeFile(Context context, Uri uri) throws IOException {

        String root = Environment.getExternalStorageDirectory().toString() + "/wipliuz";
        File myFile = new File(root, uri.getPath());


        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        if (bitmap == null) {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public static Uri copyBitmap(Context context, Uri uri, File destination) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        FileOutputStream out = new FileOutputStream(destination);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } finally {
            out.close();
        }
        return Uri.fromFile(destination);
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

}
