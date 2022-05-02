package dduwcom.mobile.finalproject.ma01_20190937;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditBookActivity extends AppCompatActivity {
    public static final String TAG = "EditBookActivity";

    final int NAVER_BOOK_CODE = 400;
    final int LIBRARY_CODE = 500;
    final int BOOKSTORE_CODE = 600;

    Calendar myCalendar = Calendar.getInstance();
    private int et_date_type;
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    Intent intent;
    ImageView edit_image;
    String edit_image_src;
    EditText edit_et_title;
    EditText edit_et_author;
    EditText edit_et_publisher;
    EditText edit_et_pubDate;
    EditText edit_et_readDate;
    EditText edit_et_library;
    EditText edit_et_bookStore;
    EditText edit_et_contents;
    RadioGroup radioGroup;
    RadioButton edit_rb_done;
    RadioButton edit_rb_reading;
    RadioButton edit_rb_will;

    int stateType;

    ImageFileManager imageFileManager = null;
    BookDBManager bookDBManager = null;
    Long bookId;
    BookDTO book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        imageFileManager = new ImageFileManager(this);
        bookDBManager = new BookDBManager(this);

        //연결
        edit_image = (ImageView) findViewById(R.id.edit_image);
        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(EditBookActivity.this, NaverBookActivity.class);
                startActivityForResult(intent, NAVER_BOOK_CODE); // onresume이 자동 호출될것
            }
        });

        edit_et_title = (EditText) findViewById(R.id.edit_et_title);
        edit_et_author = (EditText) findViewById(R.id.edit_et_author);
        edit_et_publisher = (EditText) findViewById(R.id.edit_et_publisher);
        edit_et_library = (EditText) findViewById(R.id.edit_et_library);
        edit_et_bookStore = (EditText) findViewById(R.id.edit_et_bookStore);
        edit_et_contents = (EditText) findViewById(R.id.edit_et_contents);

        edit_et_pubDate = (EditText) findViewById(R.id.edit_et_pubDate);
        edit_et_pubDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_date_type = 1;
                new DatePickerDialog(EditBookActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edit_et_readDate = (EditText) findViewById(R.id.edit_et_readDate);
        edit_et_readDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_date_type = 2;
                new DatePickerDialog(EditBookActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //값 삽입
        bookId = (Long) getIntent().getLongExtra("bookId", -1);
        book = bookDBManager.getBookbyId(bookId);

        Bitmap bitmap = imageFileManager.getBitmapFromExternal(book.getImageFileName());
        if (bitmap != null) {
            edit_image.setImageBitmap(bitmap);
        }
        edit_et_title.setText(book.getTitle());
        edit_et_author.setText(book.getAuthor());
        edit_et_publisher.setText(book.getPublisher());
        edit_et_pubDate.setText(book.getPubDate());
        edit_et_readDate.setText(book.getReadDate());
        edit_et_library.setText(book.getLibrary());
        edit_et_bookStore.setText(book.getBookStore());
        edit_et_contents.setText(book.getContent());

        //radioGroup
        stateType = book.getState();

        radioGroup = (RadioGroup) findViewById(R.id.EditRadioGroup);
        edit_rb_done = (RadioButton) findViewById(R.id.edit_rb_done);
        edit_rb_reading = (RadioButton) findViewById(R.id.edit_rb_reading);
        edit_rb_will = (RadioButton) findViewById(R.id.edit_rb_will);

        if(stateType == 2) {
            radioGroup.check(edit_rb_done.getId());
        } else if(stateType == 1) {
            radioGroup.check(edit_rb_reading.getId());
        } else if (stateType == 0) {
            radioGroup.check(edit_rb_will.getId());
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.edit_rb_done) {
                    stateType = 2;
                } else if (i == R.id.edit_rb_reading) {
                    stateType = 1;
                } else if (i == R.id.edit_rb_will) {
                    stateType = 0;
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date;

        if (et_date_type == 1) {
            et_date = (EditText) findViewById(R.id.edit_et_pubDate);
        } else {
            et_date = (EditText) findViewById(R.id.edit_et_readDate);
        }
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    public void editOnClick(View v) {
        switch (v.getId()) {
            case R.id.edit_btn_ok:

                if (edit_et_title.getText().toString().equals("")) {
                    Toast.makeText(this, "필수 항목이 입력되지 않았습니다(제목)", Toast.LENGTH_SHORT).show();
                } else {
                    BookDTO editbook = new BookDTO(
                            book.get_id(),
                            edit_et_title.getText().toString(),
                            edit_et_author.getText().toString(),
                            edit_et_publisher.getText().toString(),
                            edit_et_pubDate.getText().toString(),
                            edit_et_readDate.getText().toString(),
                            edit_et_library.getText().toString(),
                            edit_et_bookStore.getText().toString(),
                            edit_image_src,
                            edit_et_contents.getText().toString(),
                            stateType);

                    boolean result = bookDBManager.modifyBook(editbook);

                    if (result) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("editDTO", editbook);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "정상적으로 수정되지 않았습니다", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.edit_btn_cancel:   // 취소에 따른 처리
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.edit_btn_library:
                intent = new Intent(EditBookActivity.this, GoogleMapActivity.class);
                intent.putExtra("place", "library");
                startActivityForResult(intent, LIBRARY_CODE);
                break;

            case R.id.edit_btn_bookstore:
                intent = new Intent(EditBookActivity.this, GoogleMapActivity.class);
                intent.putExtra("place", "bookstore");
                startActivityForResult(intent, BOOKSTORE_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NAVER_BOOK_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "불러오기 완료", Toast.LENGTH_SHORT).show();

                    NaverBookDTO naverBookDTO = (NaverBookDTO) data.getSerializableExtra("resultData");

                    edit_et_author.setText(naverBookDTO.getAuthor());
                    edit_et_title.setText(naverBookDTO.getTitle());
                    edit_et_pubDate.setText(naverBookDTO.getPubDate());
                    edit_et_publisher.setText(naverBookDTO.getPublisher());
                    edit_image_src = naverBookDTO.getImageFileName();

                    if (edit_image_src != null) {
                        Bitmap bitmap = imageFileManager.getBitmapFromExternal(edit_image_src);

                        if (bitmap != null) {
                            edit_image.setImageBitmap(bitmap);
                        }
                    }
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "불러오기 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        else if (requestCode == LIBRARY_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    String resultAddress = (String) data.getStringExtra("resultAddress");
                    edit_et_library.setText(resultAddress);
                    Toast.makeText(this, "불러오기 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "불러오기 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        else if (requestCode == BOOKSTORE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    String resultAddress = (String) data.getStringExtra("resultAddress");
                    edit_et_library.setText(resultAddress);
                    Toast.makeText(this, "불러오기 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "불러오기 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }
}
