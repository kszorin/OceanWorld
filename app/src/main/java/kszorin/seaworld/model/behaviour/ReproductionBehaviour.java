package kszorin.model.behaviour;

import kszorin.model.Animal;
import kszorin.model.PlayingWorld;
import kszorin.model.Position;

import java.util.List;

public interface ReproductionBehaviour {
    void reproduct (Animal animal, PlayingWorld playingWorld, List<Position> foundPositionsInEnvirons);
}
