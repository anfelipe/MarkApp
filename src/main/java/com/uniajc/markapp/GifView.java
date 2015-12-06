package com.uniajc.markapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import java.io.InputStream;

public class GifView extends View {

    private InputStream gifInputStream;
    private Movie gifMovie;
    private int movieWidth, movieHeigth;
    private long movieDuration;
    private long movieStrar;

    public GifView(Context context){
        super(context);
        init(context);
    }

    public GifView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }

    public GifView(Context context, AttributeSet attributeSet, int defStyleAttr){
        super(context, attributeSet, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setFocusable(true);
        gifInputStream = context.getResources().openRawResource(+ R.drawable.eslogan_markapp); //Ruta de gif

        gifMovie = Movie.decodeStream(gifInputStream);
        movieWidth = gifMovie.width();
        movieHeigth = gifMovie.height();
        movieDuration = gifMovie.duration();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(movieWidth, movieHeigth);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        long now = SystemClock.uptimeMillis();

        if(movieStrar == 0){
            movieStrar = now;
        }

        if(gifMovie != null){

            int dur = gifMovie.duration();
            if(dur == 0){
                dur = 1000;
            }

            int relTime = (int)((now - movieStrar) % dur);

            gifMovie.setTime(relTime);
            gifMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }
}
