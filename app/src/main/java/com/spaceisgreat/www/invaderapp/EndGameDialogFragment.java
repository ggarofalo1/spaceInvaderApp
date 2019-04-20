package com.spaceisgreat.www.invaderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class EndGameDialogFragment extends DialogFragment {
    private EditText usernameEditText;
    private TextView score;
    private GameSurface gamesurface;
    private String username;
    private double timer;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View gameoverDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_gameover, null);
        builder.setView(gameoverDialogView);

        builder.setTitle(R.string.game_over);
        timer = getArguments().getDouble("timer");

        score = gameoverDialogView.findViewById(R.id.displayscore);
        usernameEditText = (EditText) gameoverDialogView.findViewById(R.id.usernameEditText);

        score.setText(getResources().getString(R.string.score, timer));

        builder.setPositiveButton(R.string.new_game,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //username = usernameEditText.getText().toString();
                        gamesurface = new GameSurface(getContext());
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


}
