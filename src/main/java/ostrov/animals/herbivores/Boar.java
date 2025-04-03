package ostrov.animals.herbivores;

import ostrov.animals.Herbivore;

public class Boar extends Herbivore {
    public Boar() {
        super(400.0, 50, 2, 50.0);
        this.symbol = "🐗";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Boar();
    }
}