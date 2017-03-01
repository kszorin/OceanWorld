package kszorin.seaworld.model.behaviour;

import kszorin.seaworld.model.*;
import kszorin.seaworld.view.PlayingWorldView;

import java.util.List;
import java.util.Map;

import static kszorin.seaworld.view.PlayingWorldView.CLEAR_WATER_CODE;

public class InEnvironsMoving implements MovingBehaviour {
    @Override
    public void move(Animal animal, PlayingWorldView playingWorldView, List<Position> foundPositionsInEnvirons) {
        int waterSpace[][] = playingWorldView.getWaterSpace();
        Map<Integer, SeaCreature> seaCreaturesMap = playingWorldView.getSeaCreaturesMap();
        Position pos = animal.getPos();

        if (foundPositionsInEnvirons.size() > 0) {
//        Случайным образом выбираем позицию из буфера свободных мест.
            int bufferRandomNum = (int) (Math.random() * (foundPositionsInEnvirons.size()));
            Position selectedFreePos = foundPositionsInEnvirons.get(bufferRandomNum);
//        Перемещаем персонаж в новую позицию.
            waterSpace[selectedFreePos.getY()][selectedFreePos.getX()] = waterSpace[pos.getY()][pos.getX()];
            waterSpace[pos.getY()][pos.getX()] = CLEAR_WATER_CODE;
            seaCreaturesMap.get(animal.getId()).setPos(selectedFreePos);
            System.out.printf("%c(%d) [%d,%d]: -> [%d,%d]\n", animal.getSpecies().toString().charAt(0), animal.getId(), pos.getX(), pos.getY(),
                    animal.getPos().getX(), animal.getPos().getY());
        }
    }
}
