package kszorin.seaworld.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kszorin.seaworld.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {

    private PlayingWorldView mainView;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mainView = (PlayingWorldView) view.findViewById(R.id.main_view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mainView.stopGame();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
