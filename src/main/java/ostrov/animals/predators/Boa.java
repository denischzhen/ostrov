package ostrov.animals.predators;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import ostrov.animals.Herbivore;
import ostrov.animals.Predator;
import ostrov.animals.herbivores.*;
import ostrov.environment.Island;
import ostrov.environment.Location;

public class Boa extends Predator {
    public Boa() {
        super(15.0, 30, 1, 3.0);
        this.symbol = "🐍";
    }

    @Override
    protected int getHuntingProbability(Herbivore prey) {
        if (prey instanceof Rabbit) return 40;
        if (prey instanceof Mouse) return 70;
        return 50;
    }

    @Override
    protected Predator createOffspring() {
        return new Boa();
    }

    @Override
    public Location move(Location currentLocation, Island island) {
        List<Location> neighbors = island.getNeighbors(currentLocation.x, currentLocation.y);
        return neighbors.isEmpty() ? currentLocation : neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
    }
}