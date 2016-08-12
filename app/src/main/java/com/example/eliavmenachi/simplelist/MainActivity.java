package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eliavmenachi.simplelist.model.Model;


public class MainActivity extends Activity implements SignUpFragment.OnFragmentInteractionListener,
        SignInFragment.OnFragmentInteractionListener,
        UserDetailsFragment.OnFragmentInteractionListener,
        EditUserFragment.OnFragmentInteractionListener,
        GroupsFragment.OnFragmentInteractionListener,
        GroupDetailsFragment.OnFragmentInteractionListener,
        CreateGroupFragment.OnFragmentInteractionListener,
        AddMemberFragment.OnFragmentInteractionListener,
        EditGroupFragment.OnFragmentInteractionListener,
        PostsFragment.OnFragmentInteractionListener,
        CreatePostFragment.OnFragmentInteractionListener {

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
        GroupsFragment fragment = GroupsFragment.newInstance();
        goToFragment(fragment);
    }

    @Override
    public void onSignUp() {
        SignUpFragment fragment = SignUpFragment.newInstance();
        goToFragment(fragment);
    }

    @Override
    public void onCreateGroupItemSelected() {
        CreateGroupFragment fragment = CreateGroupFragment.newInstance();
        goToFragment(fragment);
    }

    @Override
    public void onProfileItemSelected() {
        UserDetailsFragment fragment = UserDetailsFragment.newInstance();
        goToFragment(fragment);
    }

    @Override
    public void onGroupSelected(String groupId) {
        PostsFragment fragment = PostsFragment.newInstance(groupId);
        goToFragment(fragment);
    }

    @Override
    public void onSignOutItemSelected() {
        Model.getInstance().signOut();
        getFragmentManager().popBackStack();
    }

    @Override
    public void onEditUserItemSelected() {
        EditUserFragment fragment = EditUserFragment.newInstance();
        goToFragment(fragment);
    }

    @Override
    public void onCreatePostItemSelected(String groupId) {
        CreatePostFragment fragment = CreatePostFragment.newInstance(groupId);
        goToFragment(fragment);
    }

    @Override
    public void onPostSelected(String postId) {
        PostDetailsFragment fragment = PostDetailsFragment.newInstance(postId);
        goToFragment(fragment);
    }

    @Override
    public void onAddMemberItemSelected(String groupId) {
        AddMemberFragment fragment = AddMemberFragment.newInstance(groupId);
        goToFragment(fragment);
    }

    @Override
    public void onRemoveMemberItemSelected(String groupId) {
        RemoveMemberFragment fragment = RemoveMemberFragment.newInstance(groupId);
        goToFragment(fragment);
    }


    @Override
    public void onGroupDetailsItemSelected(String id) {
        GroupDetailsFragment fragment = GroupDetailsFragment.newInstance(id);
        goToFragment(fragment);
    }

    @Override
    public void onEditGroupItemSelected(String id) {
        EditGroupFragment fragment = EditGroupFragment.newInstance(id);
        goToFragment(fragment);
    }

    @Override
    public void onExitGroupItemSelected() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onSave() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onAdd() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onCancel() {
        //1, FragmentManager.POP_BACK_STACK_INCLUSIVE
        getFragmentManager().popBackStack();
    }

    private void goToFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
