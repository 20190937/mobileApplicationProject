package dduwcom.mobile.finalproject.ma01_20190937;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


public class StateSearchActivity extends AppCompatActivity {
    final static String TAG = "readDB";

    final int VIEW_CODE = 200;
    Intent intent;
    BookDBManager bookDBManager;
    BookCursorAdapter adapter;
    Cursor cursor;
    BookDBHelper bookDBHelper;
    ListView lvBooks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_search);

        bookDBManager = new BookDBManager(this);
        lvBooks = (ListView)findViewById(R.id.lvStateBooks);
        bookDBHelper = new BookDBHelper(this);
        adapter = new BookCursorAdapter(this, R.layout.custom_book_layout, null);
        lvBooks.setAdapter(adapter);

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(StateSearchActivity.this, ViewBookActivity.class);
                intent.putExtra("bookId", id);
                startActivityForResult(intent, VIEW_CODE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        readDB();
    }
    private void readDB() {
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from " + BookDBHelper.TABLE_NAME, null);

        adapter.changeCursor(cursor);
        bookDBHelper.close();
        Log.d(TAG, "read success");
    }

    public void stateOnClick(View v) {
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();

        switch (v.getId()) {
            case R.id.state_btn_done:
                cursor = db.rawQuery("Select * from " + BookDBHelper.TABLE_NAME + " where "+BookDBHelper.COL_STATE+"=? ", new String[]{"2"});
                adapter.changeCursor(cursor);
                break;
            case R.id.state_btn_reading:
                cursor = db.rawQuery("Select * from " + BookDBHelper.TABLE_NAME + " where "+BookDBHelper.COL_STATE+"=? ", new String[]{"1"});
                adapter.changeCursor(cursor);
                break;
            case R.id.state_btn_will:
                cursor = db.rawQuery("Select * from " + BookDBHelper.TABLE_NAME + " where "+BookDBHelper.COL_STATE+"=? ", new String[]{"0"});
                adapter.changeCursor(cursor);
                break;
        }
        bookDBHelper.close();
        Log.d(TAG, "read success");
    }
}
