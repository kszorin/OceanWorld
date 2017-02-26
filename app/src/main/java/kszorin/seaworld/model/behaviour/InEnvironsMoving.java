package kszorin.seaworld.model.behaviour;

import kszorin.seaworld.model.*;

import java.util.List;
import java.util.Map;

public class InEnvironsMoving implements MovingBehaviour {
    @Override
    public void move(Animal animal, PlayingWorld playingWorld, List<Position> foundPositionsInEnvirons) {
        int waterSpace[][] = playingWorld.getWaterSpace();
        Map<Integer, SeaCreature> seaCreaturesMap = playingWorld.getSeaCreaturesMap();
        Position pos = animal.getPos();

        if (foundPositionsInEnvirons.size() > 0) {
//        Случайным образом выбираем позицию из буфера свободных мест.
            int bufferRandomNum = (int) (Math.random() * (foundPositionsInEnvirons.size()));
            Position selectedFreePos = foundPositionsInEnvirons.get(bufferRandomNum);
//        Перемещаем животину в новую позицию.
            waterSpace[selectedFreePos.getY()][selectedFreePos.getX()] = waterSpace[pos.getY()][pos.getX()];
            waterSpace[pos.getY()][pos.getX()] = -1;
            seaCreaturesMap.get(animal.getId()).setPos(selectedFreePos);
            System.out.printf("%c(%d) [%d,%d]: -> [%d,%d]\n", animal.getSpecies().toString().charAt(0), animal.getId(), pos.getX(), pos.getY(),
                    animal.getPos().getX(), animal.getPos().getY());
        }
    }
}
