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

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.Post;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CreatePostFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String ARG_GROUP_ID = "GROUP_ID";

    private OnFragmentInteractionListener mListener;

    private String mGroupId;
    private ImageView mImageView;
    private Bitmap mImageBitmap;
    private String mImageFileName;


    public CreatePostFragment() {
        // Required empty public constructor
    }

    public static CreatePostFragment newInstance(String groupId) {
        CreatePostFragment fragment = new CreatePostFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);
        getActivity().setTitle(R.string.title_fragment_create_post);

        mImageView = (ImageView) view.findViewById(R.id.fragment_create_post_iv_image);

        final EditText etMessage = ((EditText) view.findViewById(R.id.fragment_create_post_et_message));

        Button btnSave = (Button) view.findViewById(R.id.fragment_create_post_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                String message = etMessage.getText().toString();
                String owner = Model.getInstance().getUserId();

                if (mImageBitmap != null) {
                    mImageFileName = mGroupId + timeStamp + ".jpg";
                }

                Post post = new Post("id", owner, mGroupId, message, mImageFileName);
                Model.getInstance().addPost(post);

                if (mImageBitmap != null) {
                    Model.getInstance().saveImage(mImageBitmap, mImageFileName);
                }

                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onSave();

                MyAlertDialog dialog = MyAlertDialog.newInstance("Post creation succeeded");
                dialog.setDelegate(new MyAlertDialog.Delegate() {
                    @Override
                    public void onOk() {
                        Log.d("CreatePostFragment", "OK pressed");
                    }

                });

                dialog.show(getFragmentManager(), getResources().getString(R.string.title_fragment_create_post));
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.fragment_create_post_btn_cancel);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(mImageBitmap);
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
