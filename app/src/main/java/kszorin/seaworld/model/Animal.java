package kszorin.seaworld.model;

import android.graphics.Bitmap;

import kszorin.seaworld.model.behaviour.EatingBehaviour;
import kszorin.seaworld.model.behaviour.MovingBehaviour;
import kszorin.seaworld.model.behaviour.ReproductionBehaviour;
import kszorin.seaworld.view.PlayingWorldView;

import java.util.*;

import static kszorin.seaworld.view.PlayingWorldView.CLEAR_WATER_CODE;

public abstract class Animal extends SeaCreature {
    protected final int BMP_SCALE = 3;

    protected int age;
    protected int timeFromEating;

    protected EatingBehaviour eatingBehaviour;
    protected ReproductionBehaviour reproductionBehaviour;
    protected MovingBehaviour movingBehaviour;

    protected List<SealCreatureSpecies> targetList = new ArrayList<SealCreatureSpecies>();

    protected Animal(int id, Position pos, PlayingWorldView playingWorldView, Bitmap bmp) {
        super(id, pos, playingWorldView, bmp);
        this.age = 0;
        this.timeFromEating = 0;
    }

    public abstract Animal getBaby(int id, Position pos, Bitmap bitmap);

//    Поиск подходящих мест в окрестности текущего существа, где есть цели, перечисленные в списке.
    protected List<Position> findInEnvirons (List<SealCreatureSpecies> targets) {
        int waterSpace[][] = playingWorldView.getWaterSpace();
        Map<Integer, SeaCreature> seaCreaturesMap = playingWorldView.getSeaCreaturesMap();
        int beginRangeBypassX, endRangeBypassX, beginRangeBypassY, endRangeBypassY;

//        Определяем границу окрестности по X.
            if ((beginRangeBypassX = pos.getX() - environs) < 0)
                beginRangeBypassX = 0;
            if ((endRangeBypassX = pos.getX() + environs) > (PlayingWorldView.FIELD_SIZE_X - 1))
                endRangeBypassX = PlayingWorldView.FIELD_SIZE_X - 1;

//        Определяем границу окрестности по Y.
            if ((beginRangeBypassY = pos.getY() - environs) < 0)
                beginRangeBypassY = 0;
            if ((endRangeBypassY = pos.getY() + environs) > (PlayingWorldView.FIELD_SIZE_Y - 1))
                endRangeBypassY = PlayingWorldView.FIELD_SIZE_Y - 1;

//        Определение подходящих мест в окрестности и заполнение буфера свободными позициями.
            List<Position> findPosBuffer = new ArrayList<Position>();
            for (int i = beginRangeBypassY, j; i <= endRangeBypassY; i++)
                for (j = beginRangeBypassX; j <= endRangeBypassX; j++) {
                    if ((i == pos.getY()) && (j == pos.getX()))
                        continue;
                    else {
                        if ((targets.size() == 0) && (waterSpace[i][j] == CLEAR_WATER_CODE))
                            findPosBuffer.add(new Position(j,i));
                        if ((targets.size() != 0) && (waterSpace[i][j] != CLEAR_WATER_CODE)
                                && (targets.indexOf(seaCreaturesMap.get(waterSpace[i][j]).getSpecies()) != -1))
                            findPosBuffer.add(new Position(j,i));
                    }
                }
            return findPosBuffer;
    }

    @Override
    public abstract void lifeStep();
}
