package com.binancetracker.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.binancetracker.MyApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageCache {

    private final String imagesdownloadPath = "https://raw.githubusercontent.com/KillerInk/cryptocurrency-icons/master/32/icon/";
    private WeakReference<ImageView> weakReference;
    private String name;

    public ImageCache(ImageView imageView, String assetName)
    {
        weakReference = new WeakReference(imageView);
        this.name = assetName;
    }

    public void loadBitmap()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmap();
                ImageView imageView = weakReference.get();
                if(imageView != null)
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
            }
        }).start();
    }

    private Bitmap getBitmap()
    {
        File cacheFile = new File(MyApplication.getAppCacheDir(), name);
        if (cacheFile.exists())
            return BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
        else
            return downloadFile(name,cacheFile);
    }

    private Bitmap downloadFile(String asset,File cacheFile)
    {
        String uri = imagesdownloadPath+asset+".png";

        try{
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            try (FileOutputStream out = new FileOutputStream(cacheFile)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
