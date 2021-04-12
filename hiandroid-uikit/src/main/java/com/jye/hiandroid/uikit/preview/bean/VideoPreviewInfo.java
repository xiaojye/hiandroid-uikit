package com.jye.hiandroid.uikit.preview.bean;

import android.net.Uri;
import android.widget.ImageView;

/**
 * @author jye
 * @since 1.0
 */
public class VideoPreviewInfo extends PreviewInfo {


    public VideoPreviewInfo(Uri uri) {
        super(uri);
    }

    public VideoPreviewInfo(Uri uri, ImageView imageView) {
        super(uri, imageView);
    }

}
