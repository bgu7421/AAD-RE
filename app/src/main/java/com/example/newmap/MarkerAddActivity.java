package com.example.newmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MarkerAddActivity extends AppCompatActivity {

    private EditText mTitleEditText;
    private EditText mLatEditText;
    private EditText mLngEditText;
    private long mMemoId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_add);

        mTitleEditText = findViewById(R.id.title_edit);
        mLatEditText = findViewById(R.id.lat_edit);
        mLngEditText = findViewById(R.id.lng_edit);


        Intent intent = getIntent();
        if(intent != null){


            mMemoId = intent.getLongExtra("id", -1);
            String title = intent.getStringExtra("title");
            String lat = intent.getStringExtra("lat");
            String lng = intent.getStringExtra("lng");

            mTitleEditText.setText(title);
            mLatEditText.setText(lat);
            mLngEditText.setText(lng);
        }
    }

    public void addMarker(View view) {

        String title = mTitleEditText.getText().toString();
        String lat = mLatEditText.getText().toString();
        String lng = mLngEditText.getText().toString();


        ContentValues contentValues = new ContentValues();
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_LAT, lat);
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_LNG, lng);

        SQLiteDatabase db = MemoDbHelper.getInstance(this).getWritableDatabase();

        if(mMemoId == -1){

            long newRowId = db.insert(MemoContract.MemoEntry.TABLE_NAME,null, contentValues);


            if(newRowId == -1){
                Toast.makeText(this, "Save Error!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Location has been added", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);

                startActivity(new Intent(this, MarkerListActivity.class));
            }
        }
        else{

            int count = db.update(MemoContract.MemoEntry.TABLE_NAME, contentValues, MemoContract.MemoEntry._ID + "=" + mMemoId, null);

            if(count == 0) {
                Toast.makeText(this, "Editing Error!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Location has been edited", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.marker_add_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.marker_add_menu_1:
                Toast.makeText(this, "Every Location has been deleted.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.marker_add_menu_2:
                Toast.makeText(this, "Second Menu", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MarkerListActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
