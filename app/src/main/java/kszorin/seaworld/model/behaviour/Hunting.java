package kszorin.seaworld.model.behaviour;

import kszorin.seaworld.model.Animal;
import kszorin.seaworld.model.Position;
import kszorin.seaworld.model.SeaCreature;
import kszorin.seaworld.view.PlayingWorldView;

import java.util.List;
import java.util.Map;

import static kszorin.seaworld.view.PlayingWorldView.CLEAR_WATER_CODE;

public class Hunting implements EatingBehaviour {
    @Override
    public boolean eat(Animal animal, PlayingWorldView playingWorldView, List<Position> foundPositionsInEnvirons) {
        int waterSpace[][] = playingWorldView.getWaterSpace();
        Map<Integer, SeaCreature> seaCreaturesMap = playingWorldView.getSeaCreaturesMap();
        Position pos = animal.getPos();

        if (foundPositionsInEnvirons.size() > 0) {
//        Случайным образом выбираем позицию из буфера найденых целей.
            int bufferRandomNum = (int) (Math.random() * (foundPositionsInEnvirons.size()));
            Position selectedTargetPos = foundPositionsInEnvirons.get(bufferRandomNum);

//        Уничтожаем жертву и перемещаем хищника на новую позицию.
            System.out.printf("%c(%d) [%d,%d]: kill %c (%d) [%d,%d]\n", animal.getSpecies().toString().charAt(0), animal.getId(), pos.getX(), pos.getY(),
                    seaCreaturesMap.get(waterSpace[selectedTargetPos.getY()][selectedTargetPos.getX()]).getSpecies().toString().charAt(0),
                    waterSpace[selectedTargetPos.getY()][selectedTargetPos.getX()], selectedTargetPos.getX(), selectedTargetPos.getY());
//            Для ведения статистики.
            switch (seaCreaturesMap.get(waterSpace[selectedTargetPos.getY()][selectedTargetPos.getX()]).getSpecies()) {
                case Orca: playingWorldView.setOrcasQuantity(playingWorldView.getOrcasQuantity() - 1);
                    break;
                case Penguin: playingWorldView.setPenguinsQuantity(playingWorldView.getPenguinsQuantity() - 1);
                    break;
            }
            seaCreaturesMap.remove(waterSpace[selectedTargetPos.getY()][selectedTargetPos.getX()]);
            waterSpace[selectedTargetPos.getY()][selectedTargetPos.getX()] = waterSpace[pos.getY()][pos.getX()];
            waterSpace[pos.getY()][pos.getX()] = CLEAR_WATER_CODE;
            seaCreaturesMap.get(animal.getId()).setPos(selectedTargetPos);
            return true;
        }
        else
            return false;
    }
}
