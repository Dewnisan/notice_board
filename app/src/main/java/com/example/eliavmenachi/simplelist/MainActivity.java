package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.Student;


import java.util.LinkedList;
import java.util.List;


public class MainActivity extends Activity {
    ListView listView;
    List<Student> data = new LinkedList<Student>();
    CustomAdapter adapter;
    ProgressBar progressBar;
    static final int NEW_STUDENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);

        listView = (ListView) findViewById(R.id.listView);

        adapter = new CustomAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"item click " + position,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),StudentDetailsActivity.class);
                intent.putExtra("id",data.get(position).getId());
                startActivity(intent);
            }
        });
        loadStudentsData();
    }

    void loadStudentsData(){
        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getAllStudentsAsynch(new Model.GetStudentsListener() {
            @Override
            public void onResult(List<Student> students) {
                progressBar.setVisibility(View.GONE);
                data = students;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check which request we're responding to
        if (requestCode == NEW_STUDENT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                loadStudentsData();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_add:
                Intent intent = new Intent(getApplicationContext(),NewStudentActivity.class);
                startActivityForResult(intent,NEW_STUDENT_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row_layout,null);

                CheckBox cb1 = (CheckBox) convertView.findViewById(R.id.checkBox);
                cb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("LISTAPP", "my tag is: " + v.getTag());
                        Student st = data.get((Integer) v.getTag());
                        st.setChecked(!st.isChecked());
                    }
                });
            }

            final ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
            TextView name = (TextView) convertView.findViewById(R.id.nameTextView);
            TextView id = (TextView) convertView.findViewById(R.id.idTextView);
            final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            cb.setTag(new Integer(position));
            convertView.setTag(position);

            Student st = data.get(position);
            name.setText(st.getFname() + " " + st.getLname());
            id.setText(st.getId());
            cb.setChecked(st.isChecked());

            if (st.getImageName() != null){
                Log.d("TAG","list gets image " + st.getImageName());
                final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.rowImageProgressBar);
                progress.setVisibility(View.VISIBLE);
                Model.getInstance().loadImage(st.getImageName(),new Model.LoadImageListener() {
                    @Override
                    public void onResult(Bitmap imageBmp) {
                        if (imageBmp != null && ((Integer)cb.getTag() == position)) {
                            image.setImageBitmap(imageBmp);
                            progress.setVisibility(View.GONE);
                        }
                    }
                });
            }
            return convertView;
        }
    }
}
