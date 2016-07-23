package com.example.eliavmenachi.simplelist;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.Post;


public class PostDetailsFragment extends Fragment {
    private static final String ARG_POST_ID = "POST_ID";

    private String mPostId;
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    public static PostDetailsFragment newInstance(String postId) {
        PostDetailsFragment fragment = new PostDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mPostId = getArguments().getString(ARG_POST_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        getActivity().setTitle(R.string.title_fragment_post_details);

        final TextView tvMessage = (TextView) view.findViewById(R.id.fragment_post_details_tv_message);
        mImageView = (ImageView) view.findViewById(R.id.fragment_post_details_iv_image);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.fragment_post_details_pb);
        final ProgressBar imageProgressBar = (ProgressBar) view.findViewById(R.id.fragment_post_details_pb_image);

        progressBar.setVisibility(View.VISIBLE);

        Model.getInstance().getPostByIdAsync(mPostId, new Model.GetPostListener() {
            @Override
            public void onResult(Post post) {
                tvMessage.setText(post.getMessage());
                if (post.getImageName() != null) {
                    imageProgressBar.setVisibility(View.VISIBLE);
                    Model.getInstance().loadImageAsync(post.getImageName(), new Model.LoadImageListener() {
                        @Override
                        public void onResult(Bitmap imageBmp) {
                            if (mImageView != null) {
                                mImageView.setImageBitmap(imageBmp);
                                mImageBitmap = imageBmp;
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

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
