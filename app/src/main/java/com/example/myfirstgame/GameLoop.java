package com.example.myfirstgame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    private static final double MAX_UPS= 40.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;
    private Game game;
    private boolean isRunning = false;
    private SurfaceHolder surfaceHolder;
    private double averageUPS;
    private double averageFPS;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        isRunning=true;
        //Starts the Thread and the run method
        start();
    }

    @Override
    public void run() {
        super.run();

        //Declare time and cycle count var
        int updateCount=0;
        int frameCount=0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        startTime=System.currentTimeMillis();

        //Game loop
        Canvas canvas=null;
        while (isRunning){
            //Update and render games / try because "Surface was already locked"
            try {
                canvas = surfaceHolder.lockCanvas(); //locks canvas and returns canvas object
                synchronized (surfaceHolder){
                    game.update();
                    updateCount++;
                    game.draw(canvas);
                }


            } catch (IllegalArgumentException e){
                e.printStackTrace();
            }

            finally {
                if(canvas!=null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }



            //Pause loop to not exceed target UPS
            elapsedTime=System.currentTimeMillis()-startTime;
            sleepTime=(long) (updateCount*UPS_PERIOD-elapsedTime);
            if(sleepTime>0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            //Skip frames to keep up with UPS
            while(sleepTime<0 && updateCount < MAX_UPS-1){
                game.update();
                updateCount++;
                elapsedTime=System.currentTimeMillis()-startTime;
                sleepTime=(long) (updateCount*UPS_PERIOD-elapsedTime);
            }

            //Calculate average UPS and FPS

            elapsedTime=System.currentTimeMillis()-startTime;

            if(elapsedTime>=1000){
                averageUPS=updateCount / (1E-3*elapsedTime);
                averageFPS=frameCount / (1E-3*elapsedTime);
                updateCount=0;
                frameCount=0;
                startTime=System.currentTimeMillis();
            }





        }

    }
}
