package dduwcom.mobile.finalproject.ma01_20190937;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ViewBookActivity extends AppCompatActivity {

    final int UPDATE_CODE = 200;

    Long bookId;
    BookDTO book;
    BookDBManager bookDBManager;

    ImageView view_image;
    TextView view_state;
    TextView view_title;
    TextView view_author;
    TextView view_publisher;
    TextView view_pubDate;
    TextView view_readDate;
    TextView view_library;
    TextView view_bookStore;
    TextView view_contents;
    ImageFileManager imageFileManager = null;
    Intent intent;
    AlertDialog.Builder builder;
    SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        bookDBManager = new BookDBManager(this);

        view_image = findViewById(R.id.view_image);
        view_state = findViewById(R.id.view_state);
        view_title = findViewById(R.id.view_title);
        view_author = findViewById(R.id.view_author);
        view_publisher = findViewById(R.id.view_publisher);
        view_pubDate = findViewById(R.id.view_pubDate);
        view_readDate = findViewById(R.id.view_readDate);
        view_library = findViewById(R.id.view_library);
        view_bookStore = findViewById(R.id.view_bookStore);
        view_contents = findViewById(R.id.view_contents);

        bookId = (Long) getIntent().getLongExtra("bookId", -1);
        book = bookDBManager.getBookbyId(bookId);
        setTextView(book);
    }

    private void setTextView(BookDTO book) {
        imageFileManager = new ImageFileManager(this);
        Bitmap bitmap = imageFileManager.getBitmapFromExternal(book.getImageFileName());
        if (bitmap != null) {
            view_image.setImageBitmap(bitmap);
        }

        int state = book.getState();
        String stateStr = null;
        if (state == 0) {
            stateStr = "관심 도서";
        } else if (state == 1) {
            stateStr = "읽는 중";
        } else if (state == 2) {
            stateStr = "읽기 완료";
        }
        view_state.setText(stateStr);
        view_title.setText(book.getTitle());
        view_author.setText(book.getAuthor());
        view_publisher.setText(book.getPublisher());
        view_pubDate.setText(book.getPubDate());
        view_readDate.setText(book.getReadDate());
        view_library.setText(book.getLibrary());
        view_bookStore.setText(book.getBookStore());
        view_contents.setText(book.getContent());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewMenu_edit:
                intent = new Intent(ViewBookActivity.this, EditBookActivity.class);
                intent.putExtra("bookId", bookId);
                startActivityForResult(intent, UPDATE_CODE); // onresume이 자동 호출될것임
                break;

            case R.id.viewMenu_delete:
                builder = new AlertDialog.Builder(ViewBookActivity.this);
                builder.setTitle("도서 정보 삭제")
                        .setMessage("'"+bookDBManager.getTitlebyId(bookId)+"'을(를) 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean result = bookDBManager.removeBook(bookId);
                                if (result) {
                                    finish();
                                    Toast.makeText(ViewBookActivity.this, "삭제 완료",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ViewBookActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();
                break;

            case R.id.viewMenu_share:
                Intent Sharing_intent = new Intent(Intent.ACTION_SEND);

                String Test_Message = "";
                Sharing_intent.setType("text/plain");

                spf = getSharedPreferences("share", 0);
                boolean[] options = new boolean[6];
                options[0] = spf.getBoolean("id0", true);
                options[1] = spf.getBoolean("id1", true);
                options[2] = spf.getBoolean("id2", true);
                options[3] = spf.getBoolean("id3", true);
                options[4] = spf.getBoolean("id4", true);
                options[5] = spf.getBoolean("id5", true);

                spf = getSharedPreferences("addText", 0);
                String head = spf.getString("head", "");
                String tail = spf.getString("tail", "");

                if (!head.equals("")) {
                    Test_Message += head+"\n";
                }
                if (options[0]) {
                    Test_Message += "도서명 : "+ view_title.getText().toString() +"\n";
                }
                if (options[1]) {
                    Test_Message += "저자 : "+view_author.getText().toString() +"\n";
                }
                if (options[2]) {
                    Test_Message += "출판사 : "+view_publisher.getText().toString() +"\n";
                }
                if (options[3]) {
                    Test_Message += "출판일 : "+view_pubDate.getText().toString() +"\n";
                }
                if (options[4]) {
                    Test_Message += "읽은 날짜 : "+view_readDate.getText().toString() +"\n";
                }
                if (options[5]) {
                    Test_Message += "감상평 : "+view_contents.getText().toString() +"\n";
                }
                if (!tail.equals("")) {
                    Test_Message += tail+"\n";
                }

                Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message);

                Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
                startActivity(Sharing);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_CODE) {    // onresume이 자동 호출될것임
            switch (resultCode) {
                case RESULT_OK:
                    book = (BookDTO) data.getSerializableExtra("editDTO");
                    setTextView(book);
                    Toast.makeText(this, "도서 수정 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "도서 수정 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
