package com.spaceisgreat.www.invaderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsDialogFragment extends DialogFragment {
    private EditText usernameEditText;
    private SeekBar difficultySeekbar;
    private SeekBar powerupSeekbar;
    private GameSurface gamesurface;
    private Button reset;

    private int difficulty, ammo, powerups;
    private String username;

    private View.OnClickListener resetButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetButton();
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View settingsDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_settings, null);
        builder.setView(settingsDialogView);

        builder.setTitle(R.string.settings_name);

        difficultySeekbar = (SeekBar) settingsDialogView.findViewById(R.id.difficultySeekbar);
        usernameEditText = (EditText) settingsDialogView.findViewById(R.id.usernameEditText);
        powerupSeekbar = (SeekBar) settingsDialogView.findViewById(R.id.powerupSeekbar);
        reset = (Button) settingsDialogView.findViewById(R.id.resetscore);

        difficultySeekbar.setOnSeekBarChangeListener(difficultyChangedListener);
        powerupSeekbar.setOnSeekBarChangeListener(powerupChangedListener);
        reset.setOnClickListener(resetButtonClicked);

        builder.setPositiveButton(R.string.accept_settings,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        username = usernameEditText.getText().toString();
                        gamesurface = new GameSurface(getContext());
                        gamesurface.updateSettings(difficulty, powerups, username);
                    }
                }
        );

        return builder.create();
    }

    public void resetButton() {
        String FILENAME ="gameScore.txt";
        String record = ("1.0%"+""+"%0.0");
        try{
            File root1 = new File(Environment.getExternalStorageDirectory(), "records");
            if (!root1.exists()) {
                root1.mkdirs();
            }
            File file = new File(root1, FILENAME);
            FileWriter writer = new FileWriter(file);
            writer.write(record);
            writer.flush();
            writer.close();
            Log.e("settingFragment",("Reset:" + record));
        } catch (
        IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private final OnSeekBarChangeListener difficultyChangedListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            difficulty = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private final OnSeekBarChangeListener powerupChangedListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            powerups = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
