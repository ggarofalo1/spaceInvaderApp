package com.spaceisgreat.www.invaderapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;

/*
simulates the surface of the game is an extension of surface view
 */

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {


    private GameThread gameThread;

    private Player player;
    private Rock rock;
    int gamestart =0;

    public void gamerunning(boolean run){
        if (this.gameThread != null) {
            this.gameThread.setRunning(run);
            Log.e("Game Thread", "Thread changed to: " + run);
        }
    }

    public GameSurface(Context context)  {
        super(context);

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
    }

    public void update()  {
        if (gamestart == 1) {
            this.player.update();
            this.rock.update();
            int playerX = this.player.getX();
            int playerY = this.player.getY();
            int rockX = this.rock.getX();
            int rockY = this.rock.getY();
            int xDif = playerX - rockX;
            int yDif = playerY - rockY;

            if((-150 < xDif && xDif < 150) && (yDif > -150 && yDif < 150)){
                endPlayer();
            }
        }
    }

    //starts updating the player and other objects when value =1
    public void startgame(int num){
        gamestart = num;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gamestart == 1) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                int movingVectorX = x - this.player.getX();
                int movingVectorY = y - this.player.getY();

                this.player.setMovingVector(movingVectorX, movingVectorY);
                return true;
            }
        }else{
            gamestart = 1;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);

        this.player.draw(canvas);
        this.rock.draw(canvas);
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPlayer();

        Bitmap rockBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.rock50x50);
        this.rock = new Rock(this,rockBitmap1,500,50);

        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }

    //creates a new player
    public void startPlayer(){
        Bitmap playerBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.rocket50x50x4x4);
        this.player = new Player(this,playerBitmap1,475,1300);
    }


    //kills the player
    public void endPlayer() {
        Bitmap playerBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.deadplayer);
        this.player = new Player(this,playerBitmap1,50,50);
    }

    public void superUsed() {
        for(int i = 0; i < numRocks; i++){

        }
    }
}