package com.gregapp.medcount.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kangtle_R on 12/22/2017.
 */

public class BarcodeItem {
    public final long timestamp;
    public String timeStr;
    public final String barcodeContent;

    public BarcodeItem(long timestamp, String barcodeContent) {
        this.timestamp = timestamp;
        this.barcodeContent = barcodeContent;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd' 'HH:mm");
        this.timeStr = simpleDateFormat.format(timestamp);
    }

    @Override
    public String toString() {
        return barcodeContent;
    }
}
