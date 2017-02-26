package kszorin.seaworld.model;


import kszorin.seaworld.model.behaviour.InEnvironsMoving;
import kszorin.seaworld.model.behaviour.PeriodicReproduction;
import kszorin.seaworld.view.PlayingWorldView;

public class Penguin extends Animal {
    private static final int PENGUIN_REPRODUCTION_PERIOD = 3;
    private static final byte PENGUIN_ENVIRONS = 1;

    public Penguin(int id, Position pos) {
        super(id, pos);
        species = SealCreatureSpecies.Penguin;
        environs = PENGUIN_ENVIRONS;
        reproductionPeriod = PENGUIN_REPRODUCTION_PERIOD;
        eatingBehaviour = null;
        reproductionBehaviour = new PeriodicReproduction();
        movingBehaviour = new InEnvironsMoving();
    }

    @Override
    public void lifeStep(PlayingWorldView playingWorldView) {
        movingBehaviour.move(this, playingWorldView, findInEnvirons (playingWorldView));
        age++;
        if ((age!=0) && (age % PENGUIN_REPRODUCTION_PERIOD == 0)) {
            reproductionBehaviour.reproduct(this, playingWorldView, findInEnvirons(playingWorldView));
        }
    }

    @Override
    public Animal getBaby(int id, Position pos) {
        return new Penguin(id, pos);
    }
}
