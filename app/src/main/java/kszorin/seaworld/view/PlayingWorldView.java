package kszorin.seaworld.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Map;

import kszorin.seaworld.R;
import kszorin.seaworld.model.Orca;
import kszorin.seaworld.model.Penguin;
import kszorin.seaworld.model.Position;
import kszorin.seaworld.model.SeaCreature;

public class PlayingWorldView extends SurfaceView implements SurfaceHolder.Callback {
    public static final byte FIELD_SIZE_X = 10;
    public static final byte FIELD_SIZE_Y = 15;
    private static final byte ORCAS_PERCENT_FILLING = 5;
    private static final byte PENGUINS_PERCENT_FILLING = 50;
    private static final int UPDATE_POSITIONS_DELAY = 100;
    private static final int TEXT_SIZE_DIVISOR = 40;
    public static final int CLEAR_WATER_CODE = -1;

    private DrawWorldThread drawWorldThread;
    private UpdatePositionsThread updatePositionsThread;
//    Флаг поднимается, когда кликаем по экрану.
    private boolean updateFlag;

    private int screenWidth, screenHeight;
    private float squareWidth, squareHeight;

    private int orcasQuantity = 0, penguinsQuantity = 0;
    private int seaCreaturesIdCounter;
//    В матрице содержится id персонажа либо (-1) если место пустое.
    private int waterSpace[][];
//    В отображении ключём является id персонажа, а содержимым - сам персонаж.
    private Map<Integer, SeaCreature> seaCreaturesMap;

    private Paint backgroundPaint, linePaint, textPaint;
    private int textSize;

    private Bitmap orcaBmp, penguinBmp;

