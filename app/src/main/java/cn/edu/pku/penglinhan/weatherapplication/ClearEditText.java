package cn.edu.pku.penglinhan.weatherapplication;

import android.content.Context;
import android.content.CursorLoader;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class ClearEditText extends android.support.v7.widget.AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable;
    public ClearEditText(Context context){this(context,null);}
    public ClearEditText(Context context, AttributeSet attrs) {
        this(context , attrs,android.R.attr.editTextStyle);
    }
    public  ClearEditText(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        init();
    }
    private void  init(){
        mClearDrawable=getCompoundDrawables()[2];
        if(mClearDrawable==null){
            mClearDrawable=getResources().getDrawable(R.drawable.magnifying_glass);
        }
        mClearDrawable.setBounds(0,0,mClearDrawable.getIntrinsicWidth(),mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }
    @Override
    public  boolean onTouchEvent(MotionEvent event){
        if(getCompoundDrawables()[2]!=null){
            if(event.getAction()== MotionEvent.ACTION_UP){
                boolean touchable=event.getX()> (getWidth() - getPaddingRight()-mClearDrawable.getIntrinsicWidth())&&(event.getX()<((getWidth()-getPaddingRight())));
                if(touchable){
                    this.setText("");
                }
            }
        }
        return  super.onTouchEvent(event);
    }
    /*判断字符串长度*/
    public void onFocusChange(View v,boolean hasFocus){
        if(hasFocus){
            setClearIconVisible(getText().length()>0);
        }
        else{
            setClearIconVisible(false);
        }
    }
    protected void setClearIconVisible(boolean visible){
        Drawable right=visible?mClearDrawable:null;
        setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],right,getCompoundDrawables()[3]);
    }
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
            setClearIconVisible(s.length()>0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    //public void setShakeAnimation(){this.setAnimation(shakeAnimation(5));}


}
