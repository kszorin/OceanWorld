package kszorin.seaworld.model;


public abstract class SeaCreature {
    protected int id;
    protected SealCreatureSpecies species;
    protected Position pos;
    protected byte environs;

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

    public SeaCreature(int id, Position pos) {
        this.id = id;
        this.pos = pos;
    }

    public abstract void lifeStep (PlayingWorld playingWorld);

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
