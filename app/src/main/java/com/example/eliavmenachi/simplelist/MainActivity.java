package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements SignUpFragment.OnFragmentInteractionListener,
        SignInFragment.OnFragmentInteractionListener,
        UserDetailsFragment.OnFragmentInteractionListener,
        EditUserFragment.OnFragmentInteractionListener,
        GroupsFragment.OnFragmentInteractionListener,
        CreateGroupFragment.OnFragmentInteractionListener,
        AddUserToGroupFragment.OnFragmentInteractionListener,
        PostsFragment.OnFragmentInteractionListener,
        CreatePostFragment.OnFragmentInteractionListener {

    private int mDefaultFragmentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInFragment fragment = SignInFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.activity_main_fragment_container, fragment);
        transaction.show(fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSignIn() {
        mDefaultFragmentIndex = 1;

        GroupsFragment fragment = GroupsFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onSignUp() {
        SignUpFragment fragment = SignUpFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onCreateGroupItemSelected() {
        CreateGroupFragment fragment = CreateGroupFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onProfileItemSelected() {
        UserDetailsFragment fragment = UserDetailsFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onGroupSelected(String groupId) {
        PostsFragment fragment = PostsFragment.newInstance(groupId);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onEditUserItemSelected() {
        EditUserFragment fragment = EditUserFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onCreatePostItemSelected(String groupId) {
        CreatePostFragment fragment = CreatePostFragment.newInstance(groupId);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onPostSelected(String postId) {
        PostDetailsFragment fragment = PostDetailsFragment.newInstance(postId);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onAddUserItemSelected(String groupId) {
        AddUserToGroupFragment fragment = AddUserToGroupFragment.newInstance(groupId);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onSave() {
        getFragmentManager().popBackStack(mDefaultFragmentIndex, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onAdd() {
        getFragmentManager().popBackStack(mDefaultFragmentIndex, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onCancel() {
        //1, FragmentManager.POP_BACK_STACK_INCLUSIVE
        getFragmentManager().popBackStack(mDefaultFragmentIndex, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
