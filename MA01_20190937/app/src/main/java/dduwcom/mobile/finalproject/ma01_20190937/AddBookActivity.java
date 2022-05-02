package dduwcom.mobile.finalproject.ma01_20190937;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddBookActivity extends AppCompatActivity {
    public static final String TAG = "AddBookActivity";

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
    ImageView add_image;
    String add_image_src;
    EditText add_et_title;
    EditText add_et_author;
    EditText add_et_publisher;
    EditText add_et_pubDate;
    EditText add_et_readDate;
    EditText add_et_library;
    EditText add_et_bookStore;
    EditText add_et_contents;
    RadioGroup radioGroup;
    int stateType = -1;

    ImageFileManager imageFileManager = null;
    BookDBManager bookDBManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        add_image = (ImageView) findViewById(R.id.add_image);
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AddBookActivity.this, NaverBookActivity.class);
                startActivityForResult(intent, NAVER_BOOK_CODE); // onresume이 자동 호출될것
            }
        });

        add_et_title = (EditText) findViewById(R.id.add_et_title);
        add_et_author = (EditText) findViewById(R.id.add_et_author);
        add_et_publisher = (EditText) findViewById(R.id.add_et_publisher);

        add_et_library = (EditText) findViewById(R.id.add_et_library);
        add_et_bookStore = (EditText) findViewById(R.id.add_et_bookStore);
        add_et_contents = (EditText) findViewById(R.id.add_et_contents);
                
        add_et_pubDate = (EditText) findViewById(R.id.add_et_pubDate);
        add_et_pubDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_date_type = 1;
                new DatePickerDialog(AddBookActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        add_et_readDate = (EditText) findViewById(R.id.add_et_readDate);
        add_et_readDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_date_type = 2;
                new DatePickerDialog(AddBookActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        imageFileManager = new ImageFileManager(this);
        bookDBManager = new BookDBManager(this);

        radioGroup = (RadioGroup) findViewById(R.id.addRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.add_rb_done) {
                    stateType = 2;
                } else if (i == R.id.add_rb_reading) {
                    stateType = 1;
                } else if (i == R.id.add_rb_will) {
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
            et_date = (EditText) findViewById(R.id.add_et_pubDate);
        } else {
            et_date = (EditText) findViewById(R.id.add_et_readDate);
        }
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    public void addOnClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn_ok:

                if (add_et_title.getText().toString().equals("")) {
                    Toast.makeText(this, "필수 항목이 입력되지 않았습니다(제목)", Toast.LENGTH_SHORT).show();
                } else if (stateType == -1) {
                    Toast.makeText(this, "필수 항목이 입력되지 않았습니다(등록 상태)", Toast.LENGTH_SHORT).show();
                } else {
                    BookDTO book = new BookDTO(
                            -1,
                            add_et_title.getText().toString(),
                            add_et_author.getText().toString(),
                            add_et_publisher.getText().toString(),
                            add_et_pubDate.getText().toString(),
                            add_et_readDate.getText().toString(),
                            add_et_library.getText().toString(),
                            add_et_bookStore.getText().toString(),
                            add_image_src,
                            add_et_contents.getText().toString(),
                            stateType);

                    boolean result = bookDBManager.addNewBook(book);

                    if (result) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "정상적으로 등록되지 않았습니다", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.add_btn_cancel:   // 취소에 따른 처리
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.add_btn_library:
                intent = new Intent(AddBookActivity.this, GoogleMapActivity.class);
                intent.putExtra("place", "library");
                startActivityForResult(intent, LIBRARY_CODE);
                break;

            case R.id.add_btn_bookstore:
                intent = new Intent(AddBookActivity.this, GoogleMapActivity.class);
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
                    NaverBookDTO naverBookDTO = (NaverBookDTO) data.getSerializableExtra("resultData");

                    add_et_author.setText(naverBookDTO.getAuthor());
                    add_et_title.setText(naverBookDTO.getTitle());
                    add_et_pubDate.setText(naverBookDTO.getPubDate());
                    add_et_publisher.setText(naverBookDTO.getPublisher());
                    add_image_src = naverBookDTO.getImageFileName();

                    if (add_image_src != null) {
                        Bitmap bitmap = imageFileManager.getBitmapFromExternal(add_image_src);

                        if (bitmap != null) {
                            add_image.setImageBitmap(bitmap);
                        }
                    }
                    Toast.makeText(this, "불러오기 완료", Toast.LENGTH_SHORT).show();
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
                    add_et_library.setText(resultAddress);
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
                    add_et_bookStore.setText(resultAddress);
                    Toast.makeText(this, "불러오기 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "불러오기 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
