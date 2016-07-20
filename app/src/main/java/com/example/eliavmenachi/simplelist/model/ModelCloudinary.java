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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by eliav.menachi on 17/05/2016.
 */
public class ModelCloudinary {
    private static final String CLOUDINARY_URL = "cloudinary://421694581797959:TIm0KXgFla2e129ByOKaYCucxjc@doipsyjoy";

    Cloudinary mCloudinary;

    public ModelCloudinary() {
        mCloudinary = new Cloudinary(CLOUDINARY_URL);
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

                    String name = imageName.substring(0, imageName.lastIndexOf("."));
                    Map res = mCloudinary.uploader().upload(bs, ObjectUtils.asMap("public_id", name));


                    Log.d("ModelCloudinary", "Saved image to URL: " + res.get("url"));
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
            url = new URL(mCloudinary.url().generate(imageName));

            Log.d("ModelCloudinary", "Loaded image from URL: " + url);

            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            return bmp;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //http://res.cloudinary.com/menachi/image/upload/v1460463378/test.jpg.png
        return null;
    }

    public void deleteImage(String imageName) {
        List<String> list = new LinkedList<String>();
        list.add(imageName);

        try {
            mCloudinary.api().deleteResources(list, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
