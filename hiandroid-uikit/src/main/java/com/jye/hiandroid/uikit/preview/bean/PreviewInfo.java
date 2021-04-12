package com.jye.hiandroid.uikit.preview.bean;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * @author jye
 * @since 1.0
 */
public abstract class PreviewInfo implements Serializable {

    private Uri uri;

    private float imageViewX;
    private float imageViewY;
    private int imageViewWidth;
    private int imageViewHeight;

    private Bitmap placeholder;

    public PreviewInfo(Uri uri) {
        this.uri = uri;
    }

    public PreviewInfo(Uri uri, ImageView imageView) {
        this.uri = uri;

        if (imageView == null) {
            return;
        }

        this.imageViewX = imageView.getX();
        this.imageViewY = imageView.getY();
        this.imageViewWidth = imageView.getWidth();
        this.imageViewHeight = imageView.getHeight();

        try {
            //1、在获取图片前先调用setDrawingCacheEnabled(true)这个方法：
            imageView.setDrawingCacheEnabled(true);
            //2、之后可以通过getDrawingCache()获取图片
            Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());  //获取到Bitmap的图片
            //3、获取完图片后记得调用setDrawingCacheEnabled(false)
            imageView.setDrawingCacheEnabled(false);
            if (bitmap != null) {
                this.placeholder = bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public float getImageViewX() {
        return imageViewX;
    }

    public void setImageViewX(float imageViewX) {
        this.imageViewX = imageViewX;
    }

    public float getImageViewY() {
        return imageViewY;
    }

    public void setImageViewY(float imageViewY) {
        this.imageViewY = imageViewY;
    }

    public int getImageViewWidth() {
        return imageViewWidth;
    }

    public void setImageViewWidth(int imageViewWidth) {
        this.imageViewWidth = imageViewWidth;
    }

    public int getImageViewHeight() {
        return imageViewHeight;
    }

    public void setImageViewHeight(int imageViewHeight) {
        this.imageViewHeight = imageViewHeight;
    }

    public Bitmap getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(Bitmap placeholder) {
        this.placeholder = placeholder;
    }

}
