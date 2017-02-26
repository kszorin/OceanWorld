package kszorin.seaworld.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kszorin.seaworld.model.Orca;
import kszorin.seaworld.model.Penguin;
import kszorin.seaworld.model.Position;
import kszorin.seaworld.model.SeaCreature;

public class PlayingWorldView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "MainView";

    public static final byte FIELD_SIZE_X = 10;
    public static final byte FIELD_SIZE_Y = 15;
    public static final byte ORCAS_PERCENT_FILLING = 5;
    public static final byte PENGUINS_PERCENT_FILLING = 20;

    private PlayingWorldThread playingWorldThread;
    private Activity activity;

    private int screenWidth;
    private int screenHeight;

    private final byte fieldSizeX;
    private final byte fieldSizeY;

    private float squareWidth;
    private float squareHeight;

    private final int orcasQuantity;
    private final int penguinsQuantity;
    private int seaCreaturesIdCounter;
    private int waterSpace[][];
    private Map<Integer, SeaCreature> seaCreaturesMap;

    private Paint backgroundPaint;
    private Paint linePaint;


    public PlayingWorldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Регистрация слушателя SurfaceHolder.Callback
        getHolder().addCallback(this);

        this.fieldSizeX = FIELD_SIZE_X;
        this.fieldSizeY = FIELD_SIZE_Y;
        orcasQuantity = fieldSizeX * fieldSizeY * ORCAS_PERCENT_FILLING / 100;
        penguinsQuantity = fieldSizeX * fieldSizeY * PENGUINS_PERCENT_FILLING / 100;
        waterSpace = new int[fieldSizeY][fieldSizeX];
        seaCreaturesMap = new HashMap<Integer, SeaCreature>();

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        squareWidth = screenWidth / fieldSizeX;
        squareHeight = screenHeight / fieldSizeY;
    }

//    TODO: обработка кнопки
    public void newGame() {
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
                    seaCreaturesMap.put(seaCreaturesIdCounter, new Orca(seaCreaturesIdCounter, new Position(possiblePosX, possiblePosY), this));
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
                    seaCreaturesMap.put(seaCreaturesIdCounter, new Penguin(seaCreaturesIdCounter, new Position(possiblePosX, possiblePosY), this));
                    seaCreaturesIdCounter++;
                    System.out.printf("Penguin (id=%d) ДОБАВЛЕН в [%d,%d]  \n", waterSpace[possiblePosY][possiblePosX], possiblePosX, possiblePosY);
                    break;
                }
                else
                    System.out.printf("Позиция [%d,%d] УЖЕ ЗАНЯТА существом с id=%d\n", possiblePosX, possiblePosY, waterSpace[possiblePosY][possiblePosX]);
            } while(true);
        }
    }

    private synchronized void updatePositions() {
        List<SeaCreature> seaCreaturesInOrder = new ArrayList<SeaCreature>();
//        Набираем список существ в порядке обхода поля.
        for (int i = 0, j; i < fieldSizeY; i++)
            for (j=0; j < fieldSizeX; j++) {
                if (waterSpace[i][j] != -1)
                    seaCreaturesInOrder.add(seaCreaturesMap.get(waterSpace[i][j]));
            }
//        Запускаем очередной жизненный цикл.
        try {
            for (SeaCreature seaCreature: seaCreaturesInOrder) {
                if (seaCreaturesMap.containsValue(seaCreature)) {
                    seaCreature.lifeStep();
//                    wait();
//                    Thread.sleep(1000);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public synchronized void drawGameElements(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
//        Рисуем линии.
        for (int i=0; i <=FIELD_SIZE_X; i++ ) {
            canvas.drawLine(i * squareWidth, 0, i * squareWidth, screenHeight, linePaint);
        }
        for (int i=0; i <=FIELD_SIZE_Y; i++ ) {
            canvas.drawLine(0, i * squareHeight, screenWidth, i * squareHeight, linePaint);
        }

//      TODO: придумать другой способ правильного обхода массива с существами
        List<SeaCreature> seaCreaturesInOrder = new ArrayList<SeaCreature>();
        Paint creaturePaint = new Paint();
//        Набираем список существ в порядке обхода поля.
        for (int i = 0, j; i < fieldSizeY; i++)
            for (j=0; j < fieldSizeX; j++) {
                if (waterSpace[i][j] != -1)
                    seaCreaturesInOrder.add(seaCreaturesMap.get(waterSpace[i][j]));
            }
//        Рисуем создания.
        for (SeaCreature seaCreature: seaCreaturesInOrder) {
            if (seaCreaturesMap.containsValue(seaCreature)) {
                seaCreature.draw(canvas, creaturePaint);
//            notify();
            }
        }
    }


    public void stopGame() {
//        TODO: завершение игры
        if (playingWorldThread != null)
            playingWorldThread.setThreadIsRunning(false);
    }

    public void releaseResources() {
//        TODO: освобожение ресурсов
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        newGame();
        playingWorldThread = new PlayingWorldThread(holder);
        playingWorldThread.setThreadIsRunning(true);
        playingWorldThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Обеспечить корректную зависимость потока
        boolean retry = true;
        playingWorldThread.setThreadIsRunning(false);

        while (retry) {
            try {
                playingWorldThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e(TAG, "Thread interrupted", e);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            updatePositions();
        }
        return true;
    }

    private class PlayingWorldThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean threadIsRunning = true;

        public PlayingWorldThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
            setName("PlayingWorldthread");
        }

        public void setThreadIsRunning(boolean threadIsRunning) {
            this.threadIsRunning = threadIsRunning;
        }

        @Override
        public void run() {
            Canvas canvas = null;

            while (threadIsRunning) {
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                        synchronized (surfaceHolder) {
                            drawGameElements(canvas);
                    }
                }
                finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
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

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public float getSquareHeight() {
        return squareHeight;
    }

    public float getSquareWidth() {
        return squareWidth;
    }

    public int getSeaCreaturesIdCounter() {
        return seaCreaturesIdCounter;
    }

    public void setSeaCreaturesIdCounter(int seaCreaturesIdCounter) {
        this.seaCreaturesIdCounter = seaCreaturesIdCounter;
    }
}
