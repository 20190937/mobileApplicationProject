package dduwcom.mobile.finalproject.ma01_20190937;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "readDB";
    final int ADD_CODE = 100;
    final int VIEW_CODE = 200;
    final int SHARE_OPTION_CODE = 600;
    final int STATE_SEARCH_CODE = 700;
    final int SEARCH_CODE = 800;
    int updateResultCode = RESULT_OK;

    FloatingActionButton fab;
    Intent intent;
    BookDBManager bookDBManager;
    BookCursorAdapter adapter;
    Cursor cursor;
    BookDBHelper bookDBHelper;
    ListView lvBooks = null;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookDBManager = new BookDBManager(this);
        lvBooks = (ListView)findViewById(R.id.lvBooks);
        bookDBHelper = new BookDBHelper(this);
        adapter = new BookCursorAdapter(this, R.layout.custom_book_layout, null);
        lvBooks.setAdapter(adapter);


        fab = findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, AddBookActivity.class);
                startActivityForResult(intent, ADD_CODE); // onresume이 자동 호출될것
            }
        });

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(MainActivity.this, ViewBookActivity.class);
                intent.putExtra("bookId", id);
                startActivityForResult(intent, VIEW_CODE);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenu_stateSearch:
                intent = new Intent(MainActivity.this, StateSearchActivity.class);
                startActivityForResult(intent, STATE_SEARCH_CODE);
                break;
            case R.id.mainMenu_search:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, SEARCH_CODE);
                break;
            case R.id.mainMenu_shareOption:
                intent = new Intent(MainActivity.this, ShareOptionActivity.class);
                startActivityForResult(intent, SHARE_OPTION_CODE);
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (updateResultCode != RESULT_CANCELED) {
            readDB();
        }
    }

    private void readDB() {
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from " + BookDBHelper.TABLE_NAME, null);

        adapter.changeCursor(cursor);
        bookDBHelper.close();
        Log.d(TAG, "read success");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CODE) {  // AddActivity 호출 후 결과 확인
            switch (resultCode) {
                case RESULT_OK:
                    updateResultCode = RESULT_OK;
                    Toast.makeText(this, "도서 추가 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    updateResultCode = RESULT_CANCELED;
                    Toast.makeText(this, "도서 추가 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else if (requestCode == SHARE_OPTION_CODE) {    // onresume이 자동 호출될것임
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "공유 옵션 수정 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "공유 옵션 수정 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}