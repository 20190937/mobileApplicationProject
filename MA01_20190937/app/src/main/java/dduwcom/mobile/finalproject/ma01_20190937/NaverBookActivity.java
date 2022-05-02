package dduwcom.mobile.finalproject.ma01_20190937;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NaverBookActivity extends AppCompatActivity {
    public static final String TAG = "NaverBookActivity";

    EditText etTarget;
    ListView lvList;
    String apiAddress;

    String query;
    NaverBookAdapter adapter;
    ArrayList<NaverBookDTO> resultList;

    NaverBookXmlParser parser;
    NaverNetworkManager networkManager;
    ImageFileManager imgFileManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naverbook);

        etTarget = findViewById(R.id.etTarget);
        lvList = findViewById(R.id.lvList);

        resultList = new ArrayList();
        adapter = new NaverBookAdapter(this, R.layout.custom_naverbook_layout, resultList); //아직 비었음
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.api_url);
        parser = new NaverBookXmlParser();

        networkManager = new NaverNetworkManager(this);
        networkManager.setClientId(getResources().getString(R.string.client_id));
        networkManager.setClientSecret(getResources().getString(R.string.client_secret));

        imgFileManager = new ImageFileManager(this); //this : 이 앱이 갖고 있는 환경정보

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NaverBookDTO book = resultList.get(position);

                String result = imgFileManager.moveFileToExt(resultList.get(position).getImageLink());
                Log.d(TAG, result);

                book.setImageFileName(result);
                Log.d(TAG, book.toString());


                Intent resultIntent = new Intent();
                resultIntent.putExtra("resultData", book);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 임시 파일 삭제
        imgFileManager.clearTemporaryFiles();
    }


    public void onClick(View v) { //버튼 눌렀을 때때
        switch(v.getId()) {
            case R.id.btn_placeInfo:
                query = etTarget.getText().toString();  // UTF-8 인코딩 필요

                try {
                    new NetworkAsyncTask().execute(apiAddress
                            + URLEncoder.encode(query, "UTF-8"));//오류 날수도 있으니 try catch문
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    //입력, 진행상황(필요없음 void), 출력
    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() { // UI
            super.onPreExecute();
            progressDlg = ProgressDialog.show(NaverBookActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) { // 실제 스레드
            String address = strings[0];
            String result = null;

            result = networkManager.downloadContents(address);

            if (result == null) return "Error!";
            Log.d(TAG, result);

            for (NaverBookDTO dto : resultList) {
                Bitmap bitmap = networkManager.downloadImage(dto.getImageLink());
                if (bitmap != null) {
                    imgFileManager.saveBitmapToTemporary(bitmap, dto.getImageLink());
                }
            }

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            resultList = parser.parse(result);

            adapter.setList(resultList);
            progressDlg.dismiss();
        }

    }
}
