package com.spaceisgreat.www.invaderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SettingsDialogFragment extends DialogFragment {
    private SeekBar difficultySeekbar;
    private SeekBar speedSeekbar;
    private SeekBar ammoSeekbar;
    private SeekBar powerupSeekbar;

    private int difficulty, speed, ammo, powerups;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View colorDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_settings, null);
        builder.setView(colorDialogView);

        builder.setTitle(R.string.settings_name);

        difficultySeekbar = (SeekBar) colorDialogView.findViewById(R.id.difficultySeekbar);
        speedSeekbar = (SeekBar) colorDialogView.findViewById(R.id.speedSeekbar);
        ammoSeekbar = (SeekBar) colorDialogView.findViewById(R.id.ammoSeekbar);
        powerupSeekbar = (SeekBar) colorDialogView.findViewById(R.id.powerupSeekbar);

        difficultySeekbar.setOnSeekBarChangeListener(difficultyChangedListener);
        speedSeekbar.setOnSeekBarChangeListener(speedChangedListener);
        ammoSeekbar.setOnSeekBarChangeListener(ammoChangedListener);
        powerupSeekbar.setOnSeekBarChangeListener(powerupChangedListener);

        builder.setPositiveButton(R.string.accept_settings,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }
        );

        return builder.create();
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

    private final OnSeekBarChangeListener speedChangedListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            speed = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private final OnSeekBarChangeListener ammoChangedListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            ammo = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
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
