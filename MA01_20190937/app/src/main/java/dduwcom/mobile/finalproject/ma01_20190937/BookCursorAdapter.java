package dduwcom.mobile.finalproject.ma01_20190937;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;
    int layout;
    private ImageFileManager imageFileManager;

    public BookCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        imageFileManager = new ImageFileManager(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder();
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.tvBookTitle == null) {
            holder.ivImage = view.findViewById(R.id.ivBookImage);
            holder.tvBookTitle = view.findViewById(R.id.tvBookTitle);
            holder.tvBookAuthor = view.findViewById(R.id.tvBookAuthor);
            holder.tvBookReadDate = view.findViewById(R.id.tvBookReadDate);
        }

        Bitmap bitmap = imageFileManager.getBitmapFromExternal(cursor.getString(cursor.getColumnIndexOrThrow(BookDBHelper.COL_IMAGEFILENAME)));

        if (bitmap != null) {
            holder.ivImage.setImageBitmap(bitmap);
        }

        holder.tvBookTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookDBHelper.COL_TITLE)));
        holder.tvBookAuthor.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookDBHelper.COL_AUTHOR)));
        holder.tvBookReadDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(BookDBHelper.COL_READDATE)));
    }

    static class ViewHolder {
        ImageView ivImage;
        TextView tvBookTitle;
        TextView tvBookAuthor;
        TextView tvBookReadDate;

        public ViewHolder() {
            ivImage = null;
            tvBookTitle = null;
            tvBookAuthor = null;
            tvBookReadDate = null;
        }
    }
}
