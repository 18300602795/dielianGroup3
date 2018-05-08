package com.etsdk.app.huov7.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.EditText;

import com.etsdk.app.huov7.R;


/**
 * Created by xkazerzhang on 2017/5/24.
 */
public class DemoEditText extends EditText {
    public DemoEditText(Context context) {
        super(context);
        init();
    }

    public DemoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DemoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 定制Demo中的编辑框
    private void init(){
        getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
    }
}
