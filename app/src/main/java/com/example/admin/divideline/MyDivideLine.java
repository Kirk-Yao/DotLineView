package com.example.admin.divideline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2017/9/11.
 */

public class MyDivideLine extends View {

    private Context context;

    private int circleSize = 24;
    private int lineSize = 2;

    private float circleX,circleY;
    private int radius;

    private Drawable beginLine,endLine,circleDrawable;
    private int orientation;

    private static final int VERTICAL = 1;
    private static final int HORIZONTAL = 0;

    public MyDivideLine(Context context) {
        this(context,null);
        this.context = context;
    }

    public MyDivideLine(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyDivideLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs){
        final TypedArray typedArray = getContext()
                .obtainStyledAttributes(attrs,R.styleable.MyDivideLine);

        circleSize = typedArray.getDimensionPixelSize(R.styleable.MyDivideLine_circleSize,circleSize);
        lineSize = typedArray.getDimensionPixelSize(R.styleable.MyDivideLine_lineSize,lineSize);
        beginLine = typedArray.getDrawable(R.styleable.MyDivideLine_beginLine);
        endLine = typedArray.getDrawable(R.styleable.MyDivideLine_endLine);
        circleDrawable = typedArray.getDrawable(R.styleable.MyDivideLine_circleBackGround);
        orientation = typedArray.getInteger(R.styleable.MyDivideLine_orientation,orientation);

        typedArray.recycle();

        if (beginLine != null){
            beginLine.setCallback(this);
        }

        if (endLine != null){
            endLine.setCallback(this);
        }

        if (circleDrawable != null){
            circleDrawable.setCallback(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (orientation == HORIZONTAL){
            if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(120,80);
            } else if (widthSpecMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(120,heightSpecSize);
            } else if (heightSpecMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(widthSpecSize,80);
            }
        } else {
            if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(80,120);
            } else if (widthSpecMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(80,heightSpecSize);
            } else if (heightSpecMode == MeasureSpec.AT_MOST){
                setMeasuredDimension(widthSpecSize,120);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initDrawableSize();

        if (beginLine != null){
            beginLine.draw(canvas);
        }

        if (endLine != null){
            endLine.draw(canvas);
        }

        if (circleDrawable != null){
//            circleDrawable.draw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLUE);
            canvas.drawCircle(circleX,circleY,radius,paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        initDrawableSize();
    }

    private void initDrawableSize(){
        int pLeft = getPaddingLeft();
        int pTop = getPaddingTop();
        int pRight = getPaddingRight();
        int pBottom = getPaddingBottom();

        int width = getWidth();
        int height = getHeight();
        int circleWidth = width - pLeft - pRight;
        int circleHeight = height - pTop - pBottom;

        int circleSizePx = dip2px(getContext(),circleSize);
        int lineSizePx = dip2px(getContext(),lineSize);

        Rect bounds;

        if (orientation == HORIZONTAL){
            if (circleDrawable != null){
                int circleRadius = Math.min(Math.min(circleWidth,circleHeight),circleSizePx);
                circleDrawable.setBounds(pLeft + width/2 - circleRadius/2,
                        pTop,pLeft + width/2  + circleRadius/2,pTop + circleRadius);
                bounds = circleDrawable.getBounds();

                circleX = pLeft + width/2;
                circleY = pTop + circleRadius/2;
                radius = circleRadius / 2;
            } else {
                bounds = new Rect(pLeft + width/2,pTop,pRight + width/2,pBottom );
            }

            int halfLineSize = lineSize / 2;
            int lineTop = bounds.centerY() - halfLineSize;
            if (beginLine != null){
                beginLine.setBounds(0,lineTop,bounds.left,lineTop + lineSizePx);
            }
            if (endLine != null){
                endLine.setBounds(bounds.right,lineTop,width,lineTop + lineSizePx);
            }
        } else {
            if(circleDrawable!=null){
                int circleRadius = Math.min(Math.min(circleWidth,circleHeight),circleSizePx);
                circleDrawable.setBounds(pLeft,pTop+height/2-circleRadius/2,pLeft+circleRadius,pTop+height/2-circleRadius/2+circleRadius);
                bounds=circleDrawable.getBounds();

                circleX = pLeft + circleRadius/2;
                circleY = pTop + height/2;
                radius = circleRadius / 2;

            }else{
                bounds=new Rect(pLeft+lineSizePx/2,pTop+height/2,pLeft+lineSizePx/2,pTop+height/2);
            }
            int halfLine=lineSizePx >> 1;
            int lineLeft=bounds.centerX()-halfLine;
            if(beginLine != null){
                beginLine.setBounds(lineLeft,0,lineLeft+lineSizePx,bounds.top);
            }
            if(endLine != null){
                endLine.setBounds(lineLeft,bounds.bottom,lineLeft+lineSizePx,height);
            }
        }
    }

    public void setCircleSize(int circleSize){
        if (this.circleSize != circleSize){
            this.circleSize = circleSize;
            initDrawableSize();
            invalidate();
        }
    }

    public void setLineSize(int lineSize){
        if (this.lineSize != lineSize){
            this.lineSize = lineSize;
            initDrawableSize();
            invalidate();
        }
    }

    public void setCircleDrawable(Drawable drawable){
        if (circleDrawable != drawable){
            circleDrawable = drawable;
            if (drawable != null){
                drawable.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

    public void setBeginLine(Drawable drawable){
        if (beginLine != drawable){
            beginLine = drawable;
            if (beginLine != null){
                beginLine.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

    public void setEndLine(Drawable endLine){
        if (this.endLine != endLine){
            this.endLine = endLine;
            if (endLine != null){
                endLine.setCallback(this);
            }
            initDrawableSize();
            invalidate();
        }
    }

    /**
     * dip转化成px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
