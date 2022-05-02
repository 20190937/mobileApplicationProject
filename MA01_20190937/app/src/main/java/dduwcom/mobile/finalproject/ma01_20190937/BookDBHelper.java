package dduwcom.mobile.finalproject.ma01_20190937;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookDBHelper extends SQLiteOpenHelper {
    final static String TAG = "BookDBHelper";

    final static String DB_NAME = "books.db";
    public final static String TABLE_NAME = "book_table";

    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
    public final static String COL_AUTHOR = "author";
    public final static String COL_PUBLISHER = "publisher";
    public final static String COL_PUBDATE = "pubdate";
    public final static String COL_READDATE = "readdate";
    public final static String COL_LIBRARY = "library";
    public final static String COL_BOOKSTORE = "bookstore";
    public final static String COL_IMAGEFILENAME = "imagefilename";
    public final static String COL_CONTENT = "content";
    public final static String COL_STATE = "state";


    public BookDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " +
                COL_TITLE + " TEXT, " + COL_AUTHOR + " TEXT, " + COL_PUBLISHER + " TEXT, " +
                COL_PUBDATE + " TEXT, " + COL_READDATE + " TEXT, " +
                COL_LIBRARY + " TEXT, " + COL_BOOKSTORE + " TEXT, " +
                COL_IMAGEFILENAME + " TEXT, " + COL_CONTENT + " TEXT, " +
                COL_STATE + " TEXT)";
        Log.d(TAG, sql);
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
