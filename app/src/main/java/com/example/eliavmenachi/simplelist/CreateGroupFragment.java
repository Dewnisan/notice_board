package com.example.eliavmenachi.simplelist;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.eliavmenachi.simplelist.model.Group;
import com.example.eliavmenachi.simplelist.model.Model;

public class CreateGroupFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    public static CreateGroupFragment newInstance() {
        CreateGroupFragment fragment = new CreateGroupFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        getActivity().setTitle(R.string.title_fragment_create_group);

        view.findViewById(R.id.fragment_create_group_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText) getActivity().findViewById(R.id.fragment_create_group_et_name)).getText().toString();
                String owner = Model.getInstance().getUserId();
                Group group = new Group(name, owner);

                Model.getInstance().addGroup(group);

                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onSave();

                MyAlertDialog dialog = MyAlertDialog.newInstance("Group creation succeeded");
                dialog.setDelegate(new MyAlertDialog.Delegate() {
                    @Override
                    public void onOk() {
                        Log.d("CreateGroupFragment", "OK pressed");
                    }

                });

                dialog.show(getFragmentManager(), getResources().getString(R.string.title_activity_new_student));
            }
        });

        view.findViewById(R.id.fragment_create_group_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onCancel();
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
        void onSave();

        void onCancel();
    }
}
