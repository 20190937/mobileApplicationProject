package dduwcom.mobile.finalproject.ma01_20190937;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    final static String TAG = "readDB";

    final int VIEW_CODE = 200;
    Intent intent;
    BookDBManager bookDBManager;
    BookCursorAdapter adapter;
    Cursor cursor;
    BookDBHelper bookDBHelper;
    ListView lvBooks = null;
    EditText search_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bookDBManager = new BookDBManager(this);
        lvBooks = (ListView)findViewById(R.id.lvSearchBooks);
        bookDBHelper = new BookDBHelper(this);
        adapter = new BookCursorAdapter(this, R.layout.custom_book_layout, null);
        lvBooks.setAdapter(adapter);
        search_et = (EditText)findViewById(R.id.search_et);

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(SearchActivity.this, ViewBookActivity.class);
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

    public void searchOnClick(View v) {
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        String str = search_et.getText().toString();

        switch (v.getId()) {
            case R.id.search_btn_title:
                cursor = db.rawQuery("Select * from " + BookDBHelper.TABLE_NAME +
                        " where "+BookDBHelper.COL_TITLE+" LIKE ? ", new String[]{"%"+ str+ "%"});
                adapter.changeCursor(cursor);
                break;
            case R.id.search_btn_author:
                cursor = db.rawQuery("Select * from " + BookDBHelper.TABLE_NAME +
                        " where "+BookDBHelper.COL_AUTHOR+" LIKE ? ", new String[]{"%"+ str+ "%"});
                adapter.changeCursor(cursor);
                break;
            case R.id.search_btn_publisher:
                cursor = db.rawQuery("Select * from " + BookDBHelper.TABLE_NAME +
                        " where "+BookDBHelper.COL_PUBLISHER+" LIKE ? ", new String[]{"%"+ str+ "%"});
                adapter.changeCursor(cursor);
                break;
        }
        bookDBHelper.close();
        Log.d(TAG, "read success");
    }
}
