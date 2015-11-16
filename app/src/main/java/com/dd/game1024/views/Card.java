package com.dd.game1024.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by dong on 2015-11-16 0016.
 */
public class Card extends FrameLayout {
    private Context mContext;

    private static final int BACKGROUND_0 = 0xffccc0b2;
    private static final int BACKGROUND_2 = 0xffeee4da;
    private static final int BACKGROUND_4 = 0xffece0ca;
    private static final int BACKGROUND_8 = 0xfff2b179;
    private static final int BACKGROUND_16 = 0xffeb5a32;
    private static final int BACKGROUND_32 = 0xfffe7d5d;
    private static final int BACKGROUND_64 = 0xffeb5a34;
    private static final int BACKGROUND_128 = 0xffe0c167;
    private static final int BACKGROUND_256 = 0xfff4cf5a;
    private static final int BACKGROUND_512 = 0xffe1bd44;
    private static final int BACKGROUND_1024 = 0xfff3ca32;
    private static final int BACKGROUND_2048 = 0xfff89e19;
    private static final int BACKGROUND_4096 = 0xff78b03a;

    private int mBackgroundColor[] = {
            BACKGROUND_0,
            BACKGROUND_2,
            BACKGROUND_4,
            BACKGROUND_8,
            BACKGROUND_16,
            BACKGROUND_32,
            BACKGROUND_64,
            BACKGROUND_128,
            BACKGROUND_256,
            BACKGROUND_512,
            BACKGROUND_1024,
            BACKGROUND_2048,
            BACKGROUND_4096
    };

    private int mNum = 0;
    private TextView mLable;

    private ScaleAnimation mScaleAnimation;

    public Card(Context context) {
        this(context, null);
    }

    public Card(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Card(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

        mLable = new TextView(mContext);
        mLable.setGravity(Gravity.CENTER);
        mLable.setTextSize(32);
        setNum(mNum);
        FrameLayout.LayoutParams pl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mLable, pl);

        setPadding(10, 10, 0, 0);

        mScaleAnimation = new ScaleAnimation(0.6f, 1, 0.6f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, .5f);
        mScaleAnimation.setDuration(100);
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int mNum) {
        this.mNum = mNum;
        if (mNum == 0) {
            mLable.setText("");
            mLable.setBackgroundColor(BACKGROUND_0);
        } else {
            int index = (int) (Math.log(mNum) / Math.log(2));
            if (index > mBackgroundColor.length) index = mBackgroundColor.length - 1;
            mLable.setText(mNum + "");
            mLable.setBackgroundColor(mBackgroundColor[index]);
        }
    }

    public boolean isEquals(Card card) {
        if (this.mNum != card.getNum()) {
            return false;
        }
        return true;
    }

    public void showAnimate() {
        if (mScaleAnimation != null)
            startAnimation(mScaleAnimation);
    }
}
