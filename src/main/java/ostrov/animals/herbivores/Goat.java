package ostrov.animals.herbivores;

import ostrov.animals.Herbivore;

public class Goat extends Herbivore {
    public Goat() {
        super(60.0, 140, 3, 10.0);
        this.symbol = "🐐";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Goat();
    }
}