package com.achulkov.loftmon.screens.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.achulkov.loftmon.R;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class DiagramView extends View {

    private float mExpences;
    private float mIncome;

    private Paint expencePaint = new Paint();
    private Paint incomePaint = new Paint();

    public DiagramView(final Context context) {
        super(context);
        init(null);
    }

    public DiagramView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DiagramView(
            final Context context,
            @Nullable final AttributeSet attrs,
            final int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public DiagramView(
            final Context context,
            @Nullable final AttributeSet attrs,
            final int defStyleAttr,
            final int defStyleRes
    ) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void update(float expences, float income) {
        mExpences = expences;
        mIncome = income;
        invalidate();
    }

    private void init(@Nullable AttributeSet attrs) {
/*        expencePaint.setColor(ContextCompat.getColor(getContext(), R.color.dark_sky_blue));
        incomePaint.setColor(ContextCompat.getColor(getContext(), R.color.apple_green));*/

        if (attrs == null) {
            return;
        }

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.DiagramView);
        expencePaint.setColor(ta.getColor(R.styleable.DiagramView_expenseColor, getResources().getColor(R.color.dark_sky_blue)));
        incomePaint.setColor(ta.getColor(R.styleable.DiagramView_incomeColor, getResources().getColor(R.color.apple_green)));
        ta.recycle();
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(
            final boolean changed,
            final int left,
            final int top,
            final int right,
            final int bottom
    ) {
        super.onLayout(changed, left, top, right, bottom);
    }



    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        float total = mExpences + mIncome;

        float expenceAngle = 360f*mExpences / total;
        float incomeAngle = 360f*mIncome / total;

        int space = 10;
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space,
                getHeight() - yMargin, 180 - expenceAngle/2, expenceAngle, true, expencePaint);

        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space,
                getHeight() - yMargin, 360 - incomeAngle/2, incomeAngle,true, incomePaint);
    }
}

