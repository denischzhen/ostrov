package ostrov.animals.herbivores;

import ostrov.animals.Herbivore;
import ostrov.environment.Location;
import ostrov.environment.Island;

public class Caterpillar extends Herbivore {
    public Caterpillar() {
        super(0.01, 1000, 0, 0.0);
        this.symbol = "🐛";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Caterpillar();
    }

    @Override
    public Location move(Location currentLocation, Island island) {
        return currentLocation; // Гусеницы не двигаются
    }
}