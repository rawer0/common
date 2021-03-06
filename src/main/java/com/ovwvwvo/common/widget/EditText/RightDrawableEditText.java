
package com.ovwvwvo.common.widget.EditText;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;

public abstract class RightDrawableEditText extends AppCompatEditText implements OnTouchListener, OnFocusChangeListener {

    private Drawable xD;
    private boolean alwaysShow;

    public RightDrawableEditText(Context context) {
        super(context);
        init();
    }

    public RightDrawableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RightDrawableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    public void setAlwaysShowDrawable(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        setDrawableVisible(true);
    }

    private OnTouchListener l;
    private OnFocusChangeListener f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                .getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    onDrawableClicked();
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    abstract void onDrawableClicked();

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setDrawableVisible(alwaysShow || StringUtil.isNotBlank(getText().toString()));
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }


    private void init() {
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = getResources()
                .getDrawable(android.R.drawable.presence_offline);
        }
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        setDrawableVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(watcher);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isFocused() && !alwaysShow) {
                setDrawableVisible(StringUtil.isNotBlank(s.toString()));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    protected void setDrawableVisible(boolean visible) {
        Drawable oldDrawable = getCompoundDrawables()[2];
        boolean wasVisible = true;
        if (oldDrawable == null) {
            wasVisible = false;
        } else {
            xD = oldDrawable;
        }
        if (visible != wasVisible) {
            Drawable x = visible ? xD : null;
            setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
        }
    }
}