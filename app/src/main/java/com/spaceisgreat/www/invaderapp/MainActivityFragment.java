
package com.spaceisgreat.www.invaderapp;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivityFragment extends Fragment
{
   private GameSurface gameSurface;
   GameSurface thegame;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      thegame = new GameSurface(getContext());

      View view = inflater.inflate(R.layout.fragment_main, container, false);
      thegame = (GameSurface) view.findViewById(R.id.gameView);
      return view;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState)
   {
      super.onActivityCreated(savedInstanceState);
      getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
   }

   @Override
   public void onPause()
   {
      super.onPause();
      thegame.gamerunning(false);
      gameSurface.stopGame();
   }

    @Override
    public void onResume() {
        super.onResume();
        thegame.gamerunning(true);
        //enableAccelerometerListening(); // listen for shake event
    }

   @Override
   public void onDestroy() {
      super.onDestroy();
      gameSurface.releaseResources();
   }
}
