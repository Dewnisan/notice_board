package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.eliavmenachi.simplelist.model.Group;
import com.example.eliavmenachi.simplelist.model.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateGroupFragment extends Fragment {
    private ImageView mImageView;
    private String mImageFileName;
    private Bitmap mImageBitmap;

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

        mImageView = (ImageView) view.findViewById(R.id.fragment_create_group_iv_image);

        final EditText etName = ((EditText) view.findViewById(R.id.fragment_create_group_et_name));

        Button btnSave = (Button) view.findViewById(R.id.fragment_create_group_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                String name = etName.getText().toString();
                String owner = Model.getInstance().getUserId();

                if (mImageBitmap != null) {
                    mImageFileName = name + timeStamp + ".jpg";
                }

                Group group = new Group("id", name, owner, mImageFileName);
                Model.getInstance().addGroup(group);

                if (mImageBitmap != null) {
                    Model.getInstance().saveImage(mImageBitmap, mImageFileName);
                }

                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onSave();

                MyAlertDialog dialog = MyAlertDialog.newInstance("Group creation succeeded");
                dialog.setDelegate(new MyAlertDialog.Delegate() {
                    @Override
                    public void onOk() {
                        Log.d("CreateGroupFragment", "OK pressed");
                    }

                });

                dialog.show(getFragmentManager(), getResources().getString(R.string.title_fragment_create_group));
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.fragment_create_group_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onCancel();
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(mImageBitmap);
        }
    }

    public interface OnFragmentInteractionListener {
        void onSave();

        void onCancel();
    }
}
