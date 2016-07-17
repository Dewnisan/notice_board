package com.example.eliavmenachi.simplelist.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.eliavmenachi.simplelist.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by eliav.menachi on 25/03/2015.
 */
public class Model {
    private final static Model mInstance = new Model();

    private Context mContext;
    private ModelFirebase mModelFirebase;
    private ModelCloudinary mModelCloudinary;
    private ModelSql mModelSql;

    private Model() {
        mContext = MyApplication.getAppContext();
        mModelFirebase = new ModelFirebase(MyApplication.getAppContext());
        mModelCloudinary = new ModelCloudinary();
        mModelSql = new ModelSql(MyApplication.getAppContext());
    }

    public static Model getInstance() {
        return mInstance;
    }

    public void signUp(String email, String pwd, AuthListener listener) {
        mModelFirebase.signUp(email, pwd, listener);
    }

    public void signIn(String email, String pwd, AuthListener listener) {
        mModelFirebase.signIn(email, pwd, listener);
    }

    public void signOut() {
        mModelFirebase.signOut();
    }

    public String getUserId() {
        return mModelFirebase.getUserId();
    }

    public interface GetGroupsListener {
        public void onResult(List<Group> groups);

        public void onCancel();
    }

    public void getAllUserGroupsAsync(GetGroupsListener listener) {
        mModelFirebase.getAllUserGroupsAsync(listener);
    }

    public interface AuthListener {
        void onDone(String userId, Exception e);
    }

    public void addGroup(Group group) {
        mModelFirebase.addGroup(group);
    }

    public interface GetUsersListener {
        public void onResult(List<User> Users);

        public void onCancel();
    }

    public void getAllUsersAsync(GetUsersListener listener) {
        mModelFirebase.getAllUsersAsync(listener);
    }

    public interface GetUser {
        public void onResult(User user);

        public void onCancel();
    }

    public void getUserById(String id, GetUser listener) {
        mModelFirebase.getUserById(id, listener);
    }

    public void add(User user) {
        mModelFirebase.add(user);
    }


    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        saveImageToFile(imageBitmap, imageName); // synchronously save image locally
        Thread d = new Thread(new Runnable() {  // asynchronously save image to parse
            @Override
            public void run() {
                mModelCloudinary.saveImage(imageBitmap, imageName);
            }
        });
        d.start();
    }

    public interface LoadImageListener {
        public void onResult(Bitmap imageBmp);
    }

    public void loadImage(final String imageName, final LoadImageListener listener) {
        AsyncTask<String, String, Bitmap> task = new AsyncTask<String, String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bmp = loadImageFromFile(imageName);              //first try to fin the image on the device
                if (bmp == null) {                                      //if image not found - try downloading it from parse
                    bmp = mModelCloudinary.loadImage(imageName);
                    if (bmp != null)
                        saveImageToFile(bmp, imageName);    //save the image locally for next time
                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                listener.onResult(result);
            }
        };
        task.execute();
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName) {
        FileOutputStream fos;
        OutputStream out = null;
        try {
            //File dir = context.getExternalFilesDir(null);
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir, imageFileName);
            imageFile.createNewFile();

            out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //add the picture to the gallery so we dont need to manage the cache size
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            mContext.sendBroadcast(mediaScanIntent);
            Log.d("tag", "add image to cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName) {
        String str = null;
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir, imageFileName);

            //File dir = mContext.getExternalFilesDir(null);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag", "got image from cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
