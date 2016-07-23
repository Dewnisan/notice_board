package com.example.eliavmenachi.simplelist;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.User;


public class AddMemberFragment extends Fragment {
    private static String ARG_GROUP_ID = "GROUP_ID";

    private String mGroupId;

    private OnFragmentInteractionListener mListener;

    public AddMemberFragment() {
        // Required empty public constructor
    }

    public static AddMemberFragment newInstance(String groupId) {
        AddMemberFragment fragment = new AddMemberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GROUP_ID, groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGroupId = getArguments().getString(ARG_GROUP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);
        getActivity().setTitle(R.string.title_fragment_add_user);

        final EditText etName = (EditText) view.findViewById(R.id.fragment_add_member_et_name);

        Button btnAdd = (Button) view.findViewById(R.id.fragment_add_member_btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();

                Model.getInstance().getUserByNameAsync(name, new Model.GetUserListener() {
                    @Override
                    public void onResult(User user) {
                        if (user == null) {
                            MyAlertDialog dialog = MyAlertDialog.newInstance("No such user");
                            dialog.setDelegate(new MyAlertDialog.Delegate() {
                                @Override
                                public void onOk() {
                                    Log.d("AddMemberFragment", "OK pressed");
                                }
                            });

                            dialog.show(getFragmentManager(), "AddMemberFragment");
                        } else {
                            Model.getInstance().addMemberToGroup(user.getId(), mGroupId);
                            mListener = (OnFragmentInteractionListener) getActivity();
                            mListener.onAdd();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.fragment_add_member_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onCancel();
            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
        void onAdd();

        void onCancel();
    }

}
