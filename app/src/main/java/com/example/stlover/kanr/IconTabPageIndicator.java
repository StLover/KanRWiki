package com.example.stlover.kanr;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;

/**
 * Created by StLover on 2016/3/28.
 */
public class IconTabPageIndicator extends LinearLayout implements PageIndicator {

    private static final CharSequence EMPTY_TITLE = "";

    private final View.OnClickListener mTabClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView) view;
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected,false);
        }
    };
    private final LinearLayout mTabLayout;

    private ViewPager mViewPager;
    private  ViewPager.OnPageChangeListener mListener;

    private int mSelectedTabIndex;

    private int mTabWidth;

    public IconTabPageIndicator(Context context) {
        this(context,null);
    }

    public IconTabPageIndicator(Context context,AttributeSet attrs) {
        super(context,attrs);
        setHorizontalScrollBarEnabled(false);

        mTabLayout = new LinearLayout(context,null,R.attr.tabPageIndicator);
        this.addView(mTabLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onMeasure(int widthMesureSpec,int heightMesureSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthMesureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;

        final int childCount = mTabLayout.getChildCount();
        if(childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            mTabWidth = MeasureSpec.getSize(widthMesureSpec) / childCount;
        } else {
            mTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMesureSpec,heightMesureSpec);
        final int newWidth = getMeasuredWidth();

        if(lockedExpanded && oldWidth != newWidth) {
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void addTab(int index,CharSequence text,int iconResId) {
        final TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setOnClickListener((mTabClickListener));
        tabView.setText(text);

        if(iconResId > 0) {
            tabView.setIcon(iconResId);
        }
        mTabLayout.addView(tabView,new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
    }

    @Override
    public void setViewPager(ViewPager view) {
        if(mViewPager == view) {
            return ;
        }
        if(mViewPager != null) {
            mViewPager.addOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if(adapter == null) {
            throw new IllegalStateException("ViewPager no adapter");
        }
        mViewPager = view;
        view.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if(mViewPager == null) {
            throw new IllegalStateException("ViewPager no bound");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item, false);

        final int tabCount = mTabLayout.getChildCount();
        for(int i = 0;i < tabCount;i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        IconPagerAdapter iconPagerAdapter = (IconPagerAdapter) mViewPager.getAdapter();
        if(adapter instanceof  IconPagerAdapter) {
            iconPagerAdapter = (IconPagerAdapter) adapter;
        }
        final int count = adapter.getCount();
        for(int i = 0;i < count;i++) {
            CharSequence title = adapter.getPageTitle(i);
            if(title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if(iconPagerAdapter != null) {
                iconResId = iconPagerAdapter.getIconResId(i);
            }
            addTab(i,title,iconResId);
        }
        if(mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(mListener != null) {
            mListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if(mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }


    private class TabView extends LinearLayout {
        private int mIndex;
        private ImageView mImageView;
        private TextView mTextView;

        public TabView(Context context) {
            super(context,null,R.attr.tabView);
            View view = View.inflate(context,R.layout.tab_view,null);
            mImageView = (ImageView)view.findViewById(R.id.tab_image);
            mTextView = (TextView)view.findViewById(R.id.tab_text);
            this.addView(view);
        }

        @Override
        public void onMeasure(int width,int height) {
            super.onMeasure(width, height);

            if(mTabWidth > 0) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mTabWidth,MeasureSpec.EXACTLY),height);
            }
        }

        public void setText (CharSequence text) {
            mTextView.setText(text);
        }

        public void setIcon(int resId) {
            if (resId > 0) {
                mImageView.setImageResource(resId);
            }
        }

        public int getIndex() {
            return mIndex;
        }
    }
}

