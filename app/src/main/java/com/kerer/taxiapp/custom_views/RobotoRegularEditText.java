package com.kerer.taxiapp.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ivan on 03.01.17.
 */

public class RobotoRegularEditText extends EditText {
    public RobotoRegularEditText(Context context) {
        super(context);
        changeTypeface();
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        changeTypeface();
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        changeTypeface();
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        changeTypeface();
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_regular.ttf");
        super.setTypeface(tf, style);
    }

    @Override
    public void setTypeface(Typeface tf) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_regular.ttf");
        super.setTypeface(tf);
    }

    private void changeTypeface() {
        Typeface robotoRegular = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_regular.ttf");
        this.setTypeface(robotoRegular);
    }
}
