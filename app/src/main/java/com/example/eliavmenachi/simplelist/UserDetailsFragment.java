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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.User;


public class UserDetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Bitmap mImageBitmap;
    private ImageView mImageView;

    public UserDetailsFragment() {
        // Required empty public constructor
    }

    public static UserDetailsFragment newInstance() {
        UserDetailsFragment fragment = new UserDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        getActivity().setTitle(R.string.title_fragment_user_details);

        setHasOptionsMenu(true);

        final TextView tvName = (TextView) view.findViewById(R.id.fragment_user_details_tv_name);
        mImageView = (ImageView) view.findViewById(R.id.fragment_user_details_img);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.fragment_user_details_pb);
        final ProgressBar imageProgressBar = (ProgressBar) view.findViewById(R.id.fragment_user_details_image_pb);

        progressBar.setVisibility(View.VISIBLE);

        String userId = Model.getInstance().getUserId();
        Model.getInstance().getUserById(userId, new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                tvName.setText(user.getName());
                if (user.getImageName() != null) {
                    imageProgressBar.setVisibility(View.VISIBLE);
                    Model.getInstance().loadImage(user.getImageName(), new Model.LoadImageListener() {
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
        void onEditUserItemSelected();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_edit_user:
                mListener = (OnFragmentInteractionListener) getActivity();
                mListener.onEditUserItemSelected();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
