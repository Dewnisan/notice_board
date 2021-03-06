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
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

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

    public void getUserByIdAsync(final String id, final GetUserListener listener) {
        final String lastUpdateDate = UserSql.getLastUpdateDate(mModelSql.getReadableDB());
        mModelFirebase.getUserByIdAsync(id, new GetUserListener() {
            @Override
            public void onResult(User user) {
                User result = null;

                if (user != null) {
                    // Update the local DB
                    String recentUpdate = lastUpdateDate;
                    UserSql.add(mModelSql.getWritableDB(), user);
                    if (recentUpdate == null || user.getLastUpdated().compareTo(recentUpdate) > 0) {
                        recentUpdate = user.getLastUpdated();
                    }

                    UserSql.setLastUpdateDate(mModelSql.getWritableDB(), recentUpdate);
                    result = UserSql.getById(mModelSql.getReadableDB(), id);
                }

                listener.onResult(result);
            }

            @Override
            public void onCancel() {
                // Try to handle request with local data
                User result = UserSql.getById(mModelSql.getReadableDB(), id);
                if (result != null) {
                    listener.onResult(result);
                } else {
                    listener.onCancel();
                }
            }
        }, lastUpdateDate);
    }

    public void getUserByNameAsync(final String name, final GetUserListener listener) {
        final String lastUpdateDate = UserSql.getLastUpdateDate(mModelSql.getReadableDB());
        mModelFirebase.getUserByNameAsync(name, new GetUserListener() {
            @Override
            public void onResult(User user) {
                User result = null;

                if (user != null) {
                    // Update the local DB
                    String recentUpdate = lastUpdateDate;
                    UserSql.add(mModelSql.getWritableDB(), user);
                    if (recentUpdate == null || user.getLastUpdated().compareTo(recentUpdate) > 0) {
                        recentUpdate = user.getLastUpdated();
                    }

                    UserSql.setLastUpdateDate(mModelSql.getWritableDB(), recentUpdate);
                    result = UserSql.getByName(mModelSql.getReadableDB(), name);
                }

                listener.onResult(result);
            }

            @Override
            public void onCancel() {
                // Try to handle request with local data
                User result = UserSql.getByName(mModelSql.getReadableDB(), name);
                if (result != null) {
                    listener.onResult(result);
                } else {
                    listener.onCancel();
                }
            }
        }, lastUpdateDate);
    }

    public void getAllUsersAsync(final GetUsersListener listener) {
        final String lastUpdateDate = UserSql.getLastUpdateDate(mModelSql.getReadableDB());
        mModelFirebase.getAllUsersAsync(new GetUsersListener() {
            @Override
            public void onResult(List<User> users) {
                List<User> result = null;

                if (users != null && users.size() > 0) {
                    // Update the local DB
                    String recentUpdate = lastUpdateDate;
                    for (User user : users) {
                        UserSql.add(mModelSql.getWritableDB(), user);
                        if (recentUpdate == null || user.getLastUpdated().compareTo(recentUpdate) > 0) {
                            recentUpdate = user.getLastUpdated();
                        }
                    }

                    UserSql.setLastUpdateDate(mModelSql.getWritableDB(), recentUpdate);
                    result = UserSql.getAll(mModelSql.getReadableDB());
                }

                listener.onResult(result);
            }

            @Override
            public void onCancel() {
                // Try to handle request with local data
                List<User> result = UserSql.getAll(mModelSql.getReadableDB());
                if (result != null) {
                    listener.onResult(result);
                } else {
                    listener.onCancel();
                }
            }
        }, lastUpdateDate);
    }

    public void addUser(User user) {
        mModelFirebase.addUser(user);
    }

    public void editUser(User user) {
        mModelFirebase.editUser(user);
    }

    public void editGroup(Group group) {
        mModelFirebase.editGroup(group);
    }

    public void getGroupByIdAsync(final String id, final GetGroupListener listener) {
        final String lastUpdateDate = GroupSql.getLastUpdateDate(mModelSql.getReadableDB());
        mModelFirebase.getGroupByIdAsync(id, new GetGroupListener() {
            @Override
            public void onResult(Group group) {
                Group result = null;

                if (group != null) {
                    // Update the local DB
                    String recentUpdate = lastUpdateDate;
                    GroupSql.add(mModelSql.getWritableDB(), group);
                    if (recentUpdate == null || group.getLastUpdated().compareTo(recentUpdate) > 0) {
                        recentUpdate = group.getLastUpdated();
                    }

                    GroupSql.setLastUpdateDate(mModelSql.getWritableDB(), recentUpdate);
                    result = GroupSql.getById(mModelSql.getReadableDB(), id);
                }

                listener.onResult(result);
            }

            @Override
            public void onCancel() {
                // Try to handle request with local data
                Group result = GroupSql.getById(mModelSql.getReadableDB(), id);
                if (result != null) {
                    listener.onResult(result);
                } else {
                    listener.onCancel();
                }
            }
        }, lastUpdateDate);
    }

    public void getAllUserGroupsAsync(final GetGroupsListener listener) {
        final String lastUpdateDate = GroupSql.getLastUpdateDate(mModelSql.getReadableDB());
        mModelFirebase.getAllUserGroupsAsync(new GetGroupsListener() {
            @Override
            public void onResult(List<Group> groups) {
                List<Group> result = null;

                if (groups != null && groups.size() > 0) {
                    // Update the local DB
                    String recentUpdate = lastUpdateDate;
                    for (Group group : groups) {
                        GroupSql.add(mModelSql.getWritableDB(), group);
                        if (recentUpdate == null || group.getLastUpdated().compareTo(recentUpdate) > 0) {
                            recentUpdate = group.getLastUpdated();
                        }
                    }

                    GroupSql.setLastUpdateDate(mModelSql.getWritableDB(), recentUpdate);
                    result = GroupSql.getAllByUser(mModelSql.getReadableDB(), Model.getInstance().getUserId());
                }

                listener.onResult(result);
            }

            @Override
            public void onCancel() {
                // Try to handle request with local data
                List<Group> result = GroupSql.getAllByUser(mModelSql.getReadableDB(), Model.getInstance().getUserId());
                if (result != null) {
                    listener.onResult(result);
                } else {
                    listener.onCancel();
                }
            }
        }, lastUpdateDate);
    }

    public void getAllGroupUsersAsync(final String groupId, final GetUsersListener listener) {
        final String lastUpdateDate = UserSql.getLastUpdateDate(mModelSql.getReadableDB());
        mModelFirebase.getAllGroupUsersAsync(groupId, new GetUsersListener() {
            @Override
            public void onResult(List<User> users) {
                List<User> result = null;

                if (users != null && users.size() > 0) {
                    // Update the local DB
                    String recentUpdate = lastUpdateDate;
                    for (User user : users) {
                        UserSql.add(mModelSql.getWritableDB(), user);
                        if (recentUpdate == null || user.getLastUpdated().compareTo(recentUpdate) > 0) {
                            recentUpdate = user.getLastUpdated();
                        }
                    }

                    UserSql.setLastUpdateDate(mModelSql.getWritableDB(), recentUpdate);

                    result = new LinkedList<User>();
                    Group group = GroupSql.getById(mModelSql.getReadableDB(), groupId);
                    if (group != null) {
                        for (String member : group.getMembers().values()) {
                            User user = UserSql.getById(mModelSql.getReadableDB(), member);
                            if (user != null) {
                                result.add(user);
                            }
                        }
                    }
                }

                listener.onResult(result);
            }

            @Override
            public void onCancel() {
                // Try to handle request with local data
                List<User> result = new LinkedList<User>();

                Group group = GroupSql.getById(mModelSql.getReadableDB(), groupId);
                if (group != null) {
                    for (String member : group.getMembers().values()) {
                        User user = UserSql.getById(mModelSql.getReadableDB(), member);
                        if (user != null) {
                            result.add(user);
                        }
                    }
                }

                if (result != null) {
                    listener.onResult(result);
                } else {
                    listener.onCancel();
                }
            }
        }, lastUpdateDate);
    }

    public void addGroup(Group group) {
        mModelFirebase.addGroup(group);
    }

    public void addMemberToGroup(String userId, String groupId) {
        mModelFirebase.addUserToGroup(userId, groupId);
    }

    public void removeMemberFromGroup(String userId, String groupId) {
        mModelFirebase.removeUserFromGroup(userId, groupId);
        GroupSql.remove(mModelSql.getWritableDB(), groupId);
    }

    public void getAllGroupPostsAsync(final String groupId, final GetPostsListener listener) {
        final String lastUpdateDate = PostSql.getLastUpdateDate(mModelSql.getReadableDB());
        mModelFirebase.getAllGroupPostsAsync(groupId, new GetPostsListener() {
            @Override
            public void onResult(List<Post> posts) {
                List<Post> result = null;

                if (posts != null && posts.size() > 0) {
                    //update the local DB
                    String recentUpdate = lastUpdateDate;
                    for (Post post : posts) {
                        PostSql.add(mModelSql.getWritableDB(), post);
                        if (recentUpdate == null || post.getLastUpdated().compareTo(recentUpdate) > 0) {
                            recentUpdate = post.getLastUpdated();
                        }
                    }

                    PostSql.setLastUpdateDate(mModelSql.getWritableDB(), recentUpdate);
                    result = PostSql.getAllByGroup(mModelSql.getReadableDB(), groupId);
                }

                listener.onResult(result);
            }

            @Override
            public void onCancel() {
                // Try to handle request with local data
                List<Post> result = PostSql.getAllByGroup(mModelSql.getReadableDB(), groupId);
                if (result != null) {
                    listener.onResult(result);
                } else {
                    listener.onCancel();
                }
            }
        }, lastUpdateDate);
    }

    public void getPostByIdAsync(final String id, final GetPostListener listener) {
        final String lastUpdateDate = PostSql.getLastUpdateDate(mModelSql.getReadableDB());
        mModelFirebase.getPostByIdAsync(id, new GetPostListener() {
            @Override
            public void onResult(Post post) {
                Post result = null;

                if (post != null) {
                    //update the local DB
                    String recentUpdate = lastUpdateDate;
                    PostSql.add(mModelSql.getWritableDB(), post);
                    if (recentUpdate == null || post.getLastUpdated().compareTo(recentUpdate) > 0) {
                        recentUpdate = post.getLastUpdated();
                    }

                    PostSql.setLastUpdateDate(mModelSql.getWritableDB(), recentUpdate);
                    result = PostSql.getById(mModelSql.getReadableDB(), id);
                }

                listener.onResult(result);
            }

            @Override
            public void onCancel() {
                // Try to handle request with local data
                Post result = PostSql.getById(mModelSql.getReadableDB(), id);
                if (result != null) {
                    listener.onResult(result);
                } else {
                    listener.onCancel();
                }
            }
        }, lastUpdateDate);
    }

    public void addPost(Post post) {
        mModelFirebase.addPost(post);
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

    public void deleteImage(String imageName) {
        mModelCloudinary.deleteImage(imageName);
    }

    public void loadImageAsync(final String imageName, final LoadImageListener listener) {
        AsyncTask<String, String, Bitmap> task = new AsyncTask<String, String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bmp = loadImageFromFile(imageName); // First try to find the image on the device

                if (bmp == null) { // If image not found - try downloading it from parse
                    bmp = mModelCloudinary.loadImage(imageName);
                    if (bmp != null) {
                        saveImageToFile(bmp, imageName); // Save the image locally for next time
                    }
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

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName) {
        FileOutputStream fos;
        OutputStream out = null;

        try {
            //File dir = context.getExternalFilesDir(null);
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            if (!dir.exists()) {
                dir.mkdir();
            }

            File imageFile = new File(dir, imageFileName);
            imageFile.createNewFile();

            out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            // Add the picture to the gallery so we don't need to manage the cache size
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

    public void addListenerForValueEvent(String table, ValueEventListener listener) {
        mModelFirebase.addListenerForValueEvent(table, listener);
    }


    public interface AuthListener {
        void onDone(String userId, Exception e);
    }

    public interface GetUserListener {
        public void onResult(User user);

        public void onCancel();
    }

    public interface GetUsersListener {
        public void onResult(List<User> Users);

        public void onCancel();
    }

    public interface GetGroupListener {
        public void onResult(Group group);

        public void onCancel();
    }

    public interface GetGroupsListener {
        public void onResult(List<Group> groups);

        public void onCancel();
    }

    public interface GetPostsListener {
        public void onResult(List<Post> posts);

        public void onCancel();
    }

    public interface GetPostListener {
        public void onResult(Post post);

        public void onCancel();
    }

    public interface LoadImageListener {
        public void onResult(Bitmap imageBmp);
    }
}
