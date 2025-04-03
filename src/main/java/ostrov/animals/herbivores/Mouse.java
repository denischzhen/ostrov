package ostrov.animals.herbivores;

import ostrov.animals.Herbivore;

public class Mouse extends Herbivore {
    public Mouse() {
        super(0.05, 500, 1, 0.01);
        this.symbol = "🐁";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Mouse();
    }
}