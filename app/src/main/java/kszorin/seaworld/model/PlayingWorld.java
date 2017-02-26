package kszorin.seaworld.model;


import android.graphics.Canvas;

import java.util.*;

public class PlayingWorld {
    private final byte fieldSizeX;
    private final byte fieldSizeY;
    private final int orcasQuantity;
    private final int penguinsQuantity;
    private int seaCreaturesIdCounter;
    private int waterSpace[][];

    private Map<Integer, SeaCreature> seaCreaturesMap;

    public PlayingWorld(byte fieldSizeX, byte fieldSizeY, byte orcasPercentFilling, byte penguinsPercentFilling) throws Exception {
        if ((fieldSizeX <= 0) || (fieldSizeY <= 0) || (orcasPercentFilling <=0) || (penguinsPercentFilling <=0))
            throw new Exception("Incorrect input data!");
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        orcasQuantity = fieldSizeX * fieldSizeY * orcasPercentFilling / 100;
        penguinsQuantity = fieldSizeX * fieldSizeY * penguinsPercentFilling / 100;
        waterSpace = new int[fieldSizeY][fieldSizeX];
        seaCreaturesMap = new HashMap<Integer, SeaCreature>();
        reset();
    }

    public void reset () {
//        Создаём чистое водное пространство.
        for (int i = 0, j; i < fieldSizeY; i++)
            for (j=0; j < fieldSizeX; j++)
                waterSpace[i][j] = -1;
//        Обнуляем счётчик и массив морских созданий.
        seaCreaturesIdCounter = 0;
        seaCreaturesMap.clear();

//        Создаём и расставляем касаток на поле
        int possiblePos, possiblePosX, possiblePosY;
        for (int i=0; i < orcasQuantity; i++) {
            do {
                possiblePos = (int) (Math.random() * (fieldSizeY * fieldSizeX));
                possiblePosY = possiblePos / fieldSizeX;
                possiblePosX = possiblePos % fieldSizeX;
                if (waterSpace[possiblePosY][possiblePosX] == -1 ) {
                    waterSpace[possiblePosY][possiblePosX] = seaCreaturesIdCounter;
                    seaCreaturesMap.put(seaCreaturesIdCounter, new Orca(seaCreaturesIdCounter, new Position(possiblePosX, possiblePosY)));
                    seaCreaturesIdCounter++;
                    System.out.printf("Orca (id=%d) ДОБАВЛЕНА в [%d,%d]  \n", waterSpace[possiblePosY][possiblePosX], possiblePosX, possiblePosY);
                    break;
                }
                else
                    System.out.printf("Позиция [%d,%d] УЖЕ ЗАНЯТА существом с id=%d\n", possiblePosX, possiblePosY, waterSpace[possiblePosY][possiblePosX]);
            } while(true);
        }

//        Создаём и расставляем пингвинов на поле
        for (int i=0; i < penguinsQuantity; i++) {
            do {
                possiblePos = (int) (Math.random() * (fieldSizeY * fieldSizeX));
                possiblePosY = possiblePos / fieldSizeX;
                possiblePosX = possiblePos % fieldSizeX;
                if (waterSpace[possiblePosY][possiblePosX] == -1 ) {
                    waterSpace[possiblePosY][possiblePosX] = seaCreaturesIdCounter;
                    seaCreaturesMap.put(seaCreaturesIdCounter, new Penguin(seaCreaturesIdCounter, new Position(possiblePosX, possiblePosY)));
                    seaCreaturesIdCounter++;
                    System.out.printf("Penguin (id=%d) ДОБАВЛЕН в [%d,%d]  \n", waterSpace[possiblePosY][possiblePosX], possiblePosX, possiblePosY);
                    break;
                }
                else
                    System.out.printf("Позиция [%d,%d] УЖЕ ЗАНЯТА существом с id=%d\n", possiblePosX, possiblePosY, waterSpace[possiblePosY][possiblePosX]);
            } while(true);
        }
    }
//      TODO: сделать последовательное обновление персонажей
    public void update() {
        List<SeaCreature> seaCreaturesInOrder = new ArrayList<SeaCreature>();
//        Набираем список существ в порядке обхода поля.
        for (int i = 0, j; i < fieldSizeY; i++)
            for (j=0; j < fieldSizeX; j++) {
                if (waterSpace[i][j] != -1)
                    seaCreaturesInOrder.add(seaCreaturesMap.get(waterSpace[i][j]));
            }
//        Запускаем очередной жизненный цикл.
        for (SeaCreature seaCreature: seaCreaturesInOrder) {
            if (seaCreaturesMap.containsValue(seaCreature))
                seaCreature.lifeStep(this);
        }
    }


    public void draw(Canvas canvas) {

    }


    public Map<Integer, SeaCreature> getSeaCreaturesMap() {
        return seaCreaturesMap;
    }

    public int[][] getWaterSpace() {
        return waterSpace;
    }

    public byte getFieldSizeX() {
        return fieldSizeX;
    }

    public byte getFieldSizeY() {
        return fieldSizeY;
    }

    public int getSeaCreaturesIdCounter() {
        return seaCreaturesIdCounter;
    }

    public void setSeaCreaturesIdCounter(int seaCreaturesIdCounter) {
        this.seaCreaturesIdCounter = seaCreaturesIdCounter;
    }
}
