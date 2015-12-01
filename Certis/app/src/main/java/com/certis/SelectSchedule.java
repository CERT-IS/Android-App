package com.certis;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SelectSchedule {

    final String TAG_YEAR = "year";
    final String TAG_MONTH = "month";
    final String TAG_DAY = "day";
    final String TAG_CONTENT = "content";

    ArrayList<String> arrayList;
    String year, month;

    String getContent = "";
    int gYear, gMonth, gDay = -1;

    public SelectSchedule() {

    }

    public String GetContent(int year, int month, int day){
        gYear = year;
        gMonth = month;
        gDay = day;

        executeREAD(gYear, gMonth);

        return getContent;
    }

    public ArrayList<String> executeREAD(int year, int month) {

        this.year = Integer.toString(year);
        this.month = Integer.toString(month);

        InputStream IS = null;
        BufferedReader reader;
        String result = "";

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(TAG_YEAR, this.year));
        nameValuePairs.add(new BasicNameValuePair(TAG_MONTH, this.month));

        JSONParse json = new JSONParse();
        IS = json.JSONParseExecute("SELECT", nameValuePairs);

        try {
            reader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"), 8);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            IS.close();
            result = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            arrayList = new ArrayList<String>();
            JSONArray jArray = new JSONArray(result);

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);

                String tmpDay = jObject.getString(TAG_DAY);
                String tmpContent = jObject.getString(TAG_CONTENT);

                if(gDay > 0 && Integer.parseInt(tmpDay) == gDay)
                    getContent = tmpContent;

                arrayList.add( tmpDay + "일 : " + tmpContent );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

}