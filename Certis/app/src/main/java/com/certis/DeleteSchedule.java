package com.certis;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DeleteSchedule {

    String year, month, day;

    final String TAG_YEAR = "year";
    final String TAG_MONTH = "month";
    final String TAG_DAY = "day";

    public DeleteSchedule(int year, int month, int day) {

        this.year = Integer.toString(year);
        this.month = Integer.toString(month);
        this.day = Integer.toString(day);

        executePOST();
    }

    private void executePOST() {

        InputStream IS = null;

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair(TAG_YEAR, year));
        nameValuePairs.add(new BasicNameValuePair(TAG_MONTH, month));
        nameValuePairs.add(new BasicNameValuePair(TAG_DAY, day));

        JSONParse json = new JSONParse();
        json.JSONParseExecute("DELETE", nameValuePairs);

    }

}