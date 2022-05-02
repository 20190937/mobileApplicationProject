package dduwcom.mobile.finalproject.ma01_20190937;


import android.text.Html;
import android.text.Spanned;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class NaverBookXmlParser {

    public enum TagType { NONE, TITLE, AUTHOR, PUBLISHER, PUBDATE, IMAGE };

    final static String TAG_ITEM = "item";
    final static String TAG_TITLE = "title";
    final static String TAG_AUTHOR = "author";
    final static String TAG_PUBLISHER = "publisher";
    final static String TAG_PUBDATE = "pubdate";
    final static String TAG_IMAGE = "image";

    Spanned spanned;

    public NaverBookXmlParser() {
    }

    public ArrayList<NaverBookDTO> parse(String xml) {

        ArrayList<NaverBookDTO> resultList = new ArrayList();
        NaverBookDTO dto = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            dto = new NaverBookDTO();
                        } else if (parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) tagType = TagType.TITLE; //아이템 안에 들어갔는지 확인
                        } else if (parser.getName().equals(TAG_AUTHOR)) {
                            if (dto != null) tagType = TagType.AUTHOR;
                        } else if (parser.getName().equals(TAG_PUBLISHER)) {
                            if (dto != null) tagType = TagType.PUBLISHER;
                        } else if (parser.getName().equals(TAG_PUBDATE)) {
                            if (dto != null) tagType = TagType.PUBDATE;
                        }else if (parser.getName().equals(TAG_IMAGE)) {
                            if (dto != null) tagType = TagType.IMAGE;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TITLE:
                                spanned = Html.fromHtml(parser.getText());
                                dto.setTitle(spanned.toString());
                                break;
                            case AUTHOR:
                                spanned= Html.fromHtml(parser.getText());
                                dto.setAuthor(spanned.toString());
                                break;
                            case PUBLISHER:
                                spanned = Html.fromHtml(parser.getText());
                                dto.setPublisher(spanned.toString());
                                break;
                            case PUBDATE:
                                String str = parser.getText();
                                int year = Integer.parseInt(str.substring(0,4));
                                int month = Integer.parseInt(str.substring(4,6));
                                int day = Integer.parseInt(str.substring(6));
                                String result = year+"/"+month+"/"+day;
                                dto.setPubDate(result);
                                break;
                            case IMAGE:
                                dto.setImageLink(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}