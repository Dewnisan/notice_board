package com.example.eliavmenachi.simplelist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.eliavmenachi.simplelist.model.Model;


public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        view.findViewById(R.id.fragment_register_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) getActivity().findViewById(R.id.fragment_register_et_name)).getText().toString();

                String password = ((EditText) getActivity().findViewById(R.id.fragment_register_et_password)).getText().toString();
                String passwordAgain = ((EditText) getActivity().findViewById(R.id.fragment_register_et_password_again)).getText().toString();

                if (!passwordsMatch(password, passwordAgain)) {
                    return;
                }

                Model.getInstance().register(email, password, new Model.AuthListener() {
                    @Override
                    public void onDone(String userId, Exception e) {
                        MyAlertDialog dialog;

                        if (e == null) {
                            mListener = (OnFragmentInteractionListener) getActivity();
                            mListener.onSave();

                            dialog = MyAlertDialog.newInstance("Registration Succeeded");
                        } else {
                            dialog = MyAlertDialog.newInstance("Registration Failed: " + e.getMessage());
                        }

                        dialog.setDelegate(new MyAlertDialog.Delegate() {
                            @Override
                            public void onOk() {
                                Log.d("RegisterFragment", "OK pressed");
                            }
                        });

                        dialog.show(getFragmentManager(), "RegisterFragment");
                    }
                });
            }
        });

        view.findViewById(R.id.fragment_register_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onCancel();
            }
        });

        return view;
    }

    private boolean passwordsMatch(String password, String passwordAgain) {
        if (!password.equals(passwordAgain)) {
            MyAlertDialog dialog = MyAlertDialog.newInstance("Passwords not match");
            dialog.setDelegate(new MyAlertDialog.Delegate() {
                @Override
                public void onOk() {
                    Log.d("RegisterFragment", "OK pressed");
                }
            });

            dialog.show(getFragmentManager(), "RegisterFragment");

            return false;
        }

        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onSave();

        void onCancel();
    }
}
