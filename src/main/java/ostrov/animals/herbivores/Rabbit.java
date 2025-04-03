package ostrov.animals.herbivores;

import ostrov.animals.Herbivore;

public class Rabbit extends Herbivore {
    public Rabbit() {
        super(2.0, 150, 2, 0.45);
        this.symbol = "🐇";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Rabbit();
    }
}