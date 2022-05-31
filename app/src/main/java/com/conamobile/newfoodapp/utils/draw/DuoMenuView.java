package com.conamobile.newfoodapp.utils.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.conamobile.newfoodapp.R;

public class DuoMenuView extends RelativeLayout {
    static final String TAG_FOOTER = "footer";
    private static final String TAG_HEADER = "header";
    public AppCompatTextView accountBtn;
    public AppCompatTextView orderBtn;

    @SuppressLint("ResourceType")
    @DrawableRes
    private static final int DEFAULT_DRAWABLE_ATTRIBUTE_VALUE = 0b11111111111111110010101111001111;
    @SuppressLint("ResourceType")
    @LayoutRes
    private static final int DEFAULT_LAYOUT_ATTRIBUTE_VALUE = 0b11111111111111110010101111010000;

    @DrawableRes
    private int mBackgroundDrawableId;
    @LayoutRes
    private int mHeaderViewId;
    @LayoutRes
    private int mFooterViewId;

    private OnMenuClickListener mOnMenuClickListener;
    private DataSetObserver mDataSetObserver;
    private MenuViewHolder mMenuViewHolder;
    private LayoutInflater mLayoutInflater;
    private Adapter mAdapter;

    public DuoMenuView(Context context) {
        this(context, null);
    }

    public DuoMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DuoMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttributes(attrs);
        initialize();
    }

    private void readAttributes(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.DuoMenuView);

        try {
            mBackgroundDrawableId = typedArray.getResourceId(R.styleable.DuoMenuView_background, DEFAULT_DRAWABLE_ATTRIBUTE_VALUE);
            mHeaderViewId = typedArray.getResourceId(R.styleable.DuoMenuView_header, DEFAULT_LAYOUT_ATTRIBUTE_VALUE);
            mFooterViewId = typedArray.getResourceId(R.styleable.DuoMenuView_footer, DEFAULT_LAYOUT_ATTRIBUTE_VALUE);
        } finally {
            typedArray.recycle();
        }
    }

    private void initialize() {
        ViewGroup rootView = (ViewGroup) inflate(getContext(), R.layout.duo_view_menu, this);

        mMenuViewHolder = new MenuViewHolder(rootView);
        mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        handleOptions();
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                postInvalidate();
                requestLayout();
            }
        };

        handleBackground();
        handleHeader();
        handleFooter();
    }

    private void handleBackground() {
        if (mMenuViewHolder.mMenuBackground == null) {
            return;
        }

        if (mBackgroundDrawableId != DEFAULT_DRAWABLE_ATTRIBUTE_VALUE) {
            Drawable backgroundDrawable = ContextCompat.getDrawable(getContext(), mBackgroundDrawableId);

            if (backgroundDrawable != null) {
                mMenuViewHolder.mMenuBackground.setImageDrawable(backgroundDrawable);
                return;
            }
        }

        mMenuViewHolder.mMenuBackground.setBackgroundColor(getPrimaryColor());
    }

    private void handleHeader() {
        if (mHeaderViewId == DEFAULT_LAYOUT_ATTRIBUTE_VALUE || mMenuViewHolder.mMenuHeader == null) {
            return;
        }

        View view = mLayoutInflater.inflate(mHeaderViewId, null, false);

        if (view != null) {
            if (mMenuViewHolder.mMenuHeader.getChildCount() > 0) {
                mMenuViewHolder.mMenuHeader.removeAllViews();
            }

            mMenuViewHolder.mMenuHeader.addView(view);
            view.setTag(TAG_HEADER);
            view.bringToFront();
            view.setOnClickListener(v -> {
                if (mOnMenuClickListener != null) {
                    mOnMenuClickListener.onHeaderClicked();
                }
            });
        }
    }

    private void handleFooter() {
        if (mFooterViewId == DEFAULT_LAYOUT_ATTRIBUTE_VALUE || mMenuViewHolder.mMenuFooter == null) {
            return;
        }

        View view = mLayoutInflater.inflate(mFooterViewId, null, false);

        if (view != null) {
            if (mMenuViewHolder.mMenuFooter.getChildCount() > 0) {
                mMenuViewHolder.mMenuFooter.removeAllViews();
            }

            mMenuViewHolder.mMenuFooter.addView(view);
            view.setTag(TAG_FOOTER);
            view.bringToFront();

            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;

                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof Button) {
                        viewGroup.getChildAt(i).setOnClickListener(v -> {
                            if (mOnMenuClickListener != null) {
                                mOnMenuClickListener.onFooterClicked();
                            }
                        });
                        return;
                    }
                }
            }
        }
    }

    private void handleOptions() {

        AppCompatTextView btn1;
        AppCompatTextView btn2;
        btn1 = findViewById(R.id.profileBtn);
        btn2 = findViewById(R.id.orderBtn);
        accountBtn = btn1;
        orderBtn = btn2;

        if (mAdapter == null || mAdapter.isEmpty() || mMenuViewHolder.mMenuOptions == null) {
            return;
        }

        if (mMenuViewHolder.mMenuOptions.getChildCount() > 0) {
            mMenuViewHolder.mMenuOptions.removeAllViews();
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            final int index = i;

            View optionView = mAdapter.getView(i, null, this);

            if (optionView != null) {
                mMenuViewHolder.mMenuOptions.addView(optionView);
                optionView.setOnClickListener(v -> {
                    if (mOnMenuClickListener != null) {
                        mOnMenuClickListener.onOptionClicked(index, mAdapter.getItem(index));
                    }
                });
            }
        }
    }

    private int getPrimaryColor() {
        TypedArray typedArray = getContext().obtainStyledAttributes(new TypedValue().data, new int[]{androidx.appcompat.R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        return color;
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }

    public View getHeaderView() {
        return findViewWithTag(TAG_HEADER);
    }

    public void setHeaderView(@LayoutRes int headerViewId) {
        mHeaderViewId = headerViewId;
        handleHeader();
    }

    public View getFooterView() {
        return findViewWithTag(TAG_FOOTER);
    }

    public void setFooterView(@LayoutRes int footerViewId) {
        mFooterViewId = footerViewId;
        handleFooter();
    }

    public void setBackground(@DrawableRes int backgroundDrawableId) {
        mBackgroundDrawableId = backgroundDrawableId;
        handleBackground();
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) mAdapter.unregisterDataSetObserver(mDataSetObserver);
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mDataSetObserver);
        handleOptions();
    }

    private void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof Toolbar) {
                    setViewAndChildrenEnabled(child, true);
                } else {
                    setViewAndChildrenEnabled(child, enabled);
                }
            }
        }
    }

    private static class MenuViewHolder {
        private final LinearLayout mMenuOptions;
        private final ImageView mMenuBackground;
        private final ViewGroup mMenuHeader;
        private final ViewGroup mMenuFooter;

        @SuppressLint("CutPasteId")
        MenuViewHolder(ViewGroup rootView) {
            this.mMenuOptions = (LinearLayout) rootView.findViewById(R.id.duo_view_menu_options_layout);
            this.mMenuBackground = (ImageView) rootView.findViewById(R.id.duo_view_menu_background);
            this.mMenuHeader = (ViewGroup) rootView.findViewById(R.id.duo_view_menu_header_layout);
            this.mMenuFooter = (ViewGroup) rootView.findViewById(R.id.duo_view_menu_footer_layout);
        }
    }

    public interface OnMenuClickListener {

        void onFooterClicked();

        void onHeaderClicked();

        void onOptionClicked(int position, Object objectClicked);
    }
}
