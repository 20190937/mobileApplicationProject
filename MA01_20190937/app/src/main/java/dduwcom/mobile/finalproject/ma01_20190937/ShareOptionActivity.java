package dduwcom.mobile.finalproject.ma01_20190937;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShareOptionActivity extends AppCompatActivity {
    CheckBox cb_title;
    CheckBox cb_author;
    CheckBox cb_publisher;
    CheckBox cb_pubDate;
    CheckBox cb_readDate;
    CheckBox cb_content;
    EditText et_head;
    EditText et_tail;
    SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareoption);

        cb_title = (CheckBox) findViewById(R.id.cb_title);
        cb_author = (CheckBox) findViewById(R.id.cb_author);
        cb_publisher = (CheckBox) findViewById(R.id.cb_publisher);
        cb_pubDate = (CheckBox) findViewById(R.id.cb_pubDate);
        cb_readDate = (CheckBox) findViewById(R.id.cb_readDate);
        cb_content = (CheckBox) findViewById(R.id.cb_content);
        et_head = (EditText) findViewById(R.id.et_head);
        et_tail = (EditText) findViewById(R.id.ed_tail);

        spf = getSharedPreferences("share", 0);

        boolean[] options = new boolean[6];

        options[0] = spf.getBoolean("id0", true);
        options[1] = spf.getBoolean("id1", true);
        options[2] = spf.getBoolean("id2", true);
        options[3] = spf.getBoolean("id3", true);
        options[4] = spf.getBoolean("id4", true);
        options[5] = spf.getBoolean("id5", true);

        cb_title.setChecked(options[0]);
        cb_author.setChecked(options[1]);
        cb_publisher.setChecked(options[2]);
        cb_pubDate.setChecked(options[3]);
        cb_readDate.setChecked(options[4]);
        cb_content.setChecked(options[5]);

        spf = getSharedPreferences("addText", 0);
        String head = spf.getString("head", "");
        String tail = spf.getString("tail", "");
        if (!head.equals("")) {
            et_head.setText(head);
        }
        if (!tail.equals("")) {
            et_tail.setText(tail);
        }
    }

    public void shareOnClick(View v) {
        switch (v.getId()) {
            case R.id.share_btn_ok:
                spf = getSharedPreferences("share", 0);
                SharedPreferences.Editor editor = spf.edit();

                if (cb_title.isChecked()) {
                    editor.putBoolean("id0", true);
                } else {
                    editor.putBoolean("id0", false);
                }

                if (cb_author.isChecked()) {
                    editor.putBoolean("id1", true);
                } else {
                    editor.putBoolean("id1", false);
                }

                if (cb_publisher.isChecked()) {
                    editor.putBoolean("id2", true);
                } else {
                    editor.putBoolean("id2", false);
                }

                if (cb_pubDate.isChecked()) {
                    editor.putBoolean("id3", true);
                } else {
                    editor.putBoolean("id3", false);
                }

                if (cb_readDate.isChecked()) {
                    editor.putBoolean("id4", true);
                } else {
                    editor.putBoolean("id4", false);
                }

                if (cb_content.isChecked()) {
                    editor.putBoolean("id5", true);
                } else {
                    editor.putBoolean("id5", false);
                }

                editor.commit();

                spf = getSharedPreferences("addText", 0);
                SharedPreferences.Editor editor2 = spf.edit();
                editor2.putString("head", et_head.getText().toString());
                editor2.putString("tail", et_tail.getText().toString());
                editor2.commit();

                setResult(RESULT_OK);
                finish();
                break;
            case R.id.share_btn_cancel:   // 취소에 따른 처리
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }


}
