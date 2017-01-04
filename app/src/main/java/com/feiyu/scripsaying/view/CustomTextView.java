package com.feiyu.scripsaying.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by YueDong on 2016/1/4
 * 自定义显示文本信息
 */
public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
        super(context);
        init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        setTypeface(setFont(context));
    }

    private static Typeface typeface;

    public static Typeface setFont(Context context) {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/customFont.ttf");
        }
        return typeface;
    }

}
