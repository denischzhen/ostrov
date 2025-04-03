package ostrov.animals.herbivores;

import ostrov.animals.Herbivore;

public class Deer extends Herbivore {
    public Deer() {
        super(300.0, 20, 4, 50.0);
        this.symbol = "🦌";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Deer();
    }
}