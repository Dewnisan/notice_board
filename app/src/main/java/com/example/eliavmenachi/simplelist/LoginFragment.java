package com.example.eliavmenachi.simplelist;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
                String email = ((EditText) getActivity().findViewById(R.id.fragment_login_et_user_name)).getText().toString();
                String password = ((EditText) getActivity().findViewById(R.id.fragment_login_et_password)).getText().toString();

                Model.getInstance().login(email, password, new Model.AuthListener() {
                    @Override
                    public void onDone(String userId, Exception e) {
                        if (e == null) {
                            Log.d("LoginFragment", "Login success");
                            mListener = (OnFragmentInteractionListener) getActivity();
                            mListener.onLogin(userId);
                        } else {
                            Log.d("LoginFragment", e.getMessage());
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.fragment_login_btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) getActivity().findViewById(R.id.fragment_login_et_user_name)).getText().toString();
                String password = ((EditText) getActivity().findViewById(R.id.fragment_login_et_password)).getText().toString();

                Model.getInstance().register(email, password, new Model.AuthListener() {
                    @Override
                    public void onDone(String userId, Exception e) {
                        if (e == null) {
                            MyAlertDialog dialog = MyAlertDialog.newInstance("Registration Succeeded");
                            dialog.setDelegate(new MyAlertDialog.Delegate() {
                                @Override
                                public void onOk() {
                                    Log.d("LoginFragment", "OK pressed");
                                }
                            });

                            FragmentManager fragmentManager = getFragmentManager();
                            dialog.show(fragmentManager, "LoginFragment");
                        } else {
                            Log.d("LoginFragment", e.getMessage());
                        }
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onLogin(String userId);
    }
}
