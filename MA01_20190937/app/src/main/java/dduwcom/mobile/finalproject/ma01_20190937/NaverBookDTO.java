package dduwcom.mobile.finalproject.ma01_20190937;

import android.text.Html;
import android.text.Spanned;

import java.io.Serializable;
import java.util.Calendar;

public class NaverBookDTO implements Serializable {

    private long _id;
    private String title;
    private String author;
    private String publisher;
    private String pubDate;
    private String imageLink;
    private String imageFileName;       // 외부저장소에 저장했을 때의 파일명

    @Override
    public String toString() {
        return "NaverBookDTO{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", imageFileName='" + imageFileName + '\'' +
                '}';
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }
}
