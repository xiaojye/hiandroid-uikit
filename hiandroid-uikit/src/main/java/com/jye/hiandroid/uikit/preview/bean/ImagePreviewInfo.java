package com.jye.hiandroid.uikit.preview.bean;

import android.net.Uri;
import android.widget.ImageView;

/**
 * @author jye
 * @since 1.0
 */
public class ImagePreviewInfo extends PreviewInfo {


    public ImagePreviewInfo(Uri uri) {
        super(uri);
    }

    public ImagePreviewInfo(Uri uri, ImageView imageView) {
        super(uri, imageView);
    }

}
