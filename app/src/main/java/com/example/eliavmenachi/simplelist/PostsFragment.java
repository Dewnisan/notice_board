package com.example.eliavmenachi.simplelist;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.Post;
import com.example.eliavmenachi.simplelist.model.User;

import java.util.Collections;
import java.util.Date;
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
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        getActivity().setTitle(R.string.title_fragment_posts);

        setHasOptionsMenu(true);
        mListener = (OnFragmentInteractionListener) getActivity();

        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_posts_pb);

        loadPostsData();

        mListView = (ListView) view.findViewById(R.id.fragment_posts_list_lv);

        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String postId = mData.get(position).getId();
                mListener.onPostSelected(postId);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_posts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_create_post:
                mListener.onCreatePostItemSelected(mGroupId);
                return true;
            case R.id.action_add_member:
                mListener.onAddMemberItemSelected(mGroupId);
                return true;
            case R.id.action_remove_member:
                mListener.onRemoveMemberItemSelected(mGroupId);
                return true;
            case R.id.action_exit_group:
                exitGroup();
                mListener.onExitGroupItemSelected();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void exitGroup() {
        String userId = Model.getInstance().getUserId();
        Model.getInstance().removeMemberFromGroup(userId, mGroupId);
    }

    private void loadPostsData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getAllGroupPostsAsync(mGroupId, new Model.GetPostsListener() {
            @Override
            public void onResult(List<Post> posts) {
                mProgressBar.setVisibility(View.GONE);

                if (posts != null) {
                    Collections.sort(posts);
                    mData = posts;
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onCreatePostItemSelected(String id);

        void onPostSelected(String postId);

        void onAddMemberItemSelected(String groupId);

        void onRemoveMemberItemSelected(String groupId);

        void onExitGroupItemSelected();
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
                convertView = inflater.inflate(R.layout.row_post_list, null);
            }

            final TextView tvName = (TextView) convertView.findViewById(R.id.row_post_list_tv_name);
            final TextView tvMessage = (TextView) convertView.findViewById(R.id.row_post_list_tv_message);
            TextView tvTimestamp = (TextView) convertView.findViewById(R.id.row_post_list_tv_timestamp);
            final ImageView ivImage = (ImageView) convertView.findViewById(R.id.row_post_list_iv_image);

            tvName.setTag(new Integer(position));
            convertView.setTag(position);

            Post post = mData.get(position);

            Date date = new Date(post.getTimestamp());
            tvTimestamp.setText(date.toString());

            Model.getInstance().getUserByIdAsync(post.getOwner(), new Model.GetUserListener() {
                @Override
                public void onResult(User user) {
                    tvName.setText(user.getName());
                }

                @Override
                public void onCancel() {

                }
            });

            tvMessage.setText(post.getMessage());

            if (post.getImageName() != null) {
                final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.row_post_list_pb_image);
                progress.setVisibility(View.VISIBLE);

                Model.getInstance().loadImageAsync(post.getImageName(), new Model.LoadImageListener() {
                    @Override
                    public void onResult(Bitmap imageBmp) {
                        if ((Integer) tvName.getTag() == position) {
                            progress.setVisibility(View.GONE);

                            if (imageBmp != null) {
                                ivImage.setImageBitmap(imageBmp);
                            }
                        }
                    }
                });
            }

            return convertView;
        }
    }
}