    public PlayingWorldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        waterSpace = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
        seaCreaturesMap = new HashMap<Integer, SeaCreature>();

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);

        orcaBmp = BitmapFactory.decodeResource(getResources(), R.drawable.orca);
        penguinBmp = BitmapFactory.decodeResource(getResources(), R.drawable.tux);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        squareWidth = screenWidth / FIELD_SIZE_X;
        textSize = screenHeight / TEXT_SIZE_DIVISOR;
        squareHeight = (screenHeight - textSize) / FIELD_SIZE_Y;
        textPaint.setTextSize(textSize);
    }

    public void resetGame() {
        updateFlag = false;

//        Выставляем первоначальные значения.
        orcasQuantity = FIELD_SIZE_X * FIELD_SIZE_Y * ORCAS_PERCENT_FILLING / 100;
        penguinsQuantity = FIELD_SIZE_X * FIELD_SIZE_Y * PENGUINS_PERCENT_FILLING / 100;
        for (int i = 0, j; i < FIELD_SIZE_Y; i++)
            for (j=0; j < FIELD_SIZE_X; j++)
                waterSpace[i][j] = CLEAR_WATER_CODE;
        seaCreaturesIdCounter = 0;
        seaCreaturesMap.clear();

//        Создаём и расставляем касаток на поле.
        int possiblePos, possiblePosX, possiblePosY;
        for (int i=0; i < orcasQuantity; i++) {
            do {
                possiblePos = (int) (Math.random() * (FIELD_SIZE_Y * FIELD_SIZE_X));
                possiblePosY = possiblePos / FIELD_SIZE_X;
                possiblePosX = possiblePos % FIELD_SIZE_X;
                if (waterSpace[possiblePosY][possiblePosX] == CLEAR_WATER_CODE ) {
                    waterSpace[possiblePosY][possiblePosX] = seaCreaturesIdCounter;
                    seaCreaturesMap.put(seaCreaturesIdCounter, new Orca(seaCreaturesIdCounter, new Position(possiblePosX, possiblePosY), this, orcaBmp));
                    System.out.printf("Orca (id=%d) ДОБАВЛЕНА в [%d,%d]  \n", seaCreaturesIdCounter, possiblePosX, possiblePosY);
                    seaCreaturesIdCounter++;
                    break;
                }
                else
                    System.out.printf("Позиция [%d,%d] УЖЕ ЗАНЯТА существом с id=%d\n", possiblePosX, possiblePosY, waterSpace[possiblePosY][possiblePosX]);
            } while(true);
        }

//        Создаём и расставляем пингвинов на поле.
        for (int i=0; i < penguinsQuantity; i++) {
            do {
                possiblePos = (int) (Math.random() * (FIELD_SIZE_Y * FIELD_SIZE_X));
                possiblePosY = possiblePos / FIELD_SIZE_X;
                possiblePosX = possiblePos % FIELD_SIZE_X;
                if (waterSpace[possiblePosY][possiblePosX] == CLEAR_WATER_CODE ) {
                    waterSpace[possiblePosY][possiblePosX] = seaCreaturesIdCounter;
                    seaCreaturesMap.put(seaCreaturesIdCounter, new Penguin(seaCreaturesIdCounter, new Position(possiblePosX, possiblePosY), this, penguinBmp));
                    System.out.printf("Penguin (id=%d) ДОБАВЛЕН в [%d,%d]  \n", seaCreaturesIdCounter, possiblePosX, possiblePosY);
                    seaCreaturesIdCounter++;
                    break;
                }
                else
                    System.out.printf("Позиция [%d,%d] УЖЕ ЗАНЯТА существом с id=%d\n", possiblePosX, possiblePosY, waterSpace[possiblePosY][possiblePosX]);
            } while(true);
        }
    }

    private void drawWorld(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
//        Рисуем линии.
        for (int i=0; i <=FIELD_SIZE_X; i++ ) {
            canvas.drawLine(i * squareWidth, 0, i * squareWidth, (screenHeight - textSize), linePaint);
        }
        for (int i=0; i <=FIELD_SIZE_Y; i++ ) {
            canvas.drawLine(0, i * squareHeight, screenWidth, i * squareHeight, linePaint);
        }
//        Рисуем персонажей.
        Paint creaturePaint = new Paint();
        for (int i = 0, j; i < FIELD_SIZE_Y; i++)
            for (j=0; j < FIELD_SIZE_X; j++) {
                if (waterSpace[i][j] != CLEAR_WATER_CODE) {
                    seaCreaturesMap.get(waterSpace[i][j]).draw(canvas, creaturePaint);
                }
            }
//        Печатаем количество касаток и пингвинов.
        canvas.drawText("O: " + orcasQuantity, 0, screenHeight, textPaint);
        canvas.drawText("P: " + penguinsQuantity, screenWidth/2, screenHeight, textPaint);
    }

    private void updatePositions() {
        try {
//            Цикл жизнедеятельности созданий.
            for (int i = 0, j; i < FIELD_SIZE_Y; i++)
                for (j=0; j < FIELD_SIZE_X; j++) {
                    if (updateFlag && (waterSpace[i][j] != CLEAR_WATER_CODE) && (!(seaCreaturesMap.get(waterSpace[i][j]).isLifeStepExecuteFlag()))) {
                        seaCreaturesMap.get(waterSpace[i][j]).setLifeStepExecuteFlag(true);
                        seaCreaturesMap.get(waterSpace[i][j]).lifeStep();
                        Thread.sleep(UPDATE_POSITIONS_DELAY);
                    }
                }
//            Сбрасываем признак того, что персонаж был уже обработан.
            for (SeaCreature seaCreature:seaCreaturesMap.values())
                seaCreature.setLifeStepExecuteFlag(false);
            System.out.printf("Количество касаток: %d. Количество пингвинов: %d\n", orcasQuantity, penguinsQuantity);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        resetGame();

        drawWorldThread = new DrawWorldThread(holder);
        drawWorldThread.setThreadIsRunning(true);
        drawWorldThread.start();

        updatePositionsThread = new UpdatePositionsThread();
        updatePositionsThread.setThreadIsRunning(true);
        updatePositionsThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawWorldThread.setThreadIsRunning(false);
        updatePositionsThread.setThreadIsRunning(false);
        updateFlag = false;

        while (retry) {
            try {
                drawWorldThread.join();
                updatePositionsThread.join();
                retry = false;
            } catch (InterruptedException iex) {
                iex.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        if (action == MotionEvent.ACTION_DOWN)
            updateFlag = true;

        return true;
    }

    private class DrawWorldThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean threadIsRunning = true;

        DrawWorldThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
            setName("PlayingWorldthread");
        }

        void setThreadIsRunning(boolean threadIsRunning) {
            this.threadIsRunning = threadIsRunning;
        }

        @Override
        public void run() {
            Canvas canvas = null;

            while (threadIsRunning) {
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        drawWorld(canvas);
                    }
                }
                finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private class UpdatePositionsThread extends Thread {
        private boolean threadIsRunning = true;

        UpdatePositionsThread() {
            setName("UpdatePositionsThread");
        }

        void setThreadIsRunning(boolean threadIsRunning) {
            this.threadIsRunning = threadIsRunning;
        }

        @Override
        public void run() {
            while (threadIsRunning) {
                if (updateFlag) {
                    updatePositions();
                    updateFlag = false;
                }
            }
        }
    }

    public void stopGame() {
        if (drawWorldThread != null)
            drawWorldThread.setThreadIsRunning(false);
        if (updatePositionsThread != null)
            updatePositionsThread.setThreadIsRunning(false);
    }

    public Map<Integer, SeaCreature> getSeaCreaturesMap() {
        return seaCreaturesMap;
    }

    public int[][] getWaterSpace() {
        return waterSpace;
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

    public void setOrcasQuantity(int orcasQuantity) {
        this.orcasQuantity = orcasQuantity;
    }

    public void setPenguinsQuantity(int penguinsQuantity) {
        this.penguinsQuantity = penguinsQuantity;
    }

    public int getOrcasQuantity() {
        return orcasQuantity;
    }

    public int getPenguinsQuantity() {
        return penguinsQuantity;
    }
}
