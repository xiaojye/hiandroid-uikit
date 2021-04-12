package com.jye.hiandroid.uikit.preview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.jye.hiandroid.uikit.preview.bean.ImagePreviewInfo;
import com.jye.hiandroid.uikit.preview.bean.VideoPreviewInfo;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 预览组件
 *
 * @author jye
 * @since 1.0
 */
public final class HIUIPreview {

    final WeakReference<Context> mContext;

    private HIUIPreview(Context context) {
        mContext = new WeakReference<>(context);
    }

    public static HIUIPreview with(Activity activity) {
        return new HIUIPreview(activity);
    }

    public static HIUIPreview with(Fragment fragment) {
        return new HIUIPreview(fragment.getActivity());
    }

    public static HIUIPreview with(Context context) {
        return new HIUIPreview(context);
    }

    public PreviewCreator imageList(List<ImagePreviewInfo> imageList) {
        return new PreviewCreator.ImagePreviewCreator(this, imageList);
    }

    public PreviewCreator videoList(List<VideoPreviewInfo> videoList) {
        return new PreviewCreator.VideoPreviewCreator(this, videoList);
    }

    /**
     * @author jye
     */
    public interface LoaderEngine {

        /**
         * 加载图片
         *
         * @param context      上下文对象
         * @param uri          图片Uri
         * @param placeholder  占位图
         * @param imageView    图片视图控件
         * @param progressView 加载进度视图
         */
        void loadImage(Context context, Uri uri, Drawable placeholder, ImageView imageView, View progressView);

    }

}
