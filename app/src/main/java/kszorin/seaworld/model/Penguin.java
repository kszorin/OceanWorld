package kszorin.seaworld.model;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import kszorin.seaworld.model.behaviour.InEnvironsMoving;
import kszorin.seaworld.model.behaviour.PeriodicReproduction;
import kszorin.seaworld.view.PlayingWorldView;

public class Penguin extends Animal {
    private static final int PENGUIN_REPRODUCTION_PERIOD = 3;
    private static final byte PENGUIN_ENVIRONS = 1;

    public Penguin(int id, Position pos, PlayingWorldView playingWorldView, Bitmap bmp) {
        super(id, pos, playingWorldView, bmp);
        species = SealCreatureSpecies.Penguin;
        environs = PENGUIN_ENVIRONS;
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
    public Animal getBaby(int id, Position pos, Bitmap bmp) {
        return new Penguin(id, pos, playingWorldView, bmp);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float squareWidth = playingWorldView.getSquareWidth(), squareHeight = playingWorldView.getSquareHeight();

//        Для масштабирования картинок.
        float scaleFactor;
        if (age < BMP_SCALE)
            scaleFactor = (1f + age % BMP_SCALE) / BMP_SCALE;
        else
            scaleFactor = 1;

        int bmpWidth = (int) (scaleFactor * squareWidth);
        int bmpHeight = (int) (scaleFactor * squareHeight);

//        Индикация скорых родов.
        if (age % PENGUIN_REPRODUCTION_PERIOD == PENGUIN_REPRODUCTION_PERIOD - 1) {
            paint.setColor(Color.GREEN);
            canvas.drawRect(squareWidth * pos.getX() + (int) (0.5 * (squareWidth - bmpWidth)),
                    squareHeight * pos.getY() + (int) (0.5 * squareHeight),
                    squareWidth * pos.getX() + (int) (0.5 * (squareWidth + bmpWidth)),
                    squareHeight * pos.getY() + (int) (0.5 * (squareHeight + bmpHeight)),
                    paint);
        }

//        Рисуем создание.
        canvas.drawBitmap(Bitmap.createScaledBitmap(bmp, bmpWidth, bmpHeight, false),
                squareWidth * pos.getX() + (int)(0.5 * (squareWidth - bmpWidth)),
                squareHeight * pos.getY() + (int)(0.5 * (squareHeight - bmpHeight)), null);
    }
}
