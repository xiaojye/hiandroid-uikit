package com.jye.hiandroid.uikit.preview.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jye.hiandroid.uikit.R;
import com.jye.hiandroid.uikit.preview.PreviewOptions;
import com.jye.hiandroid.uikit.preview.bean.ImagePreviewInfo;
import com.jye.hiandroid.uikit.preview.bean.PreviewInfo;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author jye
 * @since 1.0
 */
public class PreviewPagerAdapter extends PagerAdapter {

    private Context mContext;

    private PreviewOptions mPreviewOptions;

    private List<ImagePreviewInfo> mImagePreviewInfoList;

    private View mCurrentView;

    private LayoutInflater mLayoutInflater;

    public PreviewPagerAdapter(Context context, PreviewOptions previewOptions) {
        mContext = context;
        mPreviewOptions = previewOptions;
        mImagePreviewInfoList = mPreviewOptions.imagePreviewInfoList;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mImagePreviewInfoList != null ? mImagePreviewInfoList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentView = (View) object;
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }

    public PhotoView getPrimaryImageView() {
        return mCurrentView.findViewById(R.id.preview_pager_photo_view);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.preview_pager_item, container, false);
        final ProgressBar progressBar = view.findViewById(R.id.preview_pager_progress_bar);
        final PhotoView photoView = view.findViewById(R.id.preview_pager_photo_view);

        final PreviewInfo previewInfo = mImagePreviewInfoList.get(position);

        //判断是否设置了占位图
        if (previewInfo.getPlaceholder() != null) {
//            photoView.setImageBitmap(previewInfo.getPlaceholder());
        }

        //设置点击事件
        photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (mPreviewOptions.onClickListener != null) {
                    mPreviewOptions.onClickListener.onClick(view);
                } else {
                    ((Activity) mContext).finish();
                }
            }
        });
        //设置长按事件
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mPreviewOptions.onLongClickListener != null) {
                    return mPreviewOptions.onLongClickListener.onLongClick(view);
                } else {
                    return false;
                }
            }
        });

        //获取进度视图
        View progressView = mPreviewOptions.customProgressView;
        if (progressView == null) {
            progressView = progressBar;
        }
        //调用加载引擎加载图片
        mPreviewOptions.loaderEngine.loadImage(mPreviewOptions.context, previewInfo.getUri(),
                new BitmapDrawable(mContext.getResources(), previewInfo.getPlaceholder()), photoView, progressView);

        container.addView(view);

        return view;
    }

}
