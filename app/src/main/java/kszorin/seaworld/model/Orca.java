package kszorin.seaworld.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import kszorin.seaworld.model.behaviour.Hunting;
import kszorin.seaworld.model.behaviour.InEnvironsMoving;
import kszorin.seaworld.model.behaviour.PeriodicReproduction;
import kszorin.seaworld.view.PlayingWorldView;

public class Orca extends Animal {
    private static final byte ORCA_REPRODUCTION_PERIOD = 8;
    private static final byte ORCA_ENVIRONS = 1;
    private static final byte ORCA_HUNGER_DEATH_PERIOD = 3;


    public Orca(int id, Position pos, PlayingWorldView playingWorldView) {
        super(id, pos, playingWorldView);
        species = SealCreatureSpecies.Orca;
        reproductionPeriod = ORCA_REPRODUCTION_PERIOD;
        environs = ORCA_ENVIRONS;
        eatingBehaviour = new Hunting();
        reproductionBehaviour = new PeriodicReproduction();
        movingBehaviour = new InEnvironsMoving();
        targetList.add(SealCreatureSpecies.Penguin);
    }

    @Override
    public void lifeStep() {
        if (eatingBehaviour.eat(this, playingWorldView, findInEnvirons (targetList))) {
            timeFromEating = 0;
        }
        else {
            movingBehaviour.move(this, playingWorldView, findInEnvirons());
            timeFromEating++;
        }
        if (timeFromEating >= ORCA_HUNGER_DEATH_PERIOD) {
            playingWorldView.getWaterSpace()[pos.getY()][pos.getX()] = -1;
            playingWorldView.getSeaCreaturesMap().remove(this.id);
            System.out.printf("%c(%d) [%d,%d]: died of hungry!\n", species.toString().charAt(0), id, pos.getX(), pos.getY());
        }
        else {
            age++;
            if ((age!=0) && (age % ORCA_REPRODUCTION_PERIOD == 0)) {
                reproductionBehaviour.reproduct(this, playingWorldView, findInEnvirons());
            }
        }
    }

    @Override
    public Animal getBaby(int id, Position pos) {
        return new Orca(id, pos, playingWorldView);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.RED);
        canvas.drawRect(playingWorldView.getSquareWidth()/4 + playingWorldView.getSquareWidth() * pos.getX(),
                playingWorldView.getSquareHeight()/4 + playingWorldView.getSquareHeight() * pos.getY(),
                playingWorldView.getSquareWidth()*3/4 + playingWorldView.getSquareWidth() * pos.getX(),
                playingWorldView.getSquareHeight()*3/4 + playingWorldView.getSquareHeight() * pos.getY(),
                paint);
    }
}