package kszorin.seaworld.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kszorin.seaworld.R;


public class MainActivityFragment extends Fragment {

    private PlayingWorldView playingWorldView;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (view != null) {
            playingWorldView = (PlayingWorldView) view.findViewById(R.id.main_view);

            Button resetGameButton = (Button) view.findViewById(R.id.reset_game_button);
            resetGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("MainThread", "Клик по РЕСТАРТ");
                    playingWorldView.resetGame();
                }
            });
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        playingWorldView.stopGame();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
