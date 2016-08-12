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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eliavmenachi.simplelist.model.Group;
import com.example.eliavmenachi.simplelist.model.Model;

public class GroupDetailsFragment extends Fragment {

    private static final String ARG_GROUP_ID = "GroupId";

    private String mGroupId;
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private OnFragmentInteractionListener mListener;

    public GroupDetailsFragment() {

    }

    public static GroupDetailsFragment newInstance(String groupId) {
        GroupDetailsFragment fragment = new GroupDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GROUP_ID, groupId);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_group_details, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_details, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Group");
        mListener = (OnFragmentInteractionListener) getActivity();

        final TextView tvName = (TextView) view.findViewById(R.id.fragment_group_details_tv_name);
        mImageView = (ImageView) view.findViewById(R.id.fragment_group_details_iv_image);

        final ProgressBar progressBar = (android.widget.ProgressBar) view.findViewById(R.id.fragment_group_details_pb);
        final ProgressBar imageProgressBar = (ProgressBar) view.findViewById(R.id.fragment_group_details_pb_image);
        progressBar.setVisibility(View.VISIBLE);

        Model.getInstance().getGroupByIdAsync(mGroupId, new Model.GetGroupListener() {

            @Override
            public void onResult(Group group) {
                tvName.setText(group.getName());
                if (group.getImageName() != null) {
                    imageProgressBar.setVisibility(View.VISIBLE);
                    Model.getInstance().loadImageAsync(group.getImageName(), new Model.LoadImageListener() {
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
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onEditGroupItemSelected(String groupId);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_edit_group:
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onEditGroupItemSelected(mGroupId);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
