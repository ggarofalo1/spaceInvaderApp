package com.spaceisgreat.www.invaderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class EndGameDialogFragment extends DialogFragment {
    private EditText usernameEditText;
    private TextView score;
    private TextView Hscore;
    private TextView Huser;
    private GameSurface gamesurface;
    private String[] highScore;
    private String username;
    private double timer;
    public final String FILENAME ="gameScore.txt";
    private String record="";
    private File root = new File(Environment.getExternalStorageDirectory(),"records");

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View gameoverDialogView = getActivity( ).getLayoutInflater().inflate(R.layout.fragment_gameover, null);
        builder.setView(gameoverDialogView);

        builder.setTitle(R.string.game_over);
        timer = getArguments().getDouble("timer");

        Log.e("EndGameDialog","Before get text");
        score = gameoverDialogView.findViewById(R.id.displayscore);
        Hscore = gameoverDialogView.findViewById(R.id.displayhighscore);
        Huser = gameoverDialogView.findViewById(R.id.displayhighuser);
        usernameEditText = (EditText) gameoverDialogView.findViewById(R.id.usernameEditText);

        Log.e("EndGameDialog","Before get username");
        username = usernameEditText.getText().toString();
        score.setText(getResources().getString(R.string.score, timer));

        Log.e("EndGameDialog","Before set highscore stuff");

        if (root.exists()) {
            File file = new File(root, FILENAME);
            if(file.exists()){
                highScore = getHighRecord(getContext(), FILENAME);
                Hscore.setText(getResources().getString(R.string.hscore, Float.valueOf(highScore[2])));
                Huser.setText(getResources().getString(R.string.huser)+highScore[1]);
            }else{
                Hscore.setText(getResources().getString(R.string.hscore, 0.0));
                Huser.setText(getResources().getString(R.string.huser)+"NA");
            }

        }


        Log.e("EndGameDialog","Before button create");
        builder.setPositiveButton(R.string.new_game,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        username = usernameEditText.getText().toString();
                        gamesurface = new GameSurface(getContext());
                        double hs;

                        try{
                            Log.e("EndGameDialog","get hs");
                            if (root.exists()) {
                                File file = new File(root, FILENAME);
                                if(file.exists()) {
                                    hs = Float.valueOf(highScore[2]);
                                }else{
                                    hs =0;
                                }
                            }else{
                                hs=0;
                            }
                            if((float)timer >= hs){
                                record = ("1.0%"+username+"%"+timer);
                                writeToRecords(getContext(),  FILENAME, record);
                            }
                        }catch(Exception e){

                        }

                    }
                }
        );

        return builder.create();
    }

    public void writeToRecords(Context context, String sFileName, String sBody) {
        try {
            Log.e("EndGameDialog","try write");
            File root1 = new File(Environment.getExternalStorageDirectory(), "records");
            if (!root1.exists()) {
                root1.mkdirs();
            }
            File file = new File(root1, sFileName);
            FileWriter writer = new FileWriter(file);
            writer.write(sBody);
            writer.flush();
            writer.close();
            Log.e("EndGameDialog",("saved:" + sBody));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getHighRecord(Context context, String sFileName) {
        String High="";
        String[] highscore = new String[2];
        try {
            Log.e("EndGameDialog","try getHR");
            File root1 = new File(Environment.getExternalStorageDirectory(),"records");
            if (!root1.exists()) {
                root1.mkdirs();
            }
            File file = new File(root1, sFileName);
            FileReader reader = new FileReader(file);
            int i;
            while ((i=reader.read()) != -1)
                High = (High +(char) i);

            Log.e("EndGameDialog",("High: " + High));
            highscore = High.split("%", 4);
            Log.e("EndGameDialog",("highscore[2]:" + highscore[2]));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highscore;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
