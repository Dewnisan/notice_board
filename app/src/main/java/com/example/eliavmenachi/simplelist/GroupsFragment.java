package com.example.eliavmenachi.simplelist;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
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

import com.example.eliavmenachi.simplelist.model.Group;
import com.example.eliavmenachi.simplelist.model.Model;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment {
    private ListView mListView;
    private List<Group> mData = new LinkedList<Group>();
    private MyAdapter mAdapter;
    ProgressBar mProgressBar;

    private OnFragmentInteractionListener mListener;

    public GroupsFragment() {
        // Required empty public constructor
    }

    public static GroupsFragment newInstance() {
        GroupsFragment fragment = new GroupsFragment();
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
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        getActivity().setTitle(R.string.title_fragment_groups);

        setHasOptionsMenu(true);

        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_groups_pb);

        loadGroupsData();

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

    void loadGroupsData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getAllUserGroupsAsync(new Model.GetGroupsListener() {
            @Override
            public void onResult(List<Group> groups) {
                mProgressBar.setVisibility(View.GONE);
                mData = groups;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onCreateGroupItemSelected();

        void onProfileItemSelected();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_groups, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_user_details:
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onProfileItemSelected();
                return true;

            case R.id.action_create_group:
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onCreateGroupItemSelected();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
                convertView = inflater.inflate(R.layout.activity_group_list_row, null);
            }

            final TextView name = (TextView) convertView.findViewById(R.id.activity_group_list_row_tv_name);
            final ImageView image = (ImageView) convertView.findViewById(R.id.activity_group_list_row_img);
            name.setTag(new Integer(position));
            convertView.setTag(position);

            Group group = mData.get(position);

            name.setText(group.getName());

            if (group.getImageName() != null) {
                final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.activity_group_list_row_pb);
                progress.setVisibility(View.VISIBLE);

                Model.getInstance().loadImage(group.getImageName(), new Model.LoadImageListener() {
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
