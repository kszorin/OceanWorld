package kszorin.model;

import kszorin.model.behaviour.Hunting;
import kszorin.model.behaviour.InEnvironsMoving;
import kszorin.model.behaviour.PeriodicReproduction;

import java.util.ArrayList;
import java.util.List;

public class Orca extends Animal {
    private static final byte ORCA_REPRODUCTION_PERIOD = 8;
    private static final byte ORCA_ENVIRONS = 1;
    private static final byte ORCA_HUNGER_DEATH_PERIOD = 3;


    public Orca(int id, Position pos) {
        super(id, pos);
        species = SealCreatureSpecies.Orca;
        reproductionPeriod = ORCA_REPRODUCTION_PERIOD;
        environs = ORCA_ENVIRONS;
        eatingBehaviour = new Hunting();
        reproductionBehaviour = new PeriodicReproduction();
        movingBehaviour = new InEnvironsMoving();
        targetList.add(SealCreatureSpecies.Penguin);
    }

    @Override
    public void lifeStep(PlayingWorld playingWorld) {
        if (eatingBehaviour.eat(this, playingWorld, findInEnvirons (playingWorld, targetList))) {
            timeFromEating = 0;
        }
        else {
            movingBehaviour.move(this, playingWorld, findInEnvirons(playingWorld));
            timeFromEating++;
        }
        if (timeFromEating >= ORCA_HUNGER_DEATH_PERIOD) {
            playingWorld.getWaterSpace()[pos.getY()][pos.getX()] = -1;
            playingWorld.getSeaCreaturesMap().remove(this.id);
            System.out.printf("%c(%d) [%d,%d]: died of hungry!\n", species.toString().charAt(0), id, pos.getX(), pos.getY());
        }
        else {
            age++;
            if ((age!=0) && (age % ORCA_REPRODUCTION_PERIOD == 0)) {
                reproductionBehaviour.reproduct(this, playingWorld, findInEnvirons(playingWorld));
            }
        }
    }

    @Override
    public Animal getBaby(int id, Position pos) {
        return new Orca(id, pos);
    }
}