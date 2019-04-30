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
    private GameSurface gamesurface;
    private String[] highScore;
    private String username;
    private double timer;
    public final String FILENAME ="gameRecord.txt";
    private String record="";

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View gameoverDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_gameover, null);
        builder.setView(gameoverDialogView);

        builder.setTitle(R.string.game_over);
        timer = getArguments().getDouble("timer");

        score = gameoverDialogView.findViewById(R.id.displayscore);
        Hscore = gameoverDialogView.findViewById(R.id.displayhighscore);
        usernameEditText = (EditText) gameoverDialogView.findViewById(R.id.usernameEditText);

        username = usernameEditText.getText().toString();
        highScore = getHighRecord(getContext(), FILENAME);

        score.setText(getResources().getString(R.string.score, timer));
        Hscore.setText(getResources().getString(R.string.hscore)+ highScore[2]);

        builder.setPositiveButton(R.string.new_game,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //username = usernameEditText.getText().toString();
                        gamesurface = new GameSurface(getContext());
                        try{
                            double hs =Double.parseDouble(highScore[2]);
                            if(timer >= hs){
                                record = ("1.0//"+username+"//"+Double.toString(timer));
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
            File root = new File(Environment.getExternalStorageDirectory(), "records");
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, sFileName);
            FileWriter writer = new FileWriter(file);
            writer.write(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved Record", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public String[] getHighRecord(Context context, String sFileName) {
        String High="";
        String[] highscore = new String[0];
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "records");
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, sFileName);
            FileReader reader = new FileReader(file);
            int i;
            while ((i=reader.read()) != -1)
                High = (High +(char) i);

            highscore = High.split("//", 4);
        } catch (IOException e) {
            //e.printStackTrace();
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
