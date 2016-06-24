package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.Student;


import java.text.SimpleDateFormat;
import java.util.Date;


public class NewStudentActivity extends Activity {
    ImageView imageView;
    String imageFileName = null;
    Bitmap imageBitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student);


        final EditText idEt = (EditText) findViewById(R.id.nsIdEditText);
        final EditText fnameEt = (EditText) findViewById(R.id.nsFNameEditText);
        final EditText lnameEt = (EditText) findViewById(R.id.nsLNameEditText);
        final EditText addressEt = (EditText) findViewById(R.id.nsAddressEditText);
        final EditText phoneEt = (EditText) findViewById(R.id.nsPhoneEditText);
        final CheckBox check = (CheckBox) findViewById(R.id.nsCheckBox);
        imageView = (ImageView) findViewById(R.id.nsImageView);

        Button save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imageFileName = idEt.getText().toString() + timeStamp + ".jpg";

                Model.getInstance().add(new Student(idEt.getText().toString(),
                        fnameEt.getText().toString(),
                        lnameEt.getText().toString(),
                        phoneEt.getText().toString(),
                        addressEt.getText().toString(),
                        imageFileName,
                        check.isChecked()));

                Model.getInstance().saveImage(imageBitmap,imageFileName);

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takingPicture();
            }
        });
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void takingPicture(){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_student, menu);
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
