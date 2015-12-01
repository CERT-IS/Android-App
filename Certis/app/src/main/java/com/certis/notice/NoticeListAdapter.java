package com.certis.notice;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.certis.NoticeActivity;
import com.certis.R;
import com.certis.volley.AppController;

import java.util.List;

/**
 * Created by GreenUser on 2015-11-15.
 */
public class NoticeListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Notice> noticeItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public NoticeListAdapter(Activity activity, List<Notice> noticeItems) {
        this.activity = activity;
        this.noticeItems = noticeItems;
    }

    @Override
    public int getCount() {
        return noticeItems.size();
    }

    @Override
    public Object getItem(int location) {
        return noticeItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.notice_list_row, null);
        }

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.noticeThumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.noticeListTitle);
        TextView uid = (TextView) convertView.findViewById(R.id.noticeListUid);
        TextView created_at = (TextView) convertView.findViewById(R.id.noticeListCreated_at);
        TextView id = (TextView) convertView.findViewById(R.id.noticeListId);

        // getting movie data for the row
        Notice notice = noticeItems.get(position);

        // title
        title.setText(notice.getTitle());

        // thumbnail image
        thumbNail.setImageUrl(NoticeActivity.mContext.getString(R.string.certis_api_image_host_url) + notice.getThumbnailUrl(), imageLoader);

        // uid
        uid.setText(notice.getUid());

        // created_at
        created_at.setText(notice.getCreated_at());

        // id
        id.setText(String.valueOf(notice.getId()));

        return convertView;
    }
}
