package com.example.eliavmenachi.simplelist.model;

import android.content.Context;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by eliav.menachi on 17/05/2016.
 */
public class ModelFirebase {
    Firebase mFirebase;

    ModelFirebase(Context context) {
        Firebase.setAndroidContext(context);
        mFirebase = new Firebase("https://sweltering-inferno-2745.firebaseio.com/");
    }


    public void register(String email, String password, final Model.AuthListener listener) {
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

    public void login(String email, String password, final Model.AuthListener listener) {
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


    public void getAllStudentsAsynch(final Model.GetStudentsListener listener) {
        Firebase stRef = mFirebase.child("student");
        // Attach an listener to read the data at our posts reference
        stRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final List<Student> stList = new LinkedList<Student>();
                Log.d("TAG", "There are " + snapshot.getChildrenCount() + " blog posts");
                for (DataSnapshot stSnapshot : snapshot.getChildren()) {
                    Student st = stSnapshot.getValue(Student.class);
                    Log.d("TAG", st.getFname() + " - " + st.getId());
                    stList.add(st);
                }
                listener.onResult(stList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void getStudentById(String id, final Model.GetStudent listener) {
        Firebase stRef = mFirebase.child("student").child(id);
        stRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Student st = snapshot.getValue(Student.class);
                Log.d("TAG", st.getFname() + " - " + st.getId());
                listener.onResult(st);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void add(Student st) {
        Firebase stRef = mFirebase.child("student").child(st.getId());
        stRef.setValue(st);
    }
}

















