package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements LoginFragment.OnFragmentInteractionListener, GroupsFragment.OnFragmentInteractionListener, CreateGroupFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginFragment fragment = LoginFragment.newInstance();

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
    public void onLogin() {
        GroupsFragment fragment = GroupsFragment.newInstance();

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

    }

    @Override
    public void onSave() {
        getFragmentManager().popBackStack(1, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onCancel() {
        getFragmentManager().popBackStack(1, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
