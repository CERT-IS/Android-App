package com.certis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Custom_List_Adapter extends ArrayAdapter<Custom_List_Data> {
    private Context m_Context = null;

    public Custom_List_Adapter(Context context, int textViewResourceId, ArrayList<Custom_List_Data> items) {
        super(context, textViewResourceId, items);
        this.m_Context = context;
    }

    @Override
    public View getView(int nPosition, View convertView, ViewGroup parent) {
        // 뷰를 재사용 하기 위해 필요한 클래스
        PointerView pView = null;

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(m_Context).inflate(R.layout.activity_custom__list__adapter, null);
            pView = new PointerView(view);
            view.setTag(pView);
        }

        pView = (PointerView) view.getTag();

        // 데이터 클래스에서 해당 리스트목록의 데이터를 가져온다.
        Custom_List_Data custom_list_data = getItem(nPosition);

        if (custom_list_data != null) {
            // 현재 item의 position에 맞는 이미지와 글을 넣어준다.
            pView.GetIconView().setBackgroundResource(custom_list_data.getImage_ID());
            pView.GetTitleView().setText(custom_list_data.getMain_Title());
            pView.GetSubTitleView().setText(custom_list_data.getSub_Title());
            pView.GetContentView().setText(custom_list_data.getContent());
        }

        return view;
    }

    /**
     * 뷰를 재사용 하기위해 필요한 클래스
     * 클래스 자체를 view tag로 저장/불러오므로 재사용가능
     */
    private class PointerView {
        private View m_BaseView = null;
        private ImageView m_ivIcon = null;
        private TextView m_tvTitle = null;
        private TextView m_tvSubTitle;
        private TextView m_tvContent;

        public PointerView(View BaseView) {
            this.m_BaseView = BaseView;
            m_tvSubTitle = null;
            m_tvContent = null;
        }

        public ImageView GetIconView() {
            if (m_ivIcon == null) {
                m_ivIcon = (ImageView) m_BaseView.findViewById(R.id.ListViewImage);
            }

            return m_ivIcon;
        }

        public TextView GetTitleView() {
            if (m_tvTitle == null) {
                m_tvTitle = (TextView) m_BaseView.findViewById(R.id.ListViewTitle);
            }

            return m_tvTitle;
        }

        public TextView GetSubTitleView() {
            if (m_tvSubTitle == null) {
                m_tvSubTitle = (TextView) m_BaseView.findViewById(R.id.ListViewAddress);
            }

            return m_tvSubTitle;
        }

        public TextView GetContentView() {
            if (m_tvContent == null) {
                m_tvContent = (TextView) m_BaseView.findViewById(R.id.ListViewContent);
            }

            return m_tvContent;
        }

    }
}

/**
 * 리스트의 데이터 클래스
 */
class Custom_List_Data {
    private int Image_ID;
    private String Main_Title;
    private String Sub_Title;
    private String Content;

    public Custom_List_Data(int _Image_ID, String _Main_Title, String _Sub_Title, String _Content) {
        this.setImage_ID(_Image_ID);
        this.setMain_Title(_Main_Title);
        this.setSub_Title(_Sub_Title);
        this.setContent(_Content);
    }

    public int getImage_ID() { return Image_ID; }

    public void setImage_ID(int image_ID) { Image_ID = image_ID; }

    public String getMain_Title() {
        return Main_Title;
    }

    public void setMain_Title(String main_Title) {
        Main_Title = main_Title;
    }

    public String getSub_Title() {
        return Sub_Title;
    }

    public void setSub_Title(String sub_Title) {
        Sub_Title = sub_Title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

}