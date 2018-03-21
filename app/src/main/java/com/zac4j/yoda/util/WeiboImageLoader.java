package com.zac4j.yoda.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import com.zac4j.yoda.util.image.GlideApp;

/**
 * Weibo image reader
 * Created by Zac on 2018/1/1.
 */

public class WeiboImageLoader {

    /**
     * Helper method for load and display single image
     *
     * @param context activity context
     * @param holder image holder
     * @param url image link url
     */
    public static void loadSingleImage(Context context, ImageView holder, String url) {
        GlideApp.with(context).load(url).into(holder);
    }
}
