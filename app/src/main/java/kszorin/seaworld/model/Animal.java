package kszorin.seaworld.model;

import kszorin.seaworld.model.behaviour.EatingBehaviour;
import kszorin.seaworld.model.behaviour.MovingBehaviour;
import kszorin.seaworld.model.behaviour.ReproductionBehaviour;
import kszorin.seaworld.view.PlayingWorldView;

import java.util.*;

public abstract class Animal extends SeaCreature {
    protected byte age;
    protected byte reproductionPeriod;
    protected byte timeFromEating=0;
    protected byte timeFromReproduction=0;

    protected EatingBehaviour eatingBehaviour;
    protected ReproductionBehaviour reproductionBehaviour;
    protected MovingBehaviour movingBehaviour;

    protected List<SealCreatureSpecies> targetList = new ArrayList<SealCreatureSpecies>();

    public Animal(int id, Position pos) {
        super(id, pos);
        this.age = 0;
        this.timeFromEating = 0;
        this.timeFromReproduction = 0;
    }

    public abstract Animal getBaby(int id, Position pos);

    protected List<Position> findInEnvirons (PlayingWorldView playingWorldView) {
        int waterSpace[][] = playingWorldView.getWaterSpace();
        int beginRangeBypassX, endRangeBypassX, beginRangeBypassY, endRangeBypassY;

//        Определяем границу окрестности по X.
        if ((beginRangeBypassX = pos.getX() - environs) < 0)
            beginRangeBypassX = 0;
        if ((endRangeBypassX = pos.getX() + environs) > (playingWorldView.getFieldSizeX()-1))
            endRangeBypassX = playingWorldView.getFieldSizeX()-1;
//        Определяем границу окрестности по Y.
        if ((beginRangeBypassY = pos.getY() - environs) < 0)
            beginRangeBypassY = 0;
        if ((endRangeBypassY = pos.getY() + environs) > (playingWorldView.getFieldSizeY()-1))
            endRangeBypassY = playingWorldView.getFieldSizeY()-1;
//        Определение подходящих мест в окрестности и заполнение буфера свободными позициями.
        List<Position> findPosBuffer = new ArrayList<Position>();
        for (int i = beginRangeBypassY, j; i <= endRangeBypassY; i++)
            for (j = beginRangeBypassX; j <= endRangeBypassX; j++) {
                if ((i == pos.getY()) && (j == pos.getX()))
                    continue;
                else {
                    if (waterSpace[i][j] == -1)
                        findPosBuffer.add(new Position(j,i));
                }
            }
        return findPosBuffer;
    }

    protected List<Position> findInEnvirons (PlayingWorldView playingWorldView, List<SealCreatureSpecies> targets) {
        int waterSpace[][] = playingWorldView.getWaterSpace();
        Map<Integer, SeaCreature> seaCreaturesMap = playingWorldView.getSeaCreaturesMap();
        int beginRangeBypassX, endRangeBypassX, beginRangeBypassY, endRangeBypassY;

        if (targets.size() == 0)
            return Collections.emptyList();
        else {
//        Определяем границу окрестности по X.
            if ((beginRangeBypassX = pos.getX() - environs) < 0)
                beginRangeBypassX = 0;
            if ((endRangeBypassX = pos.getX() + environs) > (playingWorldView.getFieldSizeX() - 1))
                endRangeBypassX = playingWorldView.getFieldSizeX() - 1;
//        Определяем границу окрестности по Y.
            if ((beginRangeBypassY = pos.getY() - environs) < 0)
                beginRangeBypassY = 0;
            if ((endRangeBypassY = pos.getY() + environs) > (playingWorldView.getFieldSizeY() - 1))
                endRangeBypassY = playingWorldView.getFieldSizeY() - 1;

//        Определение подходящих мест в окрестности и заполнение буфера свободными позициями.
            List<Position> findPosBuffer = new ArrayList<Position>();
            for (int i = beginRangeBypassY, j; i <= endRangeBypassY; i++)
                for (j = beginRangeBypassX; j <= endRangeBypassX; j++) {
                    if ((i == pos.getY()) && (j == pos.getX()))
                        continue;
                    else {
                        if ((waterSpace[i][j] != -1) && (targets.indexOf(seaCreaturesMap.get(waterSpace[i][j]).getSpecies()) != -1))
                            findPosBuffer.add(new Position(j,i));
                    }
                }
            return findPosBuffer;
        }
    }

    @Override
    public abstract void lifeStep(PlayingWorldView playingWorldView);
}
