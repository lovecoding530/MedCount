package com.gregapp.medcount.Model;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;

/**
 * Created by Kangtle_R on 12/22/2017.
 */

public class PhotoItem {
    public final long timestamp;
    public final long lastRemindTime;
    public final String timeStr;
    public final String lastRemindTimeStr;
    public final String photoUrl;
    public Bitmap bitmap = null;

    public PhotoItem(long timestamp, String photoUrl, long lastRemindTime) {
        this.timestamp = timestamp;
        this.photoUrl = photoUrl;
        this.lastRemindTime = lastRemindTime;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd' 'HH:mm");
        this.timeStr = simpleDateFormat.format(timestamp);
        this.lastRemindTimeStr = simpleDateFormat.format(lastRemindTime);
    }

    @Override
    public String toString() {
        return photoUrl;
    }
}
