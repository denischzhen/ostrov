package ostrov.animals.herbivores;

import ostrov.animals.Herbivore;

public class Sheep extends Herbivore {
    public Sheep() {
        super(70.0, 140, 3, 15.0);
        this.symbol = "🐑";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Sheep();
    }
}