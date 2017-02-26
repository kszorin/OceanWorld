package kszorin.seaworld.model;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import kszorin.seaworld.model.behaviour.InEnvironsMoving;
import kszorin.seaworld.model.behaviour.PeriodicReproduction;
import kszorin.seaworld.view.PlayingWorldView;

public class Penguin extends Animal {
    private static final int PENGUIN_REPRODUCTION_PERIOD = 3;
    private static final byte PENGUIN_ENVIRONS = 1;

    public Penguin(int id, Position pos, PlayingWorldView playingWorldView) {
        super(id, pos, playingWorldView);
        species = SealCreatureSpecies.Penguin;
        environs = PENGUIN_ENVIRONS;
        reproductionPeriod = PENGUIN_REPRODUCTION_PERIOD;
        eatingBehaviour = null;
        reproductionBehaviour = new PeriodicReproduction();
        movingBehaviour = new InEnvironsMoving();
    }

    @Override
    public void lifeStep() {
        movingBehaviour.move(this, playingWorldView, findInEnvirons ());
        age++;
        if ((age!=0) && (age % PENGUIN_REPRODUCTION_PERIOD == 0)) {
            reproductionBehaviour.reproduct(this, playingWorldView, findInEnvirons());
        }
    }

    @Override
    public Animal getBaby(int id, Position pos) {
        return new Penguin(id, pos, playingWorldView);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.YELLOW);
        canvas.drawRect(playingWorldView.getSquareWidth()/4 + playingWorldView.getSquareWidth() * pos.getX(),
                playingWorldView.getSquareHeight()/4 + playingWorldView.getSquareHeight() * pos.getY(),
                playingWorldView.getSquareWidth()*3/4 + playingWorldView.getSquareWidth() * pos.getX(),
                playingWorldView.getSquareHeight()*3/4 + playingWorldView.getSquareHeight() * pos.getY(),
                paint);
    }
}
