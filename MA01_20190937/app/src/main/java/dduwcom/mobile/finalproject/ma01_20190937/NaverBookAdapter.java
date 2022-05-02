package dduwcom.mobile.finalproject.ma01_20190937;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NaverBookAdapter extends BaseAdapter {

    public static final String TAG = "MyBookAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<NaverBookDTO> list;
    private NaverNetworkManager networkManager = null;
    private ImageFileManager imageFileManager = null;

    // context, 레이아웃, 원본데이터
    public NaverBookAdapter(Context context, int resource, ArrayList<NaverBookDTO> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        imageFileManager = new ImageFileManager(context); //이미지 파일 가져와야하니까
        networkManager = new NaverNetworkManager(context); // 네트워크 사용해야하니까
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public NaverBookDTO getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.tvTitle);
            viewHolder.tvAuthor = view.findViewById(R.id.tvAuthor);
            viewHolder.ivImage = view.findViewById(R.id.ivImage);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        NaverBookDTO dto = list.get(position);
        viewHolder.tvTitle.setText(dto.getTitle());
        viewHolder.tvAuthor.setText(dto.getAuthor());


        if (dto.getImageLink() == null) {// 원래 이미지 없을수도..
            viewHolder.ivImage.setImageResource(R.mipmap.ic_launcher);
            return view;
        }

        // 파일에 있는지 확인
        // dto 의 이미지 주소 정보로 이미지 파일 읽기
        Bitmap savedBitmap =
                imageFileManager.getBitmapFromTemporary(dto.getImageLink()); // 파일 이름

        if (savedBitmap != null) {
            viewHolder.ivImage.setImageBitmap(savedBitmap);
            Log.d(TAG, "Image loading from file");
        } else {
            viewHolder.ivImage.setImageResource(R.mipmap.ic_launcher); // 일단 default 설정
            new GetImageAsyncTask(viewHolder).execute(dto.getImageLink());
            Log.d(TAG, "Image loading from network");
        }

        return view;
    }


    public void setList(ArrayList<NaverBookDTO> list) {
        this.list = list;
        notifyDataSetChanged(); //알아서 notify 해서 adapter 반영
    }

    //    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView tvTitle = null;
        public TextView tvAuthor = null;
        public ImageView ivImage = null;
    }


    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        ViewHolder viewHolder;
        String imageAddress;

        //현재 이 뷰 홀더가 뭔지 알아놔야하기 때문에 생성자 설정
        public GetImageAsyncTask(ViewHolder holder) {
            viewHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageAddress = params[0];
            Bitmap result = null;
            result = networkManager.downloadImage(imageAddress);
            return result;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {
                viewHolder.ivImage.setImageBitmap(bitmap); //이미지 설정
                imageFileManager.saveBitmapToTemporary(bitmap, imageAddress); //이미지 저장
            }

        }
    }

}
