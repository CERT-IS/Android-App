package com.certis;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class InsertSchedule {

    String year, month, day, content;

    final String TAG_YEAR = "year";
    final String TAG_MONTH = "month";
    final String TAG_DAY = "day";
    final String TAG_CONTENT = "content";

    public InsertSchedule(int year, int month, int day, String content) {

        this.year = Integer.toString(year);
        this.month = Integer.toString(month);
        this.day = Integer.toString(day);
        this.content = content;

        executePOST();
    }

    private void executePOST() {

        InputStream IS = null;

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair(TAG_YEAR, year));
        nameValuePairs.add(new BasicNameValuePair(TAG_MONTH, month));
        nameValuePairs.add(new BasicNameValuePair(TAG_DAY, day));
        nameValuePairs.add(new BasicNameValuePair(TAG_CONTENT, content));

        JSONParse json = new JSONParse();
        json.JSONParseExecute("INSERT", nameValuePairs);

    }

}