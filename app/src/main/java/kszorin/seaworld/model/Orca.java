package kszorin.seaworld.model;

import android.graphics.Bitmap;
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

    public Orca(int id, Position pos, PlayingWorldView playingWorldView, Bitmap bmp) {
        super(id, pos, playingWorldView, bmp);
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
            playingWorldView.setOrcasQuantity(playingWorldView.getOrcasQuantity() - 1);

            System.out.printf("%c(%d) [%d,%d]: died of hungry!\n", species.toString().charAt(0), id, pos.getX(), pos.getY());
        }
        else {
            age++;
            if ((age!=0) && (age % ORCA_REPRODUCTION_PERIOD == 0))
                reproductionBehaviour.reproduct(this, playingWorldView, findInEnvirons());
            else
                reproductionPeriod++;
        }
    }

    @Override
    public Animal getBaby(int id, Position pos, Bitmap bmp) {
        return new Orca(id, pos, playingWorldView, bmp);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
//        Для масштабирования картинок.
        float scaleFactor;
        if (age < BMP_SCALE)
            scaleFactor = (1f + age % BMP_SCALE) / BMP_SCALE;
        else
            scaleFactor = 1;
        int bmpWidth = (int) (scaleFactor * playingWorldView.getSquareWidth()), bmpHeight = (int) (scaleFactor * playingWorldView.getSquareHeight());

//        Индикация голодной смерти.
        if (timeFromEating == ORCA_HUNGER_DEATH_PERIOD - 1) {
            paint.setColor(Color.RED);
            canvas.drawRect(playingWorldView.getSquareWidth() * pos.getX() + (int) (0.5 * (playingWorldView.getSquareWidth() - bmpWidth)),
                    playingWorldView.getSquareHeight() * pos.getY() + (int) (0.5 * (playingWorldView.getSquareHeight() - bmpHeight)),
                    playingWorldView.getSquareWidth() * pos.getX() + (int) (0.5 * (playingWorldView.getSquareWidth() + bmpWidth)),
                    playingWorldView.getSquareHeight() * pos.getY() + (int) (0.5 * (playingWorldView.getSquareHeight())),
                    paint);
        }
//        Индикация скорых родов.
        if (age % ORCA_REPRODUCTION_PERIOD == ORCA_REPRODUCTION_PERIOD - 1) {
            paint.setColor(Color.GREEN);
            canvas.drawRect(playingWorldView.getSquareWidth() * pos.getX() + (int) (0.5 * (playingWorldView.getSquareWidth() - bmpWidth)),
                    playingWorldView.getSquareHeight() * pos.getY() + (int) (0.5 * (playingWorldView.getSquareHeight())),
                    playingWorldView.getSquareWidth() * pos.getX() + (int) (0.5 * (playingWorldView.getSquareWidth() + bmpWidth)),
                    playingWorldView.getSquareHeight() * pos.getY() + (int) (0.5 * (playingWorldView.getSquareHeight() + bmpHeight)),
                    paint);
        }
//        Рисуем создание.
        canvas.drawBitmap(Bitmap.createScaledBitmap(bmp, bmpWidth, bmpHeight, false),
                playingWorldView.getSquareWidth() * pos.getX() + (int)(0.5 * (playingWorldView.getSquareWidth() - bmpWidth)),
                playingWorldView.getSquareHeight() * pos.getY() + (int)(0.5 * (playingWorldView.getSquareHeight() - bmpHeight)), null);
    }
}