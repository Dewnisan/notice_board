package com.example.eliavmenachi.simplelist;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.eliavmenachi.simplelist.model.Model;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        Button btnSignIn = (Button) view.findViewById(R.id.fragment_sign_in_btn_sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) getActivity().findViewById(R.id.fragment_sign_in_et_name)).getText().toString();
                String password = ((EditText) getActivity().findViewById(R.id.fragment_sign_in_et_password)).getText().toString();

                Model.getInstance().signIn(email, password, new Model.AuthListener() {
                    @Override
                    public void onDone(String userId, Exception e) {
                        if (e == null) {
                            mListener = (OnFragmentInteractionListener) getActivity();
                            mListener.onSignIn();
                        } else {
                            MyAlertDialog dialog = MyAlertDialog.newInstance("Login failed: " + e.getMessage());
                            dialog.setDelegate(new MyAlertDialog.Delegate() {
                                @Override
                                public void onOk() {
                                    Log.d("SignInFragment", "OK pressed");
                                }
                            });

                            dialog.show(getFragmentManager(), "SignInFragment");
                        }
                    }
                });
            }
        });

        Button btnSignUp = (Button) view.findViewById(R.id.fragment_sign_in_btn_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onSignUp();
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

        return super.onOptionsItemSelected(item);
    }

    public interface OnFragmentInteractionListener {
        void onSignIn();

        void onSignUp();
    }
}
