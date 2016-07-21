package com.example.eliavmenachi.simplelist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Tal on 20/04/2016.
 */
public class MyAlertDialog extends DialogFragment {
    public static String ARG_MESSAGE = "MESSAGE";

    private Delegate delegate;
    private String mMessage;

    public static final MyAlertDialog newInstance(String message) {
        MyAlertDialog fragment = new MyAlertDialog();

        Bundle args = new Bundle(1);
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);

        return fragment;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (getArguments() != null) {
            mMessage = getArguments().getString(ARG_MESSAGE);
        } else {
            mMessage = "Success";
        }

        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(mMessage);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delegate.onOk();
            }
        });

        return builder.create();
    }

    public interface Delegate {
        public void onOk();
    }
}
