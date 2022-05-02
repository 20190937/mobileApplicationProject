package dduwcom.mobile.finalproject.ma01_20190937;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class BookDBManager {

    BookDBHelper bookDBHelper = null;
    Cursor cursor = null;

    public BookDBManager(Context context) {
        bookDBHelper = new BookDBHelper(context);
    }

    public boolean addNewBook(BookDTO newBook) {
        SQLiteDatabase db = bookDBHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(BookDBHelper.COL_TITLE, newBook.getTitle());
        value.put(BookDBHelper.COL_AUTHOR, newBook.getAuthor());
        value.put(BookDBHelper.COL_PUBLISHER, newBook.getPublisher());
        value.put(BookDBHelper.COL_PUBDATE, newBook.getPubDate());
        value.put(BookDBHelper.COL_READDATE, newBook.getReadDate());
        value.put(BookDBHelper.COL_LIBRARY, newBook.getLibrary());
        value.put(BookDBHelper.COL_BOOKSTORE, newBook.getBookStore());
        value.put(BookDBHelper.COL_IMAGEFILENAME, newBook.getImageFileName());
        value.put(BookDBHelper.COL_CONTENT, newBook.getContent());
        value.put(BookDBHelper.COL_STATE, newBook.getState());

        long count = db.insert(BookDBHelper.TABLE_NAME, null, value);
        close();

        if (count > 0) return true;
        else return false;
    }

    public boolean modifyBook(BookDTO book) {
        SQLiteDatabase sqLiteDatabase = bookDBHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(BookDBHelper.COL_TITLE, book.getTitle());
        value.put(BookDBHelper.COL_AUTHOR, book.getAuthor());
        value.put(BookDBHelper.COL_PUBLISHER, book.getPublisher());
        value.put(BookDBHelper.COL_PUBDATE, book.getPubDate());
        value.put(BookDBHelper.COL_READDATE, book.getReadDate());
        value.put(BookDBHelper.COL_LIBRARY, book.getLibrary());
        value.put(BookDBHelper.COL_BOOKSTORE, book.getBookStore());
        value.put(BookDBHelper.COL_IMAGEFILENAME, book.getImageFileName());
        value.put(BookDBHelper.COL_CONTENT, book.getContent());
        value.put(BookDBHelper.COL_STATE, book.getState());

        String whereClause = bookDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(book.get_id())};

        int result = sqLiteDatabase.update(BookDBHelper.TABLE_NAME, value, whereClause, whereArgs);
        close();

        if (result > 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean removeBook(long id) {
        SQLiteDatabase sqLiteDatabase = bookDBHelper.getWritableDatabase();
        String whereClause = BookDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        int result = sqLiteDatabase.delete(BookDBHelper.TABLE_NAME, whereClause, whereArgs);
        close();

        if (result > 0){
            return true;
        } else {
            return false;
        }
    }

    public String getTitlebyId(long id) {
        String title;
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        String selection = "_id=?";
        String[] selectArgs = new String[]{Long.toString(id)};

        Cursor cursor = db.query(BookDBHelper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);

        while(cursor.moveToNext()) {
            title = cursor.getString(cursor.getColumnIndexOrThrow(BookDBHelper.COL_TITLE));
            return title;
        }

        return null;
    }

    public BookDTO getBookbyId(long id) {
        BookDTO book = null;
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        String selection = "_id=?";
        String[] selectArgs = new String[]{Long.toString(id)};

        Cursor cursor = db.query(BookDBHelper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);

        while(cursor.moveToNext()) {
            long _id = cursor.getLong(cursor.getColumnIndexOrThrow(bookDBHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(bookDBHelper.COL_TITLE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(bookDBHelper.COL_AUTHOR));
            String publisher = cursor.getString(cursor.getColumnIndexOrThrow(bookDBHelper.COL_PUBLISHER));
            String pubDate = cursor.getString(cursor.getColumnIndexOrThrow(bookDBHelper.COL_PUBDATE));
            String readDate = cursor.getString(cursor.getColumnIndexOrThrow(bookDBHelper.COL_READDATE));
            String library = cursor.getString(cursor.getColumnIndexOrThrow(bookDBHelper.COL_LIBRARY));
            String bookStore = cursor.getString(cursor.getColumnIndexOrThrow(bookDBHelper.COL_BOOKSTORE));
            String imageFileName = cursor.getString(cursor.getColumnIndexOrThrow(bookDBHelper.COL_IMAGEFILENAME));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(bookDBHelper.COL_CONTENT));
            int state = cursor.getInt(cursor.getColumnIndexOrThrow(bookDBHelper.COL_STATE));
            book = new BookDTO (_id, title, author, publisher, pubDate, readDate, library, bookStore, imageFileName, content, state);
        }

        return book;
    }

    public void close() {
        if (bookDBHelper != null) bookDBHelper.close();
        if (cursor != null) cursor.close();
    }

}
