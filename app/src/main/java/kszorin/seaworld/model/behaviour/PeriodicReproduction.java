package kszorin.seaworld.model.behaviour;

import kszorin.seaworld.model.Animal;
import kszorin.seaworld.model.Position;
import kszorin.seaworld.model.SeaCreature;
import kszorin.seaworld.view.PlayingWorldView;

import java.util.List;
import java.util.Map;

public class PeriodicReproduction implements ReproductionBehaviour {
    @Override
    public void reproduct(Animal animal, PlayingWorldView playingWorldView, List<Position> foundPositionsInEnvirons) {
        int waterSpace[][] = playingWorldView.getWaterSpace();
        Map<Integer, SeaCreature> seaCreaturesMap = playingWorldView.getSeaCreaturesMap();
        Position pos = animal.getPos();

        if (foundPositionsInEnvirons.size() > 0) {
//        Случайным образом выбираем позицию из буфера свободных мест.
            int bufferRandomNum = (int) (Math.random() * (foundPositionsInEnvirons.size()));
            Position selectedFreePos = foundPositionsInEnvirons.get(bufferRandomNum);
//        Создаём детёныша на свободном месте.
            Animal baby = animal.getBaby(playingWorldView.getSeaCreaturesIdCounter(), selectedFreePos, animal.getBmp());
            seaCreaturesMap.put(playingWorldView.getSeaCreaturesIdCounter(), baby);
            waterSpace[selectedFreePos.getY()][selectedFreePos.getX()] = playingWorldView.getSeaCreaturesIdCounter();
            playingWorldView.setSeaCreaturesIdCounter(playingWorldView.getSeaCreaturesIdCounter() + 1);
            System.out.printf("%c(%d) [%d,%d]: produce %c(%d) [%d,%d]\n", animal.getSpecies().toString().charAt(0), animal.getId(),
                    animal.getPos().getX(), animal.getPos().getY(), baby.getSpecies().toString().charAt(0), baby.getId(),
                    baby.getPos().getX(), baby.getPos().getY());
        }
    }
}
