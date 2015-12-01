package com.certis.link;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.certis.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;

    //List of superHeroes
    List<SiteLink> sitelink;

    public CardAdapter(List<SiteLink> sitelink, Context context) {
        super();
        this.sitelink = sitelink;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sitelink_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SiteLink itemObj = sitelink.get(position);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(itemObj.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.imageView.setImageUrl(itemObj.getImageUrl(), imageLoader);
        holder.textViewTitle.setText(itemObj.getTitle());
        holder.textViewUrls.setText(itemObj.getUrls());
    }

    @Override
    public int getItemCount() {
        return sitelink.size();
    }

class ViewHolder extends RecyclerView.ViewHolder {
    public NetworkImageView imageView;
    public TextView textViewTitle;
    public TextView textViewUrls;

    public ViewHolder(View itemView) {
        super(itemView);
        imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
        textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
        textViewUrls = (TextView) itemView.findViewById(R.id.textViewUrls);
    }
}
}
