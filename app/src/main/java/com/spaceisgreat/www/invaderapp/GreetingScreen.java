package com.spaceisgreat.www.invaderapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Permissions")
                    .setMessage("Write External Storage Has Been Granted By User.")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("OK", null)

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    //permission denied
                    new AlertDialog.Builder(this)
                            .setTitle("Permissions")
                            .setMessage("Write External Storage Has Been Denied By User. This app Will not work without it.")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    System.exit(0);
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    Toast.makeText(this, "Permission denied to use your External Storage", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

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
