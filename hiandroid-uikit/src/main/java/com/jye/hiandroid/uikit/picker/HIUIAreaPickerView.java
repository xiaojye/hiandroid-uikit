package com.jye.hiandroid.uikit.picker;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;

import com.androidkun.xtablayout.XTabLayout;
import com.jye.hiandroid.uikit.R;
import com.jye.hiandroid.uikit.widget.listadapter.HIUIListAdapter;
import com.jye.hiandroid.uikit.widget.listadapter.HIUIListViewHolder;
import com.jye.hiandroid.util.HiToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 地区选择器
 *
 * @author jye
 * @since 1.0
 */
public class HIUIAreaPickerView extends FrameLayout implements AdapterView.OnItemClickListener {

    private XTabLayout mTabLayout;

    private ListView mListView;

    private TextView mReloadBtn;

    private ContentLoadingProgressBar mProgressBar;

    private AreaListAdapter mAreaListAdapter = null;

    private List<List<DataModel>> mAreasList = null;

    private SparseArray<DataModel> mSelectedMap;

    private DataProvider mDataProvider = null;

    private OnCompleteListener mOnCompleteListener = null;

    private int mLevelCount = -1;

    private boolean mIsItemClickEnable = true; //地区Item是否可点击，避免快速点击导致重复加载

    private String mLastPid = ""; //上传加载数据时的上级id参数
    private DataCallback mLastCallback = null;  //上传加载数据时的数据回调参数

    public HIUIAreaPickerView(Context context) {
        super(context);
        init(context, null);
    }

    public HIUIAreaPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HIUIAreaPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View contentView = inflate(context, R.layout.hiui_area_picker_layout, this);

