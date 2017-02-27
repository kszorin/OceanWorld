package kszorin.seaworld.model;

import android.graphics.Canvas;
import android.graphics.Paint;

import kszorin.seaworld.view.PlayingWorldView;

public abstract class SeaCreature {
    protected int id;
    protected SealCreatureSpecies species;
    protected Position pos;
    protected byte environs;
    protected PlayingWorldView playingWorldView;
    private boolean lifeStepExecute;

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

    public SeaCreature(int id, Position pos, PlayingWorldView playingWorldView) {
        this.id = id;
        this.pos = pos;
        this.playingWorldView = playingWorldView;
        lifeStepExecute = false;
    }

    public abstract void lifeStep ();

    public abstract void draw (Canvas canvas, Paint paint);

    public boolean isLifeStepExecute() {
        return lifeStepExecute;
    }

    public void setLifeStepExecute(boolean lifeStepExecute) {
        this.lifeStepExecute = lifeStepExecute;
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

        if (id != that.id) return false;
        return species == that.species;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + species.hashCode();
        return result;
    }

}
