package kszorin.seaworld.model.behaviour;

import kszorin.seaworld.model.Animal;
import kszorin.seaworld.model.PlayingWorld;
import kszorin.seaworld.model.Position;
import kszorin.seaworld.model.SeaCreature;
import kszorin.seaworld.model.behaviour.EatingBehaviour;

import java.util.List;
import java.util.Map;

public class Hunting implements EatingBehaviour {
    @Override
    public boolean eat(Animal animal, PlayingWorld playingWorld, List<Position> foundPositionsInEnvirons) {
        int waterSpace[][] = playingWorld.getWaterSpace();
        Map<Integer, SeaCreature> seaCreaturesMap = playingWorld.getSeaCreaturesMap();
        Position pos = animal.getPos();

        if (foundPositionsInEnvirons.size() > 0) {
//        Случайным образом выбираем позицию из буфера свободных мест.
            int bufferRandomNum = (int) (Math.random() * (foundPositionsInEnvirons.size()));
            Position selectedTargetPos = foundPositionsInEnvirons.get(bufferRandomNum);
//        Уничтожаем жертву и перемещаем хищника на новую позицию.
            System.out.printf("%c(%d) [%d,%d]: kill %c (%d) [%d,%d]\n", animal.getSpecies().toString().charAt(0), animal.getId(), pos.getX(), pos.getY(),
                    seaCreaturesMap.get(waterSpace[selectedTargetPos.getY()][selectedTargetPos.getX()]).getSpecies().toString().charAt(0),
                    waterSpace[selectedTargetPos.getY()][selectedTargetPos.getX()], selectedTargetPos.getX(), selectedTargetPos.getY());

            seaCreaturesMap.remove(waterSpace[selectedTargetPos.getY()][selectedTargetPos.getX()]);
            waterSpace[selectedTargetPos.getY()][selectedTargetPos.getX()] = waterSpace[pos.getY()][pos.getX()];
            waterSpace[pos.getY()][pos.getX()] = -1;
            seaCreaturesMap.get(animal.getId()).setPos(selectedTargetPos);

            return true;
        }
        return false;
    }

}
