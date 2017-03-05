package kszorin.seaworld.model.behaviour;

import java.util.List;

import kszorin.seaworld.model.Animal;
import kszorin.seaworld.model.Position;
import kszorin.seaworld.view.PlayingWorldView;

/**
 * Created by kszorin on 05.03.2017.
 */

public class VegeterianBeheviour implements EatingBehaviour {
    @Override
    public boolean eat(Animal animal, PlayingWorldView playingWorldView, List<Position> foundPositionsInEnvirons) {
        return false;
    }
}
