package com.example.eliavmenachi.simplelist;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.eliavmenachi.simplelist.model.Model;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        view.findViewById(R.id.fragment_login_btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) getActivity().findViewById(R.id.fragment_login_et_name)).getText().toString();
                String password = ((EditText) getActivity().findViewById(R.id.fragment_login_et_password)).getText().toString();

                Model.getInstance().login(email, password, new Model.AuthListener() {
                    @Override
                    public void onDone(String userId, Exception e) {
                        if (e == null) {
                            mListener = (OnFragmentInteractionListener) getActivity();
                            mListener.onLogin();
                        } else {
                            MyAlertDialog dialog = MyAlertDialog.newInstance("Login failed: " + e.getMessage());
                            dialog.setDelegate(new MyAlertDialog.Delegate() {
                                @Override
                                public void onOk() {
                                    Log.d("LoginFragment", "OK pressed");
                                }
                            });

                            dialog.show(getFragmentManager(), "LoginFragment");
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.fragment_login_btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onRegister();
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_groups, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public interface OnFragmentInteractionListener {
        void onLogin();

        void onRegister();
    }
}
