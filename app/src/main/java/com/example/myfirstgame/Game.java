package com.example.myfirstgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/*
Game manages all objects in the game / updating states / rendering objects
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private final Player player;



    public Game(Context context) {
        /*
        super initializes parents class first (SurfaceView)
        Context is an interface which allows access to specific information
         */
        super(context);
        SurfaceHolder surfaceHolder =getHolder();
        surfaceHolder.addCallback(this);

        //Initialize gameloop
        gameLoop = new GameLoop(this, surfaceHolder);

        //Initialize Player
        player = new Player(getContext(), 500,500,30);

        //should be true anyways... means an object can be focused e.g. textfield
        setFocusable(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Handles inputs
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                player.setPosition((double) event.getX(), (double) event.getY());
                return true;

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    /*
    Canvas is the Class we draw things on
    This method draws everything on the canvas
     */
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawFPS(canvas);
        drawUPS(canvas);
        player.draw(canvas);
    }
    //Shows Updates per second
    public void drawUPS (Canvas canvas){
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(),R.color.magenta);
        paint.setTextSize(70);
        paint.setColor(color);
        canvas.drawText("UPS"+ averageUPS, 100, 100, paint);
    }

    //Shows Frames per second
    public void drawFPS (Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(),R.color.magenta);
        paint.setTextSize(70);
        paint.setColor(color);
        canvas.drawText("FPS"+ averageFPS, 100, 200, paint);
    }

    public void update() {
        //updates Game state
        player.update();
    }
}
