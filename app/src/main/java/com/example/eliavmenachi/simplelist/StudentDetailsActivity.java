package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class StudentDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        final TextView name = (TextView) findViewById(R.id.sdName);
        final TextView idtv = (TextView) findViewById(R.id.sdId);
        final TextView phone = (TextView) findViewById(R.id.sdPhone);
        final TextView address = (TextView) findViewById(R.id.sdAddress);
        final ImageView image = (ImageView) findViewById(R.id.sdImage);
        final CheckBox check = (CheckBox) findViewById(R.id.sdChecked);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.sdProgressBar);
        final ProgressBar imageProgressBar = (ProgressBar) findViewById(R.id.sdImageProgressBar);
        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");


        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getStudentById(id,new Model.GetStudent() {
            @Override
            public void onResult(Student student) {
                name.setText(student.getFname());
                idtv.setText(student.getId());
                address.setText(student.getAddress());
                phone.setText(student.getPhone());
                check.setChecked(student.isChecked());
                if(student.getImageName() != null){
                    imageProgressBar.setVisibility(View.VISIBLE);
                    Model.getInstance().loadImage(student.getImageName(),new Model.LoadImageListener() {
                        @Override
                        public void onResult(Bitmap imageBmp) {
                            if (image != null) {
                                image.setImageBitmap(imageBmp);
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
