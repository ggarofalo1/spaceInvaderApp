package com.spaceisgreat.www.invaderapp;

import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Random;

/*
simulates the surface of the game is an extension of surface view
 */

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private Player player;
    private Rock[] rocks;
    private Context context;

    private static int theDifficulty = 0;
    private static int numAmmo = 1000;
    private static int numSupers = 2;
    private static int numRocks = 2;
    private static String theUsername = "";
    int gamestart =0;

    private double timer = 0.0;
    private double interval = .1;
    private boolean etimer = false;
    private Paint textPaint;
    private static int screenWidth;
    private static int screenHeight;
    public static final double TEXT_SIZE_PERCENT = 0.5 / 18;
    public static final double BOX_SIZE_PERCENT = 2.0 / 18;


    public void gamerunning(boolean run){
        if (this.gameThread != null) {
            this.gameThread.setRunning(run);
            Log.e("Game Thread", "Thread changed to: " + run);
        }
    }

    public GameSurface(Context context)  {
        super(context);
        this.context = context;

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        textPaint = new Paint();

        // Sét callback.
        this.getHolder().addCallback(this);

        newGame();
    }

    public void update()  {
        if (gamestart == 1) {
            this.player.update();

            for(int i = 0; i < numRocks; i++) {
                if(timer % 3.0 == 0) {
                    this.rocks[i].setDifficultyMultiplier(rocks[i].getDifficultyMultiplier() * 1.1f);
                }
                this.rocks[i].update();
            }
            int playerX = this.player.getX();
            int playerY = this.player.getY();

            for(int i = 0; i < numRocks; i++) { //get distance between player and rocks
                int rockX = this.rocks[i].getX();
                int rockY = this.rocks[i].getY();
                int xDif = playerX - rockX;
                int yDif = playerY - rockY;

                if ((-125 < xDif && xDif < 125) && (yDif > -125 && yDif < 125)) { //150x150 pixel hit box.. if a player enters this hit box for a rock, kill the player
                    //endPlayer();      //clears player
                    //endEnemies();    //clears enemies
                    etimer = true;  //ends timer
                    gameOver();    //shows game over popup
                    gamestart = 0;   //stop update loop
                }
            }

            if(!etimer)
                timer += interval;
        }
    }

    //starts updating the player and other objects when value =1
    public void startgame(int num){
        gamestart = num;
    }


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
            gamestart = 1;  //if paused but user taps start game
        }
        return false;
    }


    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);

        int margin = 10;
        Rect r = new Rect((int)(screenWidth-300), margin, screenWidth-margin, (int)(100));
        Drawable d;
        Paint paint = new Paint();

        //set background
        d = getResources().getDrawable(R.drawable.spacebg, null);
        d.setBounds(0, 0, screenWidth, screenHeight);
        d.draw(canvas);

        //red square
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.timerbglowscore));
        canvas.drawRect(r, paint);

        //timer text
        textPaint.setTextSize((int) (TEXT_SIZE_PERCENT * screenHeight));
        textPaint.setColor(getResources().getColor(R.color.colorTimer));
        //textPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(getResources().getString(R.string.time_format, timer), 800, 70, textPaint);

        // player
        this.player.draw(canvas);

        //rocks- enemies
        for(int i = 0; i < numRocks; i++) {
            this.rocks[i].draw(canvas);
        }


    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
        boolean retry = true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);
                this.gameThread.join();
                retry = false;
                // Parent thread must wait until the end of GameThread.
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //creates a new player
    public void startPlayer(){
        Bitmap playerBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.rocket50x50x4x4);
        this.player = new Player(this,playerBitmap1,475,1300);
    }
    public void startEnemies() { //initializes the enemies

        Bitmap rockBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.rock50x50);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        int someX = screenWidth/(numRocks + 1);
        for(int i = 0; i < numRocks; i++) {

            rocks[i] = new Rock(this, rockBitmap1, (someX * (i+1)), -50);
            switch(theDifficulty) {
                case 0:
                    System.out.println(theDifficulty);
                    break;
                case 1:
                    if(i % 2 == 0) {
                        this.rocks[i].setXVector(50);
                    }
                    else {
                        this.rocks[i].setXVector(-50);
                    }
                    this.rocks[i].setDifficultyMultiplier(2.00f);
                    this.rocks[i].setMovingVector(200);
                case 2:
                    this.rocks[i].setDifficultyMultiplier(3.00f);
                    this.rocks[i].setMovingVector(200);
                    if(i % 2 == 0) {
                        this.rocks[i].setXVector(200);
                    }
                    else {
                        this.rocks[i].setXVector(-200);
                    }
            }
        }
    }
    public void endEnemies() { //kills the enemies

        for (int i = 0; i < numRocks; i++) {
            Bitmap deadRock = BitmapFactory.decodeResource(this.getResources(),R.drawable.deadplayer);
            this.rocks[i] = new Rock(this, deadRock, 50, 50);
        }
    }

    //kills the player
    public void endPlayer() {
        Bitmap playerBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.deadplayer);
        this.player = new Player(this,playerBitmap1,50,50);
    }

    public void superUsed() { //all current rocks go off the screen
        if(numSupers > 0) {
            numSupers -= 1;
            String superMsg = String.format("You now have %d super(s) left", numSupers);
            Toast.makeText(getContext(), superMsg, Toast.LENGTH_SHORT).show();
            endEnemies();
        }
    }

    public void updateSettings(int difficulty, int powerups, int ammo, String username) {
        theDifficulty = difficulty;
        if (theDifficulty == 0) {
            numRocks = 2;
        }else if(theDifficulty == 1) {
            numRocks = 5;
        }else if(theDifficulty == 2) {
            numRocks = 7;
        }

        numSupers = powerups;
        numAmmo = ammo;
        theUsername = username;
    }

    //pop up alerting the user
    public void gameOver(){

        //start end game  dialog
        EndGameDialogFragment endDialog = new EndGameDialogFragment();
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();

        //give score to dialog
        Bundle args = new Bundle();
        args.putDouble("timer", timer);
        endDialog.setArguments(args);

        endDialog.show(fm, "Game Over");
        newGame();
    }

    public void newGame(){
        startgame(0);
        endPlayer();
        startPlayer();
        rocks = new Rock[numRocks];
        endEnemies();
        startEnemies();
        timer = 0;   //reset timer
        etimer = false;  //start timer
        Toast.makeText(getContext(), "New Game Started", Toast.LENGTH_LONG).show();
    }


    // called when the size of the SurfaceView changes,
    // such as when it's first added to the View hierarchy
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w; // store CannonView's width
        screenHeight = h; // store CannonView's height

        // configure text properties
        textPaint.setTextSize((int) (TEXT_SIZE_PERCENT * screenHeight));
        textPaint.setAntiAlias(true); // smoothes the text
    }

    // get width of the game screen
    public int getScreenWidth() {
        return screenWidth;
    }

    // get height of the game screen
    public int getScreenHeight() {
        return screenHeight;
    }

}