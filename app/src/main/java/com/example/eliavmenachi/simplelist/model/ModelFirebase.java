package com.example.eliavmenachi.simplelist.model;

import android.content.Context;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ModelFirebase {
    private static final String FIREBASE_URL = "https://sweltering-inferno-2745.firebaseio.com/";

    private Firebase mFirebase;

    public ModelFirebase(Context context) {
        Firebase.setAndroidContext(context);
        mFirebase = new Firebase(FIREBASE_URL);
    }

    public void signUp(String email, String password, final Model.AuthListener listener) {
        mFirebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                listener.onDone(result.get("uid").toString(), null);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                listener.onDone(null, firebaseError.toException());
            }
        });
    }

    public void signIn(String email, String password, final Model.AuthListener listener) {
        mFirebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                listener.onDone(authData.getUid(), null);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listener.onDone(null, firebaseError.toException());
            }
        });
    }

    public void signOut() {
        mFirebase.unauth();
    }

    public String getUserId() {
        AuthData authData = mFirebase.getAuth();
        if (authData != null) {
            return authData.getUid();
        } else {
            return null;
        }
    }

    public void addUser(User user) {
        String date = calculateDate();
        user.setLastUpdated(date);

        Firebase ref = mFirebase.child("users").child(user.getId());
        ref.setValue(user);
    }

    public void editUser(User user) {
        String date = calculateDate();
        user.setLastUpdated(date);

        Firebase ref = mFirebase.child("users").child(user.getId());
        ref.setValue(user);
    }

    public void getUserByIdAsync(String id, final Model.GetUserListener listener, String lastUpdateDate) {
        Firebase ref = mFirebase.child("users").child(id);
        Query queryRef = ref.orderByChild("lastUpdated").startAt(lastUpdateDate);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                listener.onResult(user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ModelFirebase", "The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void getUserByNameAsync(String name, final Model.GetUserListener listener, String lastUpdateDate) {
        Firebase ref = mFirebase.child("users");
        Query queryRef = ref.orderByChild("name").equalTo(name);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, HashMap<String, String>> map = (HashMap<String, HashMap<String, String>>) snapshot.getValue();

                Map.Entry<String, HashMap<String, String>> entry = map.entrySet().iterator().next();
                String key = entry.getKey();
                HashMap<String, String> value = entry.getValue();
                User user = new User(key, value.get("name"), value.get("imageName"));
                user.setLastUpdated(value.get("lastUpdated"));

                listener.onResult(user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ModelFirebase", "The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void getAllUsersAsync(final Model.GetUsersListener listener, String lastUpdateDate) {
        Firebase ref = mFirebase.child("users");
        Query queryRef = ref.orderByChild("lastUpdated").startAt(lastUpdateDate);

        // Attach an listener to read the data at our posts reference
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final List<User> list = new LinkedList<User>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    list.add(user);
                }

                listener.onResult(list);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ModelFirebase", "The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }


    public void addGroup(Group group) {
        String date = calculateDate();
        group.setLastUpdated(date);

        Firebase ref = mFirebase.child("groups");

        Firebase newGroupRef = ref.push();
        group.setId(newGroupRef.getKey());
        newGroupRef.setValue(group);
    }

    public void addUserToGroup(String userId, String groupId) {
        String date = calculateDate();

        Firebase ref = mFirebase.child("groups").child(groupId).child("members").child(userId);
        ref.setValue(userId);

        ref = mFirebase.child("groups").child(groupId).child("lastUpdated");
        ref.setValue(date);
    }

    public void removeUserFromGroup(String userId, String groupId) {
        String date = calculateDate();

        Firebase ref = mFirebase.child("groups").child(groupId).child("members").child(userId);
        ref.removeValue();

        ref = mFirebase.child("groups").child(groupId).child("lastUpdated");
        ref.setValue(date);
    }

    public void getGroupByIdAsync(String id, final Model.GetGroupListener listener, String lastUpdateDate) {
        Firebase ref = mFirebase.child("groups").child(id);
        Query queryRef = ref.orderByChild("lastUpdated").startAt(lastUpdateDate);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Group group = snapshot.getValue(Group.class);
                listener.onResult(group);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ModelFirebase", "The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void getAllUserGroupsAsync(final Model.GetGroupsListener listener, String lastUpdateDate) {
        Firebase ref = mFirebase.child("groups");
        Query queryRef = ref.orderByChild("lastUpdated").startAt(lastUpdateDate);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final List<Group> list = new LinkedList<Group>();

                for (DataSnapshot groupSnapshot : snapshot.getChildren()) {
                    Group group = groupSnapshot.getValue(Group.class);

                    if (group.getMembers().containsKey(Model.getInstance().getUserId())) {
                        list.add(group);
                    }
                }

                listener.onResult(list);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ModelFirebase", "The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void addPost(Post post) {
        String date = calculateDate();
        post.setLastUpdated(date);

        Firebase ref = mFirebase.child("posts");

        Firebase newPostRef = ref.push();
        post.setId(newPostRef.getKey());
        newPostRef.setValue(post);
    }

    public void getPostByIdAsync(String id, final Model.GetPostListener listener, String lastUpdateDate) {
        Firebase ref = mFirebase.child("posts").child(id);
        Query queryRef = ref.orderByChild("lastUpdated").startAt(lastUpdateDate);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                listener.onResult(post);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ModelFirebase", "The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void getAllGroupPostsAsync(String groupId, final Model.GetPostsListener listener, String lastUpdateDate) {
        Firebase ref = mFirebase.child("posts");
        Query queryRef = ref.orderByChild("group").equalTo(groupId);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final List<Post> list = new LinkedList<Post>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    list.add(post);
                }

                listener.onResult(list);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ModelFirebase", "The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    private String calculateDate() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = null;
        date = dateFormatGmt.format(new Date()).toString();

        return date;
    }
}

















