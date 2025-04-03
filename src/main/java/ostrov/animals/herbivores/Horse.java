package ostrov.animals.herbivores;

import ostrov.animals.Herbivore;

public class Horse extends Herbivore {
    public Horse() {
        super(400.0, 20, 4, 60.0);
        this.symbol = "🐎";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Horse();
    }
}