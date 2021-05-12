package com.example.myapplication3.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Annotation;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication3.R;

import org.w3c.dom.Text;

public class CardViewPost extends FrameLayout {

    ConstraintLayout cl = new ConstraintLayout(getContext());
    TextView postBody = new TextView(getContext());
    TextView author = new TextView(getContext());


    public CardViewPost(Context context) {
        super(context);
        init(null);
    }

    private void init(Object o) {

    }

    public CardViewPost(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(null);

    }

    public CardViewPost(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CardViewPost(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(null);
    }

    @Override
    protected void onDraw(Canvas canvas){
        cl.layout(0, 0, 400,100); //Modify values as needed
        postBody.setWidth((int) 100d);
        postBody.setHeight((int) 100d);
        postBody.layout(100,100,100,100);
        postBody.setTextColor(Color.BLACK);
        postBody.setText("POost tesxt");
        cl.draw(canvas);
    }


    public void setPostBody(String body) {
        postBody.setText(body);
    }

    public void setAuthor(String poster) {
        author.setText(poster);
    }
}