package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditUserFragment extends Fragment {
    private ImageView mImageView;
    private String mImageFileName;
    private Bitmap mImageBitmap;

    private User mCurrentUser;

    private OnFragmentInteractionListener mListener;

    public EditUserFragment() {
        // Required empty public constructor
    }

    public static EditUserFragment newInstance() {
        EditUserFragment fragment = new EditUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.fragment_edit_user_pb);
        final ProgressBar imageProgressBar = (ProgressBar) view.findViewById(R.id.fragment_edit_user_image_pb);

        final TextView tvName = (TextView) view.findViewById(R.id.fragment_edit_user_tv_name);
        mImageView = (ImageView) view.findViewById(R.id.fragment_edit_user_img);

        String userId = Model.getInstance().getUserId();
        Model.getInstance().getUserById(userId, new Model.GetUserListener() {
            @Override
            public void onResult(final User user) {
                mCurrentUser = user;

                tvName.setText(mCurrentUser.getName());
                if (mCurrentUser.getImageName() != null) {
                    imageProgressBar.setVisibility(View.VISIBLE);
                    Model.getInstance().loadImage(mCurrentUser.getImageName(), new Model.LoadImageListener() {
                        @Override
                        public void onResult(Bitmap imageBmp) {
                            if (mImageView != null) {
                                mImageView.setImageBitmap(imageBmp);
                                mImageBitmap = imageBmp;
                                mImageFileName = mCurrentUser.getImageName();
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

        Button btnSave = (Button) view.findViewById(R.id.fragment_edit_user_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageBitmap != null && mImageFileName != mCurrentUser.getImageName()) {
                    if (mCurrentUser.getImageName() != null) {
                        Model.getInstance().deleteImage(mCurrentUser.getImageName());
                    }

                    mCurrentUser.setImageName(mImageFileName);

                    Model.getInstance().saveImage(mImageBitmap, mImageFileName);
                    Model.getInstance().editUser(mCurrentUser);
                }

                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onSave();

                MyAlertDialog dialog = MyAlertDialog.newInstance("Changes successfully saved");
                dialog.setDelegate(new MyAlertDialog.Delegate() {
                    @Override
                    public void onOk() {
                        Log.d("EditUserFragment", "OK pressed");
                    }

                });

                dialog.show(getFragmentManager(), getResources().getString(R.string.title_fragment_edit_user));
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.fragment_edit_user_btn_cancel);
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

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            mImageFileName = mCurrentUser.getName() + timeStamp + ".jpg";
        }
    }

    public interface OnFragmentInteractionListener {
        void onSave();

        void onCancel();
    }
}