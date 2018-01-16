/*
 * Copyright (C) 2012 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viewpagerindicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 圆角线PageIndicator
 * Draws a line for each page. The current page line is colored differently
 * than the unselected page lines.
 */
public class RoundLinePageIndicator extends View implements PageIndicator {
    private static final int INVALID_POINTER = -1;

    private final Paint mPaintUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPaintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPaintGradient = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;
    private int mCurrentPage;
    private boolean mCentered;
    private float mLineWidth;
    /**
     * 选中的线宽度
     */
    private float mSelectedLineWidth;
    private float mGapWidth;
    /**
     * 线高，即圆角直径
     */
    private float mLineHeight;

    private float mLastMotionX = -1;
    private int mActivePointerId = INVALID_POINTER;
    private boolean mIsDragging;
    private int mTouchSlop;

    private int mStartColor;
    private int mEndColor;


    public RoundLinePageIndicator(Context context) {
        this(context, null);
    }

    public RoundLinePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiCirclePageIndicatorStyle);
    }

    public RoundLinePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;

        final Resources res = getResources();

        //Load defaults from resources
        final int defaultSelectedColor = res.getColor(R.color
                .default_line_indicator_selected_color);
        final int defaultUnselectedColor = res.getColor(R.color
                .default_line_indicator_unselected_color);
        final float defaultLineWidth = res.getDimension(R.dimen.default_line_indicator_line_width);
        final float defaultSelectedLineWidth = res.getDimension(R.dimen
                .default_line_indicator_line_width);
        final float defaultGapWidth = res.getDimension(R.dimen.default_line_indicator_gap_width);
        final float defaultStrokeWidth = res.getDimension(R.dimen
                .default_line_indicator_stroke_width);
        final boolean defaultCentered = res.getBoolean(R.bool.default_line_indicator_centered);

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundLinePageIndicator,
                defStyle, 0);

        mCentered = a.getBoolean(R.styleable.RoundLinePageIndicator_rl_vpi_centered,
                defaultCentered);
        mLineWidth = a.getDimension(R.styleable.RoundLinePageIndicator_rl_vpi_lineWidth,
                defaultLineWidth);
        mSelectedLineWidth = a.getDimension(R.styleable
                        .RoundLinePageIndicator_rl_vpi_selectedLineWidth,
                defaultSelectedLineWidth);
        mGapWidth = a.getDimension(R.styleable.RoundLinePageIndicator_rl_vpi_gapWidth,
                defaultGapWidth);
        mLineHeight = a.getDimension(R.styleable.RoundLinePageIndicator_rl_vpi_strokeWidth,
                defaultStrokeWidth);

        mPaintUnselected.setStyle(Paint.Style.FILL);
        mEndColor = a.getColor(R.styleable.RoundLinePageIndicator_rl_vpi_unselectedColor,
                defaultUnselectedColor);
        mPaintUnselected.setColor(mEndColor);

        mPaintSelected.setStyle(Paint.Style.FILL);
        mStartColor = a.getColor(R.styleable.RoundLinePageIndicator_rl_vpi_selectedColor,
                defaultSelectedColor);
        mPaintSelected.setColor(mStartColor);

        mPaintGradient.setStyle(Paint.Style.FILL);

        a.recycle();

        if (mLineWidth >= mSelectedLineWidth) {
            mSelectedLineWidth = mLineWidth;
        }
        if (mLineWidth < mLineHeight) {
            mLineHeight = mLineWidth;
        }

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }


    public void setCentered(boolean centered) {
        mCentered = centered;
        invalidate();
    }

    public boolean isCentered() {
        return mCentered;
    }

    public void setUnselectedColor(int unselectedColor) {
        mPaintUnselected.setColor(unselectedColor);
        invalidate();
    }

    public int getUnselectedColor() {
        return mPaintUnselected.getColor();
    }

    public void setSelectedColor(int selectedColor) {
        mPaintSelected.setColor(selectedColor);
        invalidate();
    }

    public int getSelectedColor() {
        return mPaintSelected.getColor();
    }

    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
        invalidate();
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public float getmSelectedLineWidth() {
        return mSelectedLineWidth;
    }

    public void setmSelectedLineWidth(float mSelectedLineWidth) {
        this.mSelectedLineWidth = mSelectedLineWidth;

        if (mLineWidth >= mSelectedLineWidth) {
            this.mSelectedLineWidth = mLineWidth;
        }
        invalidate();
    }

    public void setStrokeWidth(float lineHeight) {
        mLineHeight = lineHeight;
        if (mLineWidth < mLineHeight) {
            mLineHeight = mLineWidth;
        }
        invalidate();
    }

    public float getStrokeWidth() {
        return mLineHeight;
    }

    public void setGapWidth(float gapWidth) {
        mGapWidth = gapWidth;
        invalidate();
    }

    public float getGapWidth() {
        return mGapWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null) {
            return;
        }
        final int count = mViewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }

        // 只有一个时，隐藏
        if (count == 1) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        final float lineWidthAndGap = mLineWidth + mGapWidth;
        final float indicatorWidth = (count - 1) * lineWidthAndGap + mSelectedLineWidth;
        final float paddingTop = getPaddingTop();
        final float paddingLeft = getPaddingLeft();
        final float paddingRight = getPaddingRight();

        float horizontalOffset = paddingLeft;
        if (mCentered) {
            horizontalOffset += ((getWidth() - paddingLeft - paddingRight) / 2.0f) -
                    (indicatorWidth / 2.0f);
        }

        float curLeft = horizontalOffset;
        float radius = mLineHeight / 2.0f;

        Log.d("onDraw", "curPosition=" + curPosition + ", positionOffset="
                + positionOffset + ", lastPositionOffset=" + lastPositionOffset +
                ", positionOffset=" + positionOffset);
        if (positionOffset == 0f) {
            // 选中项之前的
            for (int i = 0; i < curPosition; i++) {
                float dx2 = curLeft + mLineWidth;
                RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                canvas.drawRoundRect(rect, radius, radius, mPaintUnselected);
                curLeft += lineWidthAndGap;
            }

            // 选中项
            if (curPosition < count) {
                float dx2 = curLeft + mSelectedLineWidth;
                RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                canvas.drawRoundRect(rect, radius, radius, mPaintSelected);
                curLeft = dx2 + mGapWidth;
            }

            // 选中项之后的
            for (int i = curPosition + 1; i < count; i++) {
                float dx2 = curLeft + mLineWidth;
                RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                canvas.drawRoundRect(rect, radius, radius, mPaintUnselected);
                curLeft += lineWidthAndGap;
            }
        } else {
            // 绘制变化过程
            boolean isBack = lastPositionOffset > positionOffset; // 向前滑

            if (isBack) {
                curPosition++;
                // 选中项之前的
                for (int i = 0; i < curPosition - 1; i++) {
                    float dx2 = curLeft + mLineWidth;
                    RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                    canvas.drawRoundRect(rect, radius, radius, mPaintUnselected);
                    curLeft += lineWidthAndGap;
                }

                float detal = (mSelectedLineWidth - mLineWidth) * positionOffset; // 当前项与未选中项的长度差值

                // 变化项
                if (curPosition - 1 < count) {
                    float dx2 = curLeft + mSelectedLineWidth - detal;
                    RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                    mPaintGradient.setColor(getColor(positionOffset));
                    canvas.drawRoundRect(rect, radius, radius, mPaintGradient);
                    curLeft = dx2 + mGapWidth;
                }

                // 选中项
                if (curPosition < count) {
                    float dx2 = curLeft + mLineWidth + detal;
                    RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                    mPaintGradient.setColor(getColor(1f - positionOffset));
                    canvas.drawRoundRect(rect, radius, radius, mPaintGradient);
                    curLeft = dx2 + mGapWidth;
                }

                // 选中项之后的
                for (int i = curPosition + 1; i < count; i++) {
                    float dx2 = curLeft + mLineWidth;
                    RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                    canvas.drawRoundRect(rect, radius, radius, mPaintUnselected);
                    curLeft += lineWidthAndGap;
                }
            } else {
                // 选中项之前的
                for (int i = 0; i < curPosition; i++) {
                    float dx2 = curLeft + mLineWidth;
                    RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                    canvas.drawRoundRect(rect, radius, radius, mPaintUnselected);
                    curLeft += lineWidthAndGap;
                }

                // 未选中项与当前项与未选中项的长度差值
                float detal = (mSelectedLineWidth - mLineWidth) * positionOffset;

                // 选中项
                if (curPosition < count) {
                    float dx2 = curLeft + mSelectedLineWidth - detal;
                    RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                    mPaintGradient.setColor(getColor(positionOffset));
                    canvas.drawRoundRect(rect, radius, radius, mPaintGradient);
                    curLeft = dx2 + mGapWidth;
                }

                // 变化项
                if (curPosition + 1 < count) {
                    float dx2 = curLeft + mLineWidth + detal;
                    RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                    mPaintGradient.setColor(getColor(1f - positionOffset));
                    canvas.drawRoundRect(rect, radius, radius, mPaintGradient);
                    curLeft = dx2 + mGapWidth;
                }

                // 选中项之后的
                for (int i = curPosition + 2; i < count; i++) {
                    float dx2 = curLeft + mLineWidth;
                    RectF rect = new RectF(curLeft, paddingTop, dx2, paddingTop + mLineHeight);
                    canvas.drawRoundRect(rect, radius, radius, mPaintUnselected);
                    curLeft += lineWidthAndGap;
                }
            }
        }
    }

    // 获取某一个百分比间的颜色,radio取值[0,1]
    public int getColor(float radio) {
        int redStart = Color.red(mStartColor);
        int blueStart = Color.blue(mStartColor);
        int greenStart = Color.green(mStartColor);
        int aStrart = Color.alpha(mStartColor);
        int redEnd = Color.red(mEndColor);
        int blueEnd = Color.blue(mEndColor);
        int greenEnd = Color.green(mEndColor);
        int aEnd = Color.alpha(mEndColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        int a = (int) (aStrart + ((aEnd - aStrart) * radio + 0.5));
        return Color.argb(a, red, greed, blue);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (super.onTouchEvent(ev)) {
            return true;
        }
        if ((mViewPager == null) || (mViewPager.getAdapter().getCount() == 0)) {
            return false;
        }

        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mLastMotionX = ev.getX();
                break;

            case MotionEvent.ACTION_MOVE: {
                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev,
                        mActivePointerId);
                final float x = MotionEventCompat.getX(ev, activePointerIndex);
                final float deltaX = x - mLastMotionX;

                if (!mIsDragging) {
                    if (Math.abs(deltaX) > mTouchSlop) {
                        mIsDragging = true;
                    }
                }

                if (mIsDragging) {
                    mLastMotionX = x;
                    if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
                        mViewPager.fakeDragBy(deltaX);
                    }
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!mIsDragging) {
                    final int count = mViewPager.getAdapter().getCount();
                    final int width = getWidth();
                    final float halfWidth = width / 2f;
                    final float sixthWidth = width / 6f;

                    if ((mCurrentPage > 0) && (ev.getX() < halfWidth - sixthWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage - 1);
                        }
                        return true;
                    } else if ((mCurrentPage < count - 1) && (ev.getX() > halfWidth + sixthWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage + 1);
                        }
                        return true;
                    }
                }

                mIsDragging = false;
                mActivePointerId = INVALID_POINTER;
                if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
                break;

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mLastMotionX = MotionEventCompat.getX(ev, index);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev,
                        mActivePointerId));
                break;
        }

        return true;
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }
        if (mViewPager != null) {
            //Clear us from the old pager.
            mViewPager.setOnPageChangeListener(null);
        }
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        Log.d("setCurrentItem", "item=" + item);
        invalidate();
    }

    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }

    private float positionOffset = 0f;
    private float lastPositionOffset = 0f;
    // 当前第一页可见的下标
    private int curPosition = 0;

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        Log.d("onPageScrolled", "position=" + position + ", positionOffset="
                + positionOffset + ", positionOffsetPixels=" + positionOffsetPixels);

        lastPositionOffset = this.positionOffset;
        // 去除精度影响
        if (positionOffset > 0.99f) {
            this.positionOffset = 1f;
        } else if (positionOffset < 0.01f) {
            this.positionOffset = 0f;
        } else {
            this.positionOffset = positionOffset;
        }
        this.curPosition = position;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {

        Log.d("onPageSelected", "position=" + position);
        mCurrentPage = position;
        invalidate();

        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        float result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
            //We were told how big to be
            result = specSize;
        } else {
            //Calculate the width according the views count
            final int count = mViewPager.getAdapter().getCount();
            result = getPaddingLeft() + getPaddingRight() + ((count - 1) * mLineWidth) + ((count -
                    1) * mGapWidth) + mSelectedLineWidth;
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) Math.ceil(result);
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        float result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the height
            result = mLineHeight + getPaddingTop() + getPaddingBottom();
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) Math.ceil(result);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }

        @SuppressWarnings("UnusedDeclaration")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}