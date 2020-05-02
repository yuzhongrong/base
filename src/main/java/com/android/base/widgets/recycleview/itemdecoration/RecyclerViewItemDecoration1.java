package com.android.base.widgets.recycleview.itemdecoration;

import android.content.Context;
import android.graphics.*;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.regex.Pattern;


public class RecyclerViewItemDecoration1 extends RecyclerView.ItemDecoration {


    public static final int MODE_HORIZONTAL = 0;
    public static final int MODE_VERTICAL = 1;
    public static final int MODE_GRID = 2;


    private static final String DEFAULT_COLOR = "#cccccc";


    private int mDrawableRid = 0;

    private int mColor = Color.parseColor(DEFAULT_COLOR);

    private int mThickness;

    private int mDashWidth = 0;

    private int mDashGap = 0;
    private boolean mFirstLineVisible;
    private boolean mLastLineVisible;
    private int mPaddingStart = 0;
    private int mPaddingEnd = 0;

    private int mMode;

    private Paint mPaint;

    private Bitmap mBmp;
    private NinePatch mNinePatch;

    private int mCurrentThickness;

    private Boolean hasNinePatch = false;

    public RecyclerViewItemDecoration1() {
    }

    @Deprecated
    public RecyclerViewItemDecoration1(int recyclerviewMode, Context context, int drawableRid) {
        this.mMode = recyclerviewMode;
        this.mDrawableRid = drawableRid;

        this.mBmp = BitmapFactory.decodeResource(context.getResources(), drawableRid);
        if (mBmp.getNinePatchChunk() != null) {
            hasNinePatch = true;
            mNinePatch = new NinePatch(mBmp, mBmp.getNinePatchChunk(), null);
        }
        initPaint();

    }

    @Deprecated
    public RecyclerViewItemDecoration1(int recyclerviewMode, int color, int thick, int dashWidth, int dashGap) {
        this.mMode = recyclerviewMode;
        this.mColor = color;
        this.mThickness = thick;
        this.mDashWidth = dashWidth;
        this.mDashGap = dashGap;

        initPaint();

    }

    @Deprecated
    public RecyclerViewItemDecoration1(int recyclerviewMode, String color, int thick, int dashWidth, int dashGap) {
        this.mMode = recyclerviewMode;
        if (isColorString(color)) {
            this.mColor = Color.parseColor(color);
        } else {
            this.mColor = Color.parseColor(DEFAULT_COLOR);
        }
        this.mThickness = thick;
        this.mDashWidth = dashWidth;
        this.mDashGap = dashGap;

        initPaint();
    }

