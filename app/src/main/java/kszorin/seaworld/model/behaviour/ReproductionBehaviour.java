package kszorin.seaworld.model.behaviour;

import kszorin.seaworld.model.Animal;
import kszorin.seaworld.model.PlayingWorld;
import kszorin.seaworld.model.Position;

import java.util.List;

public interface ReproductionBehaviour {
    void reproduct (Animal animal, PlayingWorld playingWorld, List<Position> foundPositionsInEnvirons);
}
