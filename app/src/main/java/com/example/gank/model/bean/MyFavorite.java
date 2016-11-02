package com.example.gank.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by dell on 2016/10/19.
 */
@Entity
public class MyFavorite {
    @Id
    Long id;
    String url;
    String desc;
    String who;
    String publishedAt;
    public String getPublishedAt() {
        return this.publishedAt;
    }
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
    public String getWho() {
        return this.who;
    }
    public void setWho(String who) {
        this.who = who;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 369085114)
    public MyFavorite(Long id, String url, String desc, String who,
            String publishedAt) {
        this.id = id;
        this.url = url;
        this.desc = desc;
        this.who = who;
        this.publishedAt = publishedAt;
    }
    @Generated(hash = 1538796775)
    public MyFavorite() {
    }
}
