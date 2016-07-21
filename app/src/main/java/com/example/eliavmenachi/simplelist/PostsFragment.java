package com.example.eliavmenachi.simplelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.Post;

import java.util.LinkedList;
import java.util.List;

public class PostsFragment extends Fragment {
    public static String ARG_GROUP_ID = "GROUP_ID";

    private String mGroupId;
    private ListView mListView;
    private List<Post> mData = new LinkedList<Post>();
    private MyAdapter mAdapter;
    ProgressBar mProgressBar;

    private OnFragmentInteractionListener mListener;

    public PostsFragment() {
        // Required empty public constructor
    }

    public static PostsFragment newInstance(String groupId) {
        PostsFragment fragment = new PostsFragment();
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
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.activity_post_list_row, null);
            }

            final TextView name = (TextView) convertView.findViewById(R.id.activity_post_list_row_tv_name);
            final ImageView image = (ImageView) convertView.findViewById(R.id.activity_post_list_row_img);
            name.setTag(new Integer(position));
            convertView.setTag(position);

            Post post = mData.get(position);

            name.setText(post.getOwner());

            if (post.getImageName() != null) {
                final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.activity_post_list_row_pb);
                progress.setVisibility(View.VISIBLE);

                Model.getInstance().loadImage(post.getImageName(), new Model.LoadImageListener() {
                    @Override
                    public void onResult(Bitmap imageBmp) {
                        if ((Integer) name.getTag() == position) {
                            progress.setVisibility(View.GONE);

                            if (imageBmp != null) {
                                image.setImageBitmap(imageBmp);
                            }
                        }
                    }
                });
            }

            return convertView;
        }
    }
}
