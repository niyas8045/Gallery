package com.zaq.www.mygallery.Database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Pic {
    @PrimaryKey(autoGenerate = true)
    private int picid;
    @ColumnInfo(name = "pic_path")
    private String picPath;
    @ColumnInfo(name = "timestamp")
    private Long timestamp;

    public Pic(int picid, String picPath, Long timestamp) {
        this.picid = picid;
        this.picPath = picPath;
        this.timestamp = timestamp;
    }

    @Ignore
    public Pic(String picPath, Long timestamp) {
        this.picPath = picPath;
        this.timestamp = timestamp;
    }

    public int getPicid() {
        return picid;
    }

    public void setPicid(int picid) {
        this.picid = picid;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
