package com.certis.notice;

/**
 * Created by GreenUser on 2015-11-15.
 */
public class Notice {
    private String title;
    private String thumbnailUrl;
    private String uid;
    private String created_at;
    private int id;

    public Notice() {
    }

    public Notice(String title, String thumbnailUrl, String uid, String created_at, int id) {
        this.title = title;
        this.uid= uid;
        this.created_at = created_at;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
