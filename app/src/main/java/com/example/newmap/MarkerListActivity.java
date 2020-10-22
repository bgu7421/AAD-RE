package com.example.newmap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MarkerListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_INSERT = 1000;

    private MemoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MarkerListActivity.this, MarkerAddActivity.class), REQUEST_CODE_INSERT);
            }
        });


        ListView listView = findViewById(R.id.memo_list);


        Cursor cursor = getMemoCursor();

        mAdapter = new MemoAdapter(this, cursor);

        listView.setAdapter(mAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MarkerListActivity.this, MarkerAddActivity.class);


                Cursor cursor = (Cursor) mAdapter.getItem(position);


                String title = cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_TITLE));
                String lat = cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_LAT));
                String lng = cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_LNG));


                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);

                startActivityForResult(intent, REQUEST_CODE_INSERT);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final long deleteId = id;

                AlertDialog.Builder builder = new AlertDialog.Builder(MarkerListActivity.this);

                builder.setTitle("메모 삭제");
                builder.setMessage("메모를 삭제하시겠습니까?");

                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLiteDatabase db = MemoDbHelper.getInstance(MarkerListActivity.this).getWritableDatabase();

                        int deletedCount = db.delete(MemoContract.MemoEntry.TABLE_NAME, MemoContract.MemoEntry._ID + " = " + deleteId, null);


                        if(deletedCount == 0){
                            Toast.makeText(MarkerListActivity.this, "삭제에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            mAdapter.swapCursor(getMemoCursor());
                            Toast.makeText(MarkerListActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
                return true;
            }
        });
    }



    private  Cursor getMemoCursor(){

        MemoDbHelper dbHelper = MemoDbHelper.getInstance(this);


        return dbHelper.getReadableDatabase().query(MemoContract.MemoEntry.TABLE_NAME, null, null, null,null,null,null,null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_INSERT && resultCode == RESULT_OK){

            mAdapter.swapCursor(getMemoCursor());
        }
    }


    private static class MemoAdapter extends CursorAdapter{

        public MemoAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

            return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView titleText = view.findViewById(android.R.id.text1);

            titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_TITLE)));
        }
    }
}
