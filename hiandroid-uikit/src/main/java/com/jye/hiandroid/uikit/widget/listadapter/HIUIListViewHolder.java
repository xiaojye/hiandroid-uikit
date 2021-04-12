package com.jye.hiandroid.uikit.widget.listadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 * <p>
 * 这是一个通用的ViewHolder, 将会装载AbsListView子类的item View, 并且将item
 * view中的子视图进行缓存和索引，使得用户能够方便的获取这些子view, 减少了代码重复。
 *
 * @author jye
 * @since 1.0
 */
public class HIUIListViewHolder {
    
    private View mContentView;

    /**
     * 构造函数
     *
     * @param context  Context
     * @param layoutId ListView、GridView或者其他AbsListVew子类的 Item View的资源布局id
     */
    protected HIUIListViewHolder(Context context, ViewGroup parent, int layoutId) {
        mContentView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mContentView.setTag(this);
    }

    /**
     * 获取CommonViewHolder，当convertView为空的时候从布局xml装载item view,
     * 并且将该CommonViewHolder设置为convertView的tag, 便于复用convertView.
     *
     * @param context     Context
     * @param convertView Item view
     * @param layoutId    布局资源id, 例如R.layout.my_listview_item.
     * @return 通用的CommonViewHolder实例
     */
    public static HIUIListViewHolder getViewHolder(Context context, View convertView, ViewGroup parent, int layoutId) {

        context = (context == null && parent != null) ? parent.getContext() : context;
        HIUIListViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new HIUIListViewHolder(context, parent, layoutId);
        } else {
            viewHolder = (HIUIListViewHolder) convertView.getTag();
        }
        // 将当前item view设置为ViewFinder要查找的root view, 这一步不能搞错，否则查找不到对象的view
        HIUIListViewFinder.initContentView(viewHolder.getContentView());

        return viewHolder;
    }

    /**
     * @return 当前项的convertView, 在构造函数中装载
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * 为id为textViewId的TextView设置文本内容
     *
     * @param textViewId 视图id
     * @param text       要设置的文本内容
     */
    public void setTextForTextView(int textViewId, CharSequence text) {
        TextView textView = HIUIListViewFinder.findViewById(mContentView, textViewId);
        if (textView != null) {
            textView.setText(text);
        }
    }

    /**
     * 为ImageView设置图片
     *
     * @param imageViewId ImageView的id, 例如R.id.my_imageview
     * @param drawableId  Drawable图片的id, 例如R.drawable.my_photo
     */
    public void setImageForView(int imageViewId, int drawableId) {
        ImageView imageView = HIUIListViewFinder.findViewById(mContentView, imageViewId);
        if (imageView != null) {
            imageView.setImageResource(drawableId);
        }
    }

    /**
     * 为ImageView设置图片
     *
     * @param imageViewId ImageView的id, 例如R.id.my_imageview
     * @param bmp         Bitmap图片
     */
    public void setImageForView(int imageViewId, Bitmap bmp) {
        ImageView imageView = HIUIListViewFinder.findViewById(mContentView, imageViewId);
        if (imageView != null) {
            imageView.setImageBitmap(bmp);
        }
    }

    /**
     * 为CheckBox设置是否选中
     *
     * @param checkViewId CheckBox的id
     * @param isCheck     是否选中
     */
    public void setCheckForCheckBox(int checkViewId, boolean isCheck) {
        CheckBox checkBox = HIUIListViewFinder.findViewById(mContentView, checkViewId);
        if (checkBox != null) {
            checkBox.setChecked(isCheck);
        }
    }

    /**
     * @param viewId
     * @param visibility
     */
    public void setVisibility(int viewId, int visibility) {
        View view = HIUIListViewFinder.findViewById(mContentView, viewId);
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    /**
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, OnClickListener listener) {
        View view = HIUIListViewFinder.findViewById(mContentView, viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    /**
     * @param viewId
     * @param listener
     */
    public void setOnTouchListener(int viewId, OnTouchListener listener) {
        View view = HIUIListViewFinder.findViewById(mContentView, viewId);
        if (view != null) {
            view.setOnTouchListener(listener);
        }
    }

    public void setBackgroundColor(int viewId, int color) {
        View view = HIUIListViewFinder.findViewById(mContentView, viewId);
        if (view != null) {
            view.setBackgroundColor(color);
        }
    }
}