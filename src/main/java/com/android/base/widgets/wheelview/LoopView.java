package com.android.base.widgets.wheelview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.android.base.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LoopView extends View {

    private float scaleX = 1F;

    private static final float DEFAULT_TEXT_SIZE = (Resources.getSystem().getDisplayMetrics().density * 16);

    private static final int DEFAULT_VISIBIE_ITEMS = 9;

    public enum ACTION {
        CLICK, FLING, DAGGLE
    }

    private Context context;

    Handler handler;
    private GestureDetector flingGestureDetector;
    LoopItemSelectedListener onItemSelectedListener;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    private Paint paintOuterText;
    private Paint paintCenterText;
    private Paint paintIndicator;

    List<String> items;

    float lineSpacingMultiplier;
    int maxTextHeight;

    int outerTextSize;
    int centerTextSize;
    int outerTextColor;
    int centerTextColor;
    int dividerColor;

    boolean isLoop;

    int firstLineY;
    int secondLineY;

    int totalScrollY;
    int initPosition;
    private int selectedItem;
    int preCurrentIndex;
    int change;

    int itemsVisibleCount;

    String[] drawingStrings;

    int measuredHeight;
    int measuredWidth;

    int halfCircumference;
    int radius;

    private int mOffset = 0;
    private float previousY;
    long startTime = 0;

    private Rect outerRect = new Rect();
    private Rect centerRect = new Rect();

    private int paddingLeft, paddingRight;


    public void setCenterTextColor(int centerTextColor) {
        this.centerTextColor = centerTextColor;
        paintCenterText.setColor(centerTextColor);
    }


    public void setOuterTextColor(int outerTextColor) {
        this.outerTextColor = outerTextColor;
        paintOuterText.setColor(outerTextColor);
    }


    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        paintIndicator.setColor(dividerColor);
    }

    public LoopView(Context context) {
        super(context);
        initLoopView(context, null);
    }

    public LoopView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        initLoopView(context, attributeset);
    }

    public LoopView(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        initLoopView(context, attributeset);
    }

    private void initLoopView(Context context, AttributeSet attributeset) {
        this.context = context;
        handler = new LoopMessageHandler(this);
        flingGestureDetector = new GestureDetector(context, new LoopGestureListener(this));
        flingGestureDetector.setIsLongpressEnabled(false);

        TypedArray typedArray = context.obtainStyledAttributes(attributeset, R.styleable.WheelView);
        centerTextSize = (int) typedArray.getDimension(R.styleable.WheelView_awv_centerTextSize, DEFAULT_TEXT_SIZE);
        outerTextSize = (int) typedArray.getDimension(R.styleable.WheelView_awv_outerTextSize, DEFAULT_TEXT_SIZE);
        centerTextColor = typedArray.getColor(R.styleable.WheelView_awv_centerTextColor, 0xff313131);
        outerTextColor = typedArray.getColor(R.styleable.WheelView_awv_outerTextColor, 0xffafafaf);
        dividerColor = typedArray.getColor(R.styleable.WheelView_awv_dividerTextColor, 0xffEEEEEE);
        itemsVisibleCount = typedArray.getInteger(R.styleable.WheelView_awv_itemsVisibleCount, DEFAULT_VISIBIE_ITEMS);
        if (itemsVisibleCount % 2 == 0) {
            itemsVisibleCount = DEFAULT_VISIBIE_ITEMS;
        } else {
            itemsVisibleCount += 2;//实际的条目数比itemsVisibleCount少两个，所以要加2
        }
        isLoop = typedArray.getBoolean(R.styleable.WheelView_awv_isLoop, true);
        typedArray.recycle();

        drawingStrings = new String[itemsVisibleCount];

        totalScrollY = 0;
        initPosition = -1;

        initPaints();
    }


    public void setItemsVisibleCount(int visibleNumber) {
        if (visibleNumber % 2 == 0) {
            return;
        }
        if (visibleNumber != itemsVisibleCount) {
            itemsVisibleCount = visibleNumber;
            drawingStrings = new String[itemsVisibleCount];
        }
    }

    private void initPaints() {
        paintOuterText = new Paint();
        paintOuterText.setColor(outerTextColor);
        paintOuterText.setAntiAlias(true);
        paintOuterText.setTypeface(Typeface.MONOSPACE);
        paintOuterText.setTextSize(outerTextSize);
        paintCenterText = new Paint();
        paintCenterText.setColor(centerTextColor);
        paintCenterText.setAntiAlias(true);
        paintCenterText.setTextScaleX(scaleX);
        paintCenterText.setTypeface(Typeface.MONOSPACE);
        paintCenterText.setTextSize(centerTextSize);
        paintIndicator = new Paint();
        paintIndicator.setColor(dividerColor);
        paintIndicator.setAntiAlias(true);
    }

    private void remeasure() {
        if (items == null) {
            return;
        }
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
        if (measuredWidth == 0 || measuredHeight == 0) {
            return;
        }
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        measuredWidth = measuredWidth - paddingRight;
        paintOuterText.getTextBounds("哈哈", 0, 2, outerRect);
        paintCenterText.getTextBounds("哈哈", 0, 2, centerRect);
        maxTextHeight = centerRect.height();
        halfCircumference = (int) (measuredHeight * Math.PI / 2);
        lineSpacingMultiplier = Float.valueOf(new DecimalFormat("0.00").format((float)
                halfCircumference / (maxTextHeight * (itemsVisibleCount - 1))));
        radius = measuredHeight / 2;
        firstLineY = (int) ((measuredHeight - lineSpacingMultiplier * maxTextHeight) / 2.0F);
        secondLineY = (int) ((measuredHeight + lineSpacingMultiplier * maxTextHeight) / 2.0F);
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (items.size() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }
        preCurrentIndex = initPosition;
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            float itemHeight = lineSpacingMultiplier * maxTextHeight;
            mOffset = (int) ((totalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture =
                mExecutor.scheduleWithFixedDelay(new LoopScroll(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();
        // change this number, can change fling speed
        int velocityFling = 10;
        mFuture = mExecutor.scheduleWithFixedDelay(new LoopTimerTask(this, velocityY), 0, velocityFling,
                TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    public void setNotLoop() {
        isLoop = false;
    }

    public final void setCenterTextSize(float size) {
        if (size > 0.0F) {
            centerTextSize = (int) (context.getResources().getDisplayMetrics().density * size);
            paintCenterText.setTextSize(centerTextSize);
        }
    }

    public final void setOuterTextSize(float size) {
        if (size > 0.0F) {
            outerTextSize = (int) (context.getResources().getDisplayMetrics().density * size);
            paintOuterText.setTextSize(outerTextSize);
        }
    }

    public final void setInitPosition(int initPosition) {
        if (initPosition < 0) {
            this.initPosition = 0;
        } else {
            if (items != null && items.size() > initPosition) {
                this.initPosition = initPosition;
            }
        }
    }

    public final void setListener(LoopItemSelectedListener OnItemSelectedListener) {
        onItemSelectedListener = OnItemSelectedListener;
    }

    public final void setItems(List<String> items) {
        this.items = items;
        remeasure();
        invalidate();
    }

    public final int getSelectedItem() {
        return selectedItem;
    }

    protected final void onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(new LoopSelectedRunnable(this), 200L);
        }
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setCurrentPosition(int position) {
        if (items == null || items.isEmpty()) {
            return;
        }
        int size = items.size();
        if (position >= 0 && position < size && position != selectedItem) {
            initPosition = position;
            totalScrollY = 0;
            mOffset = 0;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (items == null) {
            return;
        }
        change = (int) (totalScrollY / (lineSpacingMultiplier * maxTextHeight));
        preCurrentIndex = initPosition + change % items.size();
        if (!isLoop) {
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = items.size() - 1;
            }
        } else {
            if (preCurrentIndex < 0) {
                preCurrentIndex = items.size() + preCurrentIndex;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = preCurrentIndex - items.size();
            }
        }
        int j2 = (int) (totalScrollY % (lineSpacingMultiplier * maxTextHeight));
        // put value to drawingString
        int k1 = 0;
        while (k1 < itemsVisibleCount) {
            int l1 = preCurrentIndex - (itemsVisibleCount / 2 - k1);
            if (isLoop) {
                while (l1 < 0) {
                    l1 = l1 + items.size();
                }
                while (l1 > items.size() - 1) {
                    l1 = l1 - items.size();
                }
                drawingStrings[k1] = items.get(l1);
            } else if (l1 < 0) {
                drawingStrings[k1] = "";
            } else if (l1 > items.size() - 1) {
                drawingStrings[k1] = "";
            } else {
                drawingStrings[k1] = items.get(l1);
            }
            k1++;
        }
        canvas.drawLine(paddingLeft, firstLineY, measuredWidth, firstLineY, paintIndicator);
        canvas.drawLine(paddingLeft, secondLineY, measuredWidth, secondLineY, paintIndicator);

        int i = 0;
        while (i < itemsVisibleCount) {
            canvas.save();
            float itemHeight = maxTextHeight * lineSpacingMultiplier;
            double radian = ((itemHeight * i - j2) * Math.PI) / halfCircumference;
            if (radian >= Math.PI || radian <= 0) {
                canvas.restore();
            } else {
                int translateY = (int) (radius - Math.cos(radian) * radius - (Math.sin(radian) * maxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // first divider
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
                    canvas.drawText(drawingStrings[i], getTextX(outerRect), getTextY(outerRect), paintOuterText);
                    canvas.restore();

                    canvas.save();
                    canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingStrings[i], getTextX(centerRect), getTextY(centerRect), paintCenterText);
                    canvas.restore();
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // second divider
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    canvas.drawText(drawingStrings[i], getTextX(centerRect), getTextY(centerRect), paintCenterText);
                    canvas.restore();

                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingStrings[i], getTextX(outerRect), getTextY(outerRect), paintOuterText);
                    canvas.restore();
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // center item_sort
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingStrings[i], getTextX(centerRect), getTextY(centerRect), paintCenterText);
                    selectedItem = items.indexOf(drawingStrings[i]);
                } else {
                    // other item_sort
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.drawText(drawingStrings[i], getTextX(outerRect), getTextY(outerRect), paintOuterText);
                }
                canvas.restore();
            }
            i++;
        }
    }

    // text start drawing position
    private int getTextX(Rect rect) {
        int textWidth = rect.width();
        textWidth *= scaleX;
        return (measuredWidth - paddingLeft - textWidth) / 2 + paddingLeft;
    }

    private int getTextY(Rect rect) {
        return rect.height() - rect.bottom / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        remeasure();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean eventConsumed = flingGestureDetector.onTouchEvent(event);
        float itemHeight = lineSpacingMultiplier * maxTextHeight;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = previousY - event.getRawY();
                previousY = event.getRawY();
                totalScrollY = (int) (totalScrollY + dy);
                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (items.size() - 1 - initPosition) * itemHeight;
                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((radius - y) / radius) * radius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - itemsVisibleCount / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - startTime) > 120) {
                        smoothScroll(ACTION.DAGGLE);
                    } else {
//                        smoothScroll(ACTION.CLICK);
                    }
                }
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        invalidate();
        return true;
    }
}