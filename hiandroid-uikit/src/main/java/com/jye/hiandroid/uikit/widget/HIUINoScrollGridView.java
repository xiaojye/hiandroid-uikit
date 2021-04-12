package com.jye.hiandroid.uikit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 无滑动功能的GridView（解决ScrollView嵌套GridView冲突问题）
 *
 * @author jye
 * @since 1.0
 */
public class HIUINoScrollGridView extends GridView {

    public HIUINoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HIUINoScrollGridView(Context context) {
        super(context);
    }

    public HIUINoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置gridView不可滑动
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}