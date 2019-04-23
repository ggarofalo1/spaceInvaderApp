package com.spaceisgreat.www.invaderapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GreetingScreen extends AppCompatActivity {

    private TextView title;
    private TextView version;
    private Button play;
    private Button help;
    public final double VERSION = 1.0;


    private View.OnClickListener clickPlaygame = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickedPlaygame();
        }
    };

    private View.OnClickListener clickHelp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickedHelp();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greating_screen);

        //set custom font for title
        title = (TextView) findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/gamecuben.ttf");
        title.setTypeface(typeface);

        //version text
        version = (TextView) findViewById(R.id.version);
        version.setText(getResources().getString(R.string.version, VERSION));

        //Button
        play = findViewById(R.id.playGame);
        play.setOnClickListener(clickPlaygame);

        //How to play button
        help = findViewById(R.id.help);
        help.setOnClickListener(clickHelp);

    }



    public void clickedPlaygame(){
        Intent game = new Intent(this, MainActivity.class);
        this.startActivity(game);
    }

    public void clickedHelp(){
        Intent help = new Intent(this, HelpActivity.class);
        this.startActivity(help);
    }
}