        mProgressBar = contentView.findViewById(R.id.progressBar);
        mReloadBtn = contentView.findViewById(R.id.reloadBtn);
        mReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLoadData(mLastPid, mLastCallback);
            }
        });
        mReloadBtn.setVisibility(View.GONE);

        //初始化TabLayout
        mTabLayout = contentView.findViewById(R.id.tabLayout);
        mTabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                HIUIAreaPickerView.this.onTabSelectedProxy(tab);
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {
            }
        });

        //初始化ListView
        mListView = contentView.findViewById(R.id.listView);
        mListView.setOnItemClickListener(this);
        mAreaListAdapter = new AreaListAdapter(getContext());
        mListView.setAdapter(mAreaListAdapter);

        mAreasList = new ArrayList<>();
        mSelectedMap = new SparseArray<>();

        mTabLayout.addTab(mTabLayout.newTab().setText("请选择"));
    }

    private void onTabSelectedProxy(XTabLayout.Tab tab) {
        for (int i = mTabLayout.getTabCount() - 1; i >= 0; i--) {
            if (i > tab.getPosition()) {
                mTabLayout.removeTabAt(i);
                mAreasList.remove(i);
                mSelectedMap.remove(i);
            }
        }
        tab.setText("请选择");
        mSelectedMap.remove(tab.getPosition());

        mAreaListAdapter.setNewData(mAreasList.size() > tab.getPosition() ? mAreasList.get(tab.getPosition()) : null);
        mListView.smoothScrollToPositionFromTop(0, 0, 0);
    }

    private class AreaListAdapter extends HIUIListAdapter<DataModel> {

        AreaListAdapter(Context context) {
            super(context, R.layout.hiui_area_picker_item, new ArrayList<DataModel>());
        }

        void setNewData(List<DataModel> dataSource) {
            mData.clear();
            if (dataSource != null) {
                mData.addAll(dataSource);
            }
            notifyDataSetChanged();
        }

        @Override
        protected void fillItemData(HIUIListViewHolder viewHolder, int position, DataModel item) {
            viewHolder.setTextForTextView(R.id.textView, item.getName());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (!mIsItemClickEnable) {
            return;
        }

        final DataModel item = mAreaListAdapter.getItem(position);

        final int currentTabIndex = mTabLayout.getSelectedTabPosition();

        mSelectedMap.put(currentTabIndex, item);

        //当设定了选择层级时，且选择地区层级数达到了设定值时直接返回
        if (mLevelCount != -1 && mSelectedMap.size() >= mLevelCount) {
            completeAtOnce();
            return;
        }

        doLoadData(item.getId(), new DataCallback() {
            @Override
            public void onSuccess(@NonNull List<DataModel> dataList) {
                //当加载的地区列表为空时表示已经加载到最后一级了
                if (dataList.isEmpty()) {
                    completeAtOnce();
                    return;
                }

                mTabLayout.getTabAt(currentTabIndex).setText(item.getName());
                mTabLayout.addTab(mTabLayout.newTab().setText("请选择"));
                mTabLayout.getTabAt(mTabLayout.getTabCount() - 1).select();

                mAreasList.add(dataList);
                mAreaListAdapter.setNewData(dataList);
                mListView.smoothScrollToPositionFromTop(0, 0, 0);
            }

            @Override
            public void onFailure(@NonNull String errorMsg) {
                HiToastUtils.showShort(getContext(), errorMsg);
            }
        });
    }

    /**
     * 根据传入的上级id加载对应下级数据
     *
     * @param pid 上级id
     */
    private void doLoadData(String pid, final DataCallback callback) {
        mLastPid = pid;
        mLastCallback = callback;

        mIsItemClickEnable = false;
        mProgressBar.show();
        mReloadBtn.setVisibility(View.GONE);
        mDataProvider.provideData(pid, new DataCallback() {
            @Override
            public void onSuccess(@NonNull List<DataModel> areaList) {
                mProgressBar.hide();
                mReloadBtn.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mIsItemClickEnable = true;
                callback.onSuccess(areaList);
            }

            @Override
            public void onFailure(@NonNull String errorMsg) {
                mProgressBar.hide();
                mReloadBtn.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                mIsItemClickEnable = true;
                callback.onFailure(errorMsg);
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //以下为对外公开调用方法
    //----------------------------------------------------------------------------------------------

    /**
     * 数据模型
     */
    public static class DataModel {
        private String id;
        private String name;

        public DataModel(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 数据提供者
     * <p>
     * 本选择器不包含数据获取，需调用端通过设置 DataProvider 来为选择器提供数据
     * </p>
     */
    public interface DataProvider {

        /**
         * 根据传入的上级地区id提供地区数据
         *
         * @param pid          上级地区id
         * @param dataCallback 数据回调接口
         */
        void provideData(@NonNull String pid, @NonNull DataCallback dataCallback);
    }

    /**
     * 数据回调接口
     */
    public interface DataCallback {

        /**
         * 成功回调
         *
         * @param dataList 地区列表数据
         */
        void onSuccess(@NonNull List<DataModel> dataList);

        /**
         * 失败回调
         *
         * @param errorMsg 错误信息
         */
        void onFailure(@NonNull String errorMsg);
    }

    /**
     * 地区选择完成回调接口
     */
    public interface OnCompleteListener {

        /**
         * 选择完成回调
         */
        void onComplete(String[] ids, String[] names);
    }

    /**
     * 设置数据提供者并初始加载1级地区列表
     *
     * @param dataProvider 数据提供者实现
     */
    public void setDataProvider(@NonNull DataProvider dataProvider) {
        setDataProvider(-1, dataProvider);
    }

    /**
     * 设置数据提供者并初始加载1级地区列表
     *
     * @param levelCount   地区选择层级数(-1表示无限级)
     * @param dataProvider 数据提供者实现
     */
    public void setDataProvider(int levelCount, @NonNull DataProvider dataProvider) {
        mDataProvider = dataProvider;
        mLevelCount = levelCount;

        //加载1级地区列表
        doLoadData("", new DataCallback() {
            @Override
            public void onSuccess(@NonNull List<DataModel> dataList) {
                mAreasList.clear();
                mAreasList.add(dataList);
                mAreaListAdapter.setNewData(dataList);
                mListView.smoothScrollToPositionFromTop(0, 0, 0);
            }

            @Override
            public void onFailure(@NonNull String errorMsg) {
                HiToastUtils.showShort(getContext(), errorMsg);
            }
        });
    }

    /**
     * 设置地区选择完成监听
     *
     * @param onCompleteListener 地区选择完成回调接口
     */
    public void setOnCompleteListener(@NonNull OnCompleteListener onCompleteListener) {
        mOnCompleteListener = onCompleteListener;
    }

    /**
     * 立即完成（调用选择完成回调函数）
     */
    public void completeAtOnce() {
        if (mOnCompleteListener != null) {
            int selectedCount = mSelectedMap.size();
            String[] ids = new String[selectedCount];
            String[] names = new String[selectedCount];
            for (int i = 0; i < selectedCount; i++) {
                DataModel entity = mSelectedMap.valueAt(i);
                ids[i] = entity.getId();
                names[i] = entity.getName();
            }
            mOnCompleteListener.onComplete(ids, names);
        }
    }

}