    public void setParams(Context context, Param params) {

        this.mMode = params.mode;
        this.mDrawableRid = params.drawableRid;
        this.mColor = params.color;
        this.mThickness = params.thickness;
        this.mDashGap = params.dashGap;
        this.mDashWidth = params.dashWidth;
        this.mPaddingStart = params.paddingStart;
        this.mPaddingEnd = params.paddingEnd;
        this.mFirstLineVisible = params.firstLineVisible;
        this.mLastLineVisible = params.lastLineVisible;

        this.mBmp = BitmapFactory.decodeResource(context.getResources(), mDrawableRid);
        if (mBmp != null) {

            if (mBmp.getNinePatchChunk() != null) {
                hasNinePatch = true;
                mNinePatch = new NinePatch(mBmp, mBmp.getNinePatchChunk(), null);
            }

            if (mMode == MODE_HORIZONTAL)
                mCurrentThickness = mThickness == 0 ? mBmp.getHeight() : mThickness;
            if (mMode == MODE_VERTICAL)
                mCurrentThickness = mThickness == 0 ? mBmp.getWidth() : mThickness;
        }

        initPaint();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mThickness);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        mPaint.setColor(mColor);
        if (mMode == MODE_HORIZONTAL) {
            drawHorinzonal(c, parent);
        } else if (mMode == MODE_VERTICAL) {
            drawVertical(c, parent);
        } else if (mMode == MODE_GRID) {
            drawGrid(c, parent);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (mMode == MODE_HORIZONTAL) {

            if (!(!mLastLineVisible &&
                    parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount() - 1)) {
                if (mDrawableRid != 0) {
                    outRect.set(0, 0, 0, mCurrentThickness);
                } else {
                    outRect.set(0, 0, 0, mThickness);
                }
            }

            if (mFirstLineVisible && parent.getChildLayoutPosition(view) == 0) {
                if (mDrawableRid != 0) {
                    outRect.set(0, mCurrentThickness, 0, mCurrentThickness);
                } else {
                    outRect.set(0, mThickness, 0, mThickness);
                }
            }

        } else if (mMode == MODE_VERTICAL) {
            if (!(!mLastLineVisible &&
                    parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount() - 1)) {
                if (mDrawableRid != 0) {
                    outRect.set(0, 0, mCurrentThickness, 0);
                } else {
                    outRect.set(0, 0, mThickness, 0);
                }
            }
            if (mFirstLineVisible && parent.getChildLayoutPosition(view) == 0) {
                if (mDrawableRid != 0) {
                    outRect.set(mCurrentThickness, 0, mCurrentThickness, 0);
                } else {
                    outRect.set(mThickness, 0, mThickness, 0);
                }
            }

        } else if (mMode == MODE_GRID) {
            int columnSize = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            int itemSzie = parent.getAdapter().getItemCount();
            if (mDrawableRid != 0) {
                if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)
                        && isLastGridColumn(parent.getChildLayoutPosition(view), columnSize)) {
                    outRect.set(0, 0, 0, 0);
                } else if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)) {
                    outRect.set(0, 0, mBmp.getWidth(), 0);
                } else if ((parent.getChildLayoutPosition(view) + 1) % columnSize != 0) {
                    outRect.set(0, 0, mBmp.getWidth(), mBmp.getHeight());
                } else {
                    outRect.set(0, 0, 0, mBmp.getHeight());
                }
            } else {
                if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)
                        && isLastGridColumn(parent.getChildLayoutPosition(view), columnSize)) {
                    outRect.set(0, 0, 0, 0);
                } else if (isLastRowGrid(parent.getChildLayoutPosition(view), itemSzie, columnSize)) {
                    outRect.set(0, 0, mThickness, 0);
                } else if ((parent.getChildLayoutPosition(view) + 1) % columnSize != 0) {
                    outRect.set(0, 0, mThickness, mThickness);
                } else {
                    outRect.set(0, 0, 0, mThickness);
                }

            }
        }

    }


    public static boolean isColorString(String colorStr) {
        return Pattern.matches("^#([0-9a-fA-F]{6}||[0-9a-fA-F]{8})$", colorStr);
    }

    private boolean isPureLine() {
        if (mDashGap == 0 && mDashWidth == 0)
            return true;
        return false;
    }


    private void drawHorinzonal(Canvas c, RecyclerView parent) {
        int childrentCount = parent.getChildCount();

        if (mDrawableRid != 0) {

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                int myY = childView.getTop();

                if (hasNinePatch) {
                    Rect rect = new Rect(mPaddingStart, myY - mCurrentThickness, parent.getWidth() - mPaddingEnd, myY);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, mPaddingStart, myY - mCurrentThickness, mPaint);
                }
            }

            for (int i = 0; i < childrentCount; i++) {
                if (!mLastLineVisible && i == childrentCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                int myY = childView.getBottom();

                if (hasNinePatch) {
                    Rect rect = new Rect(mPaddingStart, myY, parent.getWidth() - mPaddingEnd, myY + mCurrentThickness);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, mPaddingStart, myY, mPaint);
                }

            }

        } else {

            boolean isPureLine = isPureLine();
            if (!isPureLine) {
                PathEffect effects = new DashPathEffect(new float[]{0, 0, mDashWidth, mThickness}, mDashGap);
                mPaint.setPathEffect(effects);
            }

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                int myY = childView.getTop() - mThickness/ 2 ;

                if (isPureLine) {
                    c.drawLine(mPaddingStart, myY, parent.getWidth() - mPaddingEnd, myY, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(mPaddingStart, myY);
                    path.lineTo(parent.getWidth() - mPaddingEnd, myY);
                    c.drawPath(path, mPaint);
                }
            }

            for (int i = 0; i < childrentCount; i++) {
                if (!mLastLineVisible && i == childrentCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                int myY = childView.getBottom() + mThickness / 2;

                if (isPureLine) {
                    c.drawLine(mPaddingStart, myY, parent.getWidth() - mPaddingEnd, myY, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(mPaddingStart, myY);
                    path.lineTo(parent.getWidth() - mPaddingEnd, myY);
                    c.drawPath(path, mPaint);
                }

            }

        }
    }


    private void drawVertical(Canvas c, RecyclerView parent) {
        int childrentCount = parent.getChildCount();
        if (mDrawableRid != 0) {

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                int myX = childView.getLeft();
                if (hasNinePatch) {
                    Rect rect = new Rect(myX - mCurrentThickness, mPaddingStart, myX, parent.getHeight() - mPaddingEnd);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, myX - mCurrentThickness, mPaddingStart, mPaint);
                }
            }
            for (int i = 0; i < childrentCount; i++) {
                if (!mLastLineVisible && i == childrentCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                int myX = childView.getRight();
                if (hasNinePatch) {
                    Rect rect = new Rect(myX, mPaddingStart, myX + mCurrentThickness, parent.getHeight() - mPaddingEnd);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, myX, mPaddingStart, mPaint);
                }
            }

        } else {

            boolean isPureLine = isPureLine();
            if (!isPureLine) {
                PathEffect effects = new DashPathEffect(new float[]{0, 0, mDashWidth, mThickness}, mDashGap);
                mPaint.setPathEffect(effects);
            }

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                int myX = childView.getLeft() - mThickness / 2;
                if (isPureLine) {
                    c.drawLine(myX, mPaddingStart, myX, parent.getHeight() - mPaddingEnd, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(myX, mPaddingStart);
                    path.lineTo(myX, parent.getHeight() - mPaddingEnd);
                    c.drawPath(path, mPaint);
                }
            }

            for (int i = 0; i < childrentCount; i++) {
                if (!mLastLineVisible && i == childrentCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                int myX = childView.getRight() + mThickness / 2;
                if (isPureLine) {
                    c.drawLine(myX, mPaddingStart, myX, parent.getHeight() - mPaddingEnd, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(myX, mPaddingStart);
                    path.lineTo(myX, parent.getHeight() - mPaddingEnd);
                    c.drawPath(path, mPaint);
                }

            }
        }
    }


    private void drawGrid(Canvas c, RecyclerView parent) {

        int childrentCount = parent.getChildCount();
        int columnSize = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        int adapterChildrenCount = parent.getAdapter().getItemCount();

        if (mDrawableRid != 0) {
            if (hasNinePatch) {
                for (int i = 0; i < childrentCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myX = childView.getRight();
                    int myY = childView.getBottom();

                    //horizonal
                    if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                        Rect rect = new Rect(0, myY, myX, myY + mBmp.getHeight());
                        mNinePatch.draw(c, rect);
                    }

                    //vertical
                    if (isLastRowGrid(i, adapterChildrenCount, columnSize)
                            && !isLastGridColumn(i, columnSize)) {
                        Rect rect = new Rect(myX, childView.getTop(), myX + mBmp.getWidth(), myY);
                        mNinePatch.draw(c, rect);
                    } else if (!isLastGridColumn(i, columnSize)) {
                        Rect rect = new Rect(myX, childView.getTop(), myX + mBmp.getWidth(), myY + mBmp.getHeight());
                        mNinePatch.draw(c, rect);
                    }

                }
            } else {

                for (int i = 0; i < childrentCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myX = childView.getRight();
                    int myY = childView.getBottom();

                    //horizonal
                    if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                        c.drawBitmap(mBmp, childView.getLeft(), myY, mPaint);
                    }

                    //vertical
                    if (!isLastGridColumn(i, columnSize)) {
                        c.drawBitmap(mBmp, myX, childView.getTop(), mPaint);
                    }


                }
            }
        } else if (mDashWidth == 0 && mDashGap == 0) {

            for (int i = 0; i < childrentCount; i++) {
                View childView = parent.getChildAt(i);
                int myX = childView.getRight() + mThickness / 2;
                int myY = childView.getBottom() + mThickness / 2;

                //horizonal
                if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                    c.drawLine(childView.getLeft(), myY, childView.getRight() + mThickness, myY, mPaint);
                }

                //vertical
                if (isLastRowGrid(i, adapterChildrenCount, columnSize)
                        && !isLastGridColumn(i, columnSize)) {
                    c.drawLine(myX, childView.getTop(), myX, childView.getBottom(), mPaint);
                } else if (!isLastGridColumn(i, columnSize)) {
                    c.drawLine(myX, childView.getTop(), myX, myY, mPaint);
                }

            }


        } else {
            PathEffect effects = new DashPathEffect(new float[]{0, 0, mDashWidth, mThickness}, mDashGap);
            mPaint.setPathEffect(effects);
            for (int i = 0; i < childrentCount; i++) {
                View childView = parent.getChildAt(i);
                int myX = childView.getRight() + mThickness / 2;
                int myY = childView.getBottom() + mThickness / 2;

                //horizonal
                if (!isLastRowGrid(i, adapterChildrenCount, columnSize)) {
                    Path path = new Path();
                    path.moveTo(0, myY);
                    path.lineTo(myX, myY);
                    c.drawPath(path, mPaint);
                }

                //vertical
                if (isLastRowGrid(i, adapterChildrenCount, columnSize)
                        && !isLastGridColumn(i, columnSize)) {
                    Path path = new Path();
                    path.moveTo(myX, childView.getTop());
                    path.lineTo(myX, childView.getBottom());
                    c.drawPath(path, mPaint);
                } else if (!isLastGridColumn(i, columnSize)) {
                    Path path = new Path();
                    path.moveTo(myX, childView.getTop());
                    path.lineTo(myX, childView.getBottom());
                    c.drawPath(path, mPaint);
                }

            }
        }
    }


    private boolean isLastGridColumn(int position, int columnSize) {
        boolean isLast = false;
        if ((position + 1) % columnSize == 0) {
            isLast = true;
        }
        return isLast;
    }


    private boolean isLastRowGrid(int position, int itemSize, int columnSize) {
        return position / columnSize == (itemSize - 1) / columnSize;
    }

    public static class Builder {

        private Param params;
        private Context context;

        public Builder(Context context) {

            params = new Param();
            this.context = context;

        }

        public RecyclerViewItemDecoration1 create() {
            RecyclerViewItemDecoration1 recyclerViewItemDecoration = new RecyclerViewItemDecoration1();
            recyclerViewItemDecoration.setParams(context, params);
            return recyclerViewItemDecoration;
        }

        public Builder mode(int mode) {
            params.mode = mode;
            return this;
        }

        public Builder drawableID(int drawableID) {
            params.drawableRid = drawableID;
            return this;
        }

        public Builder color(@ColorInt int color) {
            params.color = color;
            return this;
        }

        public Builder color(String color) {
            if (isColorString(color)) {
                params.color = Color.parseColor(color);
            }
            return this;
        }

        public Builder thickness(int thickness) {
            params.thickness = thickness;
            return this;
        }

        public Builder dashWidth(int dashWidth) {
            params.dashWidth = dashWidth;
            return this;
        }

        public Builder dashGap(int dashGap) {
            params.dashGap = dashGap;
            return this;
        }

        public Builder lastLineVisible(boolean visible) {
            params.lastLineVisible = visible;
            return this;
        }

        public Builder firstLineVisible(boolean visible) {
            params.firstLineVisible = visible;
            return this;
        }

        public Builder paddingStart(int padding) {
            params.paddingStart = padding;
            return this;
        }

        public Builder paddingEnd(int padding) {
            params.paddingEnd = padding;
            return this;
        }
    }

    private static class Param {

        public int mode = MODE_HORIZONTAL;
        public int drawableRid = 0;
        public int color = Color.parseColor(DEFAULT_COLOR);
        public int thickness;
        public int dashWidth = 0;
        public int dashGap = 0;
        public boolean lastLineVisible;
        public boolean firstLineVisible;
        public int paddingStart;
        public int paddingEnd;
    }

}