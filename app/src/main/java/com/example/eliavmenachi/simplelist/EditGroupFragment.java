package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.eliavmenachi.simplelist.model.Group;
import com.example.eliavmenachi.simplelist.model.Model;

import java.text.SimpleDateFormat;
import java.util.Date;


public class EditGroupFragment extends Fragment {

    private static final String ARG_GROUP_ID = "GROUP_ID";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private String mGroupId;
    private ImageView mImageView;
    private String mImageFileName;
    private Bitmap mImageBitmap;

    private Group mCurrentGroup;

    private OnFragmentInteractionListener mListener;

    public EditGroupFragment() {

    }

    public static EditGroupFragment newInstance(String id) {
        EditGroupFragment fragment = new EditGroupFragment();

        Bundle args = new Bundle();
        args.putString(ARG_GROUP_ID, id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mGroupId = getArguments().getString(ARG_GROUP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_group, container, false);

        getActivity().setTitle(R.string.title_fragment_edit_group);
        mListener = (OnFragmentInteractionListener) getActivity();

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.fragment_edit_group_pb);
        final ProgressBar imageProgressBar = (ProgressBar) view.findViewById(R.id.fragment_edit_group_pb_image);
        final EditText etName = (EditText) view.findViewById(R.id.fragment_edit_group_tv_name);
        mImageView = (ImageView) view.findViewById(R.id.fragment_edit_group_iv_image);

        Model.getInstance().getGroupByIdAsync(mGroupId, new Model.GetGroupListener() {
            @Override
            public void onResult(Group group) {
                mCurrentGroup = group;
                etName.setText(mCurrentGroup.getName());

                if (mCurrentGroup.getImageName() != null) {
                    imageProgressBar.setVisibility(View.VISIBLE);
                    Model.getInstance().loadImageAsync(mCurrentGroup.getImageName(), new Model.LoadImageListener() {
                        @Override
                        public void onResult(Bitmap imageBmp) {
                            if (mImageView != null) {
                                mImageView.setImageBitmap(imageBmp);
                                mImageBitmap = imageBmp;
                                mImageFileName = mCurrentGroup.getImageName();
                            }

                            imageProgressBar.setVisibility(View.GONE);
                        }
                    });
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {

            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        Button btnSave = (Button) view.findViewById(R.id.fragment_edit_group_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etName.getText().toString();
                mCurrentGroup.setName(s);
                Model.getInstance().editGroup(mCurrentGroup);

                if (mImageBitmap != null && mImageFileName != mCurrentGroup.getImageName()) {
                    if (mCurrentGroup.getImageName() != null) {
                        Model.getInstance().deleteImage(mCurrentGroup.getImageName());
                    }

                    mCurrentGroup.setImageName(mImageFileName);

                    Model.getInstance().saveImage(mImageBitmap, mImageFileName);
                    Model.getInstance().editGroup(mCurrentGroup);
                }
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onSave();

                MyAlertDialog dialog = MyAlertDialog.newInstance("Changes successfully saved");
                dialog.setDelegate(new MyAlertDialog.Delegate() {
                    @Override
                    public void onOk() {
                        Log.d("EditGroupFragment", "OK pressed");
                    }

                });

                dialog.show(getFragmentManager(), getResources().getString(R.string.title_fragment_edit_group));
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.fragment_edit_group_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        void onCancel();

        void onSave();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(mImageBitmap);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            mImageFileName = mCurrentGroup.getName() + timeStamp + ".jpg";
        }
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
