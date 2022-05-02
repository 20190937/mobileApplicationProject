package dduwcom.mobile.finalproject.ma01_20190937;


import android.text.Editable;

import java.io.Serializable;
import java.util.Calendar;

public class BookDTO implements Serializable {
    private long _id;
    private String title;
    private String author;
    private String publisher;
    private String pubDate;
    private String readDate;
    private String library;
    private String bookStore;
    private String imageFileName;
    private String content;
    private int state;

    public BookDTO(long _id, String title, String author, String publisher,
                   String pubDate, String readDate, String library, String bookStore,
                   String imageFileName, String content, int state) {
        this._id = _id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.readDate = readDate;
        this.library = library;
        this.bookStore = bookStore;
        this.imageFileName = imageFileName;
        this.content = content;
        this.state = state;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getBookStore() {
        return bookStore;
    }

    public void setBookStore(String bookStore) {
        this.bookStore = bookStore;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
