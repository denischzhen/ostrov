package ostrov.animals.herbivores;

import ostrov.animals.Herbivore;

public class Buffalo extends Herbivore {
    public Buffalo() {
        super(700.0, 10, 3, 100.0);
        this.symbol = "🐃";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Buffalo();
    }
}