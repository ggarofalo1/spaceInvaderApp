package com.spaceisgreat.www.invaderapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
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
    private int numSupers = 2;
    private Player player;
    private Rock[] rocks;
    private int numRocks = 2;
    int gamestart =0;

    private double timer;
    private double interval = .1;
    private boolean etimer = false;
    private Paint textPaint;
    private int screenWidth;
    private int screenHeight;
    public static final double TEXT_SIZE_PERCENT = 0.5 / 18;


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

        textPaint = new Paint();

        // Sét callback.
        this.getHolder().addCallback(this);
    }

    public void update()  {
        if (gamestart == 1) {
            this.player.update();
            for(int i = 0; i < this.numRocks; i++) {
                this.rocks[i].update();
            }
            int playerX = this.player.getX();
            int playerY = this.player.getY();

            for(int i = 0; i < this.numRocks; i++) { //get distance between player and rocks
                int rockX = this.rocks[i].getX();
                int rockY = this.rocks[i].getY();
                int xDif = playerX - rockX;
                int yDif = playerY - rockY;

                if ((-125 < xDif && xDif < 125) && (yDif > -125 && yDif < 125)) { //150x150 pixel hit box.. if a player enters this hit box for a rock, kill the player
                    //endPlayer();      //clears player
                    //endEnemies();    //clears enemies
                    etimer = true;  //ends timer
                    gameOver();    //shows gameoveer popup
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
            gamestart = 1;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);

        // player
        this.player.draw(canvas);

        //rocks- enemies
        for(int i = 0; i < this.numRocks; i++) {
            this.rocks[i].draw(canvas);
        }

        //timer text
        textPaint.setColor(getResources().getColor(R.color.colorTimer));
        //textPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(getResources().getString(R.string.time_format, timer), 800, 50, textPaint);
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.gameThread = new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
        rocks = new Rock[this.numRocks];

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

        int someX;
        Random generator = new Random();
        Bitmap rockBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.rock50x50);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        for(int i = 0; i < this.numRocks; i++) {
            someX = generator.nextInt(width) + 1;
            rocks[i] = new Rock(this, rockBitmap1, someX, -50);
            if(i > 0) {
                if((this.rocks[i-1].x - this.rocks[i].x) < 100) {
                    this.rocks[i].x = someX + 150;
                }
            }
        }
    }
    public void endEnemies() { //kills the enemies

        for (int i = 0; i < this.numRocks; i++) {
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
        if(this.numSupers > 0) {
            this.numSupers -= 1;
            String superMsg = String.format("You now have %d super(s) left", this.numSupers);
            Toast.makeText(getContext(), superMsg, Toast.LENGTH_SHORT).show();
            endEnemies();
        }
    }

    //pop up alerting the user
    public void gameOver(){
        /*new AlertDialog.Builder(getContext())
                .setTitle("Game Over")
                .setMessage(String.format("You played for: %d seconds. Would you like to play agian?", this.timer))

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newGame();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/

    }

    public void newGame(){
        startgame(0);
        endPlayer();
        startPlayer();
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