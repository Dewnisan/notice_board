package com.example.eliavmenachi.simplelist.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by eliav.menachi on 17/05/2016.
 */
public class ModelCoulinary {
    Cloudinary cloudinary;

    public ModelCoulinary(){
        cloudinary = new Cloudinary("cloudinary://395375899647957:m1hUMLJ9-80xXrQbhHxSGJX_DvU@menachi");
    }

    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                    String name = imageName.substring(0,imageName.lastIndexOf("."));
                    Map res = cloudinary.uploader().upload(bs , ObjectUtils.asMap("public_id", name));
                    Log.d("TAG","save image to url" + res.get("url"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public Bitmap loadImage(String imageName) {
        URL url = null;
        try {
            url = new URL(cloudinary.url().generate(imageName));
            Log.d("TAG", "load image from url" + url);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bmp;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "url" + url);

        //http://res.cloudinary.com/menachi/image/upload/v1460463378/test.jpg.png
        return null;
    }

}