package com.spaceisgreat.www.invaderapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Rock extends GameObject {

    private static final int ROW_ONE = 0;


    // Row index of Image are being used.
    private int rowUsing = ROW_ONE;

    private int colUsing;

    private Bitmap[] imagerock;

    // Velocity of game character (pixel/millisecond)
    public static final float VELOCITY = 0.1f;
    private float difficultyMultiplier = 1.50f;
    private int movingVectorX = 0;
    private int movingVectorY = 50;
    private long lastDrawNanoTime =-1;

    private GameSurface gameSurface;

    public Rock(GameSurface gameSurface, Bitmap image, int x, int y) {

        super(image, 1, 1, x, y);

        this.gameSurface= gameSurface;
        this.imagerock = new Bitmap[colCount]; //1

        for(int col = 0; col< this.colCount; col++ ) {
            this.imagerock[col] = this.createSubImageAt(ROW_ONE, col);
        }
    }

    public Bitmap[] getMoveBitmaps()  {
        switch (rowUsing)  {
            case ROW_ONE:
                return  this.imagerock;
            default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap()  {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.colUsing];
    }


    public void update()  {
        this.colUsing++;
        if(colUsing >= this.colCount)  {
            this.colUsing =0;
        }
        // Current time in nanoseconds
        long now = System.nanoTime();

        // Never once did draw.
        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }
        // Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds).
        int deltaTime = (int) ((now - lastDrawNanoTime)/ 1000000 );

        // Distance moves
        float distance = difficultyMultiplier * VELOCITY * deltaTime;
        double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);

        // Calculate the new position of the game character.
        this.x = x +  (int)(distance* movingVectorX / movingVectorLength);
        this.y = y +  (int)(distance* movingVectorY / movingVectorLength);

        // When the game's character touches the edge of the screen, then change direction

        if(this.x < 0 )  {
            this.x = 0;
            this.movingVectorX = - this.movingVectorX;
        } else if(this.x > this.gameSurface.getWidth() -width)  {
            this.x= this.gameSurface.getWidth()-width;
            this.movingVectorX = - this.movingVectorX;
        }

        if(this.y > this.gameSurface.getHeight()- height)  {
            this.y = -height;
        }

    }

    public void draw(Canvas canvas)  {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap,x, y, null);
        // Last draw time.
        this.lastDrawNanoTime= System.nanoTime();
    }

    public void setMovingVector(int movingVectorY)  {
        this.movingVectorY = movingVectorY;
    }
    public void setXVector(int movingVectorX) { this.movingVectorX = movingVectorX; }
    public void setDifficultyMultiplier(float difficultyMultiplier) { this.difficultyMultiplier = difficultyMultiplier; }
}
