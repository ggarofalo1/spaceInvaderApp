package com.spaceisgreat.www.invaderapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private TextView title;
    private TextView gestureText;
    private TextView scoreText;
    private TextView dodgeText;
    private Button backToGame;

    private View.OnClickListener returnToGame = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            clickedReturn();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_screen);

        //set custom font for text
        title = (TextView) findViewById(R.id.helpTitle);
        gestureText = (TextView) findViewById(R.id.gestureTextView);
        scoreText = (TextView) findViewById(R.id.scoreTextView);
        dodgeText = (TextView) findViewById(R.id.dodgeTextView);

        backToGame = (Button) findViewById(R.id.returnToGame);
        backToGame.setOnClickListener(returnToGame);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/gamecuben.ttf");
        title.setTypeface(typeface);
        gestureText.setTypeface(typeface);
        scoreText.setTypeface(typeface);
        dodgeText.setTypeface(typeface);
    }
    public void clickedReturn() {

        Intent backToGame = new Intent(this, GreetingScreen.class);
        this.startActivity(backToGame);
    }
}
