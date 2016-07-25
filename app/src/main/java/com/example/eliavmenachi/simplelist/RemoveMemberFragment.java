package com.example.eliavmenachi.simplelist;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.User;

import java.util.LinkedList;
import java.util.List;


public class RemoveMemberFragment extends Fragment {
    private static final String ARG_GROUP_ID = "GROUP_ID";

    private List<User> mData = new LinkedList<User>();

    private String mGroupId;

    private OnFragmentInteractionListener mListener;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private MyAdapter mAdapter;

    public RemoveMemberFragment() {
        // Required empty public constructor
    }

    public static RemoveMemberFragment newInstance(String groupId) {
        RemoveMemberFragment fragment = new RemoveMemberFragment();
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
        View view = inflater.inflate(R.layout.fragment_remove_member, container, false);

        getActivity().setTitle(R.string.title_fragment_remove_member);
        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_groups_pb);

        loadUsersData();

        mListView = (ListView) view.findViewById(R.id.fragment_group_list_lv);

        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void loadUsersData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getAllGroupUsersAsync(mGroupId, new Model.GetUsersListener() {
            @Override
            public void onResult(List<User> users) {
                for (User user : users) {
                    String userId = Model.getInstance().getUserId();
                    if (user.getId().equals(userId)) {
                        users.remove(user);
                        break;
                    }
                }

                mProgressBar.setVisibility(View.GONE);
                mData = users;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {

            }
        });
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
                convertView = inflater.inflate(R.layout.row_remove_member_list, null);
            }

            final TextView tvName = (TextView) convertView.findViewById(R.id.row_remove_member_list_tv_name);
            final ImageView ivImage = (ImageView) convertView.findViewById(R.id.row_remove_member_list_iv_image);
            Button btnRemove = (Button) convertView.findViewById(R.id.row_remove_member_list_btn_remove);

            tvName.setTag(new Integer(position));
            convertView.setTag(position);

            final User user = mData.get(position);

            Model.getInstance().getUserByIdAsync(user.getId(), new Model.GetUserListener() {
                @Override
                public void onResult(User user) {
                    tvName.setText(user.getName());
                }

                @Override
                public void onCancel() {

                }
            });

            if (user.getImageName() != null) {
                final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.row_post_list_pb_image);
                progress.setVisibility(View.VISIBLE);

                Model.getInstance().loadImageAsync(user.getImageName(), new Model.LoadImageListener() {
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

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.getInstance().removeMemberFromGroup(user.getId(), mGroupId);
                    loadUsersData();
                    mAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
}
