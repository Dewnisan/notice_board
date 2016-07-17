package com.example.eliavmenachi.simplelist.model;

import android.content.Context;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by eliav.menachi on 17/05/2016.
 */
public class ModelFirebase {
    private static final String FIREBASE_URL = "https://sweltering-inferno-2745.firebaseio.com/";

    Firebase mFirebase;

    ModelFirebase(Context context) {
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

    public void getAllUsersAsync(final Model.GetUsersListener listener) {
        Firebase ref = mFirebase.child("users");
        // Attach an listener to read the data at our posts reference
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
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
                System.out.println("The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void getUserById(String id, final Model.GetUser listener) {
        Firebase ref = mFirebase.child("users").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                listener.onResult(user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void add(User user) {
        Firebase ref = mFirebase.child("user").child(user.getId());
        ref.setValue(user);
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

                    if (group.getMembers().contains(Model.getInstance().getUserId())) {
                        list.add(group);
                    }
                }

                listener.onResult(list);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onCancel();
            }
        });
    }

    public void addGroup(Group group) {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = null;
        date = dateFormatGmt.format(new Date()).toString();

        group.setLastUpdated(date);
        Firebase ref = mFirebase.child("groups");

        Firebase newGroupRef = ref.push();
        group.setId(newGroupRef.getKey());
        newGroupRef.setValue(group);
    }
}

















