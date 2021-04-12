package com.jye.hiandroid.uikit.preview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.jye.hiandroid.uikit.R;
import com.jye.hiandroid.uikit.preview.bean.ImagePreviewInfo;
import com.jye.hiandroid.uikit.preview.bean.VideoPreviewInfo;
import com.jye.hiandroid.uikit.preview.ui.ImagePreviewActivity;

import java.util.List;

/**
 * @author jye
 * @since 1.0
 */
public abstract class PreviewCreator {

    final PreviewOptions mPreviewOptions;

    PreviewCreator(HIUIPreview HIUIPreview) {
        mPreviewOptions = PreviewOptions.getCleanInstance();
        mPreviewOptions.context = HIUIPreview.mContext.get();
    }

    /**
     * 设置当前索引
     *
     * @param currentIndex 当前索引位置
     * @return PreviewCreator
     */
    public PreviewCreator currentIndex(int currentIndex) {
        mPreviewOptions.currentIndex = currentIndex;
        return this;
    }

    /**
     * 设置自定义遮盖层，定制自己想要的效果，当设置遮盖层后，原本的指示器会被隐藏
     *
     * @param customShadeView 自定义遮盖层
     * @return PreviewCreator
     */
    public PreviewCreator customShadeView(View customShadeView) {
        mPreviewOptions.customShadeView = customShadeView;
        return this;
    }

    /**
     * 自定义ProgressView
     *
     * @param customProgressView 自定义ProgressView
     * @return PreviewCreator
     */
    public PreviewCreator customProgressView(View customProgressView) {
        mPreviewOptions.customProgressView = customProgressView;
        return this;
    }

    /**
     * 设置点击事件监听器
     *
     * @param onClickListener 点击事件监听器
     * @return PreviewCreator
     */
    public PreviewCreator onClickListener(View.OnClickListener onClickListener) {
        mPreviewOptions.onClickListener = onClickListener;
        return this;
    }

    /**
     * 设置长按事件监听器
     *
     * @param onLongClickListener 长按事件监听器
     * @return PreviewCreator
     */
    public PreviewCreator onLongClickListener(View.OnLongClickListener onLongClickListener) {
        mPreviewOptions.onLongClickListener = onLongClickListener;
        return this;
    }

    /**
     * 设置页面切换监听器
     *
     * @param onPageChangeListener 页面切换监听器
     * @return PreviewCreator
     */
    public PreviewCreator onPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mPreviewOptions.onPageChangeListener = onPageChangeListener;
        return this;
    }

    /**
     * 打开预览界面
     *
     * @param loaderEngine 图片¬加载引擎
     */
    public void start(HIUIPreview.LoaderEngine loaderEngine) {
        mPreviewOptions.loaderEngine = loaderEngine;

        final Context context = mPreviewOptions.context;
        if (context == null) {
            return;
        }

        doStart(context);
    }

    /**
     * 子类实现预览页面的跳转
     *
     * @param context 上下文对象
     */
    protected abstract void doStart(Context context);

    public static class ImagePreviewCreator extends PreviewCreator {

        ImagePreviewCreator(HIUIPreview HIUIPreview, List<ImagePreviewInfo> imagePreviewInfoList) {
            super(HIUIPreview);
            mPreviewOptions.imagePreviewInfoList = imagePreviewInfoList;
        }

        @Override
        protected void doStart(Context context) {
            Intent intent = new Intent(context, ImagePreviewActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.preview_enter_anim, 0);
        }

    }

    public static class VideoPreviewCreator extends PreviewCreator {

        VideoPreviewCreator(HIUIPreview HIUIPreview, List<VideoPreviewInfo> videoPreviewInfoList) {
            super(HIUIPreview);
            mPreviewOptions.videoPreviewInfoList = videoPreviewInfoList;
        }

        @Override
        protected void doStart(Context context) {
            Intent intent = new Intent(context, ImagePreviewActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.preview_enter_anim, 0);
        }

    }

}
