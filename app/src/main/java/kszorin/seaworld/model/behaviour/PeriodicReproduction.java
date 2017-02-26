package kszorin.seaworld.model.behaviour;

import kszorin.seaworld.model.Animal;
import kszorin.seaworld.model.PlayingWorld;
import kszorin.seaworld.model.Position;
import kszorin.seaworld.model.SeaCreature;

import java.util.List;
import java.util.Map;

public class PeriodicReproduction implements ReproductionBehaviour {
    @Override
    public void reproduct(Animal animal, PlayingWorld playingWorld, List<Position> foundPositionsInEnvirons) {
        int waterSpace[][] = playingWorld.getWaterSpace();
        Map<Integer, SeaCreature> seaCreaturesMap = playingWorld.getSeaCreaturesMap();
        Position pos = animal.getPos();

        if (foundPositionsInEnvirons.size() > 0) {
//        Случайным образом выбираем позицию из буфера свободных мест.
            int bufferRandomNum = (int) (Math.random() * (foundPositionsInEnvirons.size()));
            Position selectedFreePos = foundPositionsInEnvirons.get(bufferRandomNum);
//        Создаём детёныша на свободном месте.
            Animal baby = animal.getBaby(playingWorld.getSeaCreaturesIdCounter(), selectedFreePos);
            seaCreaturesMap.put(playingWorld.getSeaCreaturesIdCounter(), baby);
            waterSpace[selectedFreePos.getY()][selectedFreePos.getX()] = playingWorld.getSeaCreaturesIdCounter();
            playingWorld.setSeaCreaturesIdCounter(playingWorld.getSeaCreaturesIdCounter() + 1);

            System.out.printf("%c(%d) [%d,%d]: produce %c(%d) [%d,%d]\n", animal.getSpecies().toString().charAt(0), animal.getId(),
                    animal.getPos().getX(), animal.getPos().getY(), baby.getSpecies().toString().charAt(0), baby.getId(),
                    baby.getPos().getX(), baby.getPos().getY());
        }
    }
}
