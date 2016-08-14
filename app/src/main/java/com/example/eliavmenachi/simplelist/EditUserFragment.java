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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditUserFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

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
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        mListener = (OnFragmentInteractionListener) getActivity();

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.fragment_edit_user_pb);
        final ProgressBar imageProgressBar = (ProgressBar) view.findViewById(R.id.fragment_edit_user_pb_image);

        final TextView tvName = (TextView) view.findViewById(R.id.fragment_edit_user_tv_name);
        mImageView = (ImageView) view.findViewById(R.id.fragment_edit_user_iv_image);

        String userId = Model.getInstance().getUserId();
        Model.getInstance().getUserByIdAsync(userId, new Model.GetUserListener() {
            @Override
            public void onResult(final User user) {
                mCurrentUser = user;

                tvName.setText(mCurrentUser.getName());
                if (mCurrentUser.getImageName() != null) {
                    imageProgressBar.setVisibility(View.VISIBLE);
                    Model.getInstance().loadImageAsync(mCurrentUser.getImageName(), new Model.LoadImageListener() {
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

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public interface OnFragmentInteractionListener {
        void onSave();

        void onCancel();
    }
}
