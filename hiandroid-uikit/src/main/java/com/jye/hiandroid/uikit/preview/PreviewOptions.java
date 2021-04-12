package com.jye.hiandroid.uikit.preview;

import android.content.Context;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.jye.hiandroid.uikit.preview.bean.ImagePreviewInfo;
import com.jye.hiandroid.uikit.preview.bean.PreviewInfo;
import com.jye.hiandroid.uikit.preview.bean.VideoPreviewInfo;

import java.util.List;

/**
 * @author jye
 * @since 1.0
 */
public class PreviewOptions {

    /**
     * 上下文对象
     */
    public Context context;

    /**
     * 预览图片加载器接口
     */
    public HIUIPreview.LoaderEngine loaderEngine;

    /**
     * 预览信息列表
     */
    public List<PreviewInfo> previewInfoList;

    /**
     * 图片预览信息列表
     */
    public List<ImagePreviewInfo> imagePreviewInfoList;


    /**
     * 视频预览信息列表
     */
    public List<VideoPreviewInfo> videoPreviewInfoList;

    /**
     * 当前索引
     */
    public int currentIndex;

    /**
     * 自定义遮盖层
     */
    public View customShadeView;

    /**
     * 自定义进度视图
     */
    public View customProgressView;

    /**
     * 点击事件监听器
     */
    public View.OnClickListener onClickListener;

    /**
     * 长按事件监听器
     */
    public View.OnLongClickListener onLongClickListener;

    /**
     * 页面切换监听器
     */
    public ViewPager.OnPageChangeListener onPageChangeListener;

    private PreviewOptions() {
    }

    private static final class InstanceHolder {
        private static final PreviewOptions INSTANCE = new PreviewOptions();
    }

    public static PreviewOptions getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static PreviewOptions getCleanInstance() {
        PreviewOptions previewOptions = getInstance();
        previewOptions.reset();
        return previewOptions;
    }

    private void reset() {
        context = null;
        loaderEngine = null;
        previewInfoList = null;
        imagePreviewInfoList = null;
        videoPreviewInfoList = null;
        currentIndex = 0;
        customShadeView = null;
        customProgressView = null;
        onClickListener = null;
        onLongClickListener = null;
        onPageChangeListener = null;
    }

}
