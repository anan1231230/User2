package com.hclz.client.faxian.bean;

import java.io.Serializable;

/**
 * Created by hjm on 16/9/21.
 */

public class WenzhangBean implements Serializable {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic() {
        return image;
    }

    public void setPic(String pic) {
        this.image = pic;
    }

    public String getType() {
        return category_name;
    }

    public void setType(String type) {
        this.category_name = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    private boolean isRead = false;
    private String category;
    private String category_name;
    private String cover_image;
    private String id;
    private String image;
    private String title;
    private String url;



}
