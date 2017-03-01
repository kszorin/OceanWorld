package kszorin.seaworld.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import kszorin.seaworld.view.PlayingWorldView;

public abstract class SeaCreature {
    protected int id;
    protected SealCreatureSpecies species;
    protected Position pos;
    protected byte environs;
    protected PlayingWorldView playingWorldView;
    protected Bitmap bmp;
    private boolean lifeStepExecuteFlag;

    protected SeaCreature(int id, Position pos, PlayingWorldView playingWorldView, Bitmap bmp) {
        this.id = id;
        this.pos = pos;
        this.bmp = bmp;
        this.playingWorldView = playingWorldView;
        lifeStepExecuteFlag = false;
    }

    public int getId() {
        return id;
    }

    public SealCreatureSpecies getSpecies() {
        return species;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public abstract void lifeStep ();

    public abstract void draw (Canvas canvas, Paint paint);

    public boolean isLifeStepExecuteFlag() {
        return lifeStepExecuteFlag;
    }

    public void setLifeStepExecuteFlag(boolean lifeStepExecuteFlag) {
        this.lifeStepExecuteFlag = lifeStepExecuteFlag;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    @Override
    public String toString() {
        return "SeaCreature{" +
                "id=" + id +
                ", species=" + species +
                ", pos[X,Y]=[" + pos.getX() +
                "," + pos.getY() + "]" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeaCreature that = (SeaCreature) o;

        if (id != that.id)
            return false;
        return species == that.species;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + species.hashCode();
        return result;
    }

}
