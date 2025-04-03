package ostrov.animals.predators;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import ostrov.animals.Herbivore;
import ostrov.animals.Predator;
import ostrov.animals.herbivores.*;
import ostrov.environment.Island;
import ostrov.environment.Location;

public class Bear extends Predator {
    public Bear() {
        super(500.0, 5, 2, 80.0);
        this.symbol = "🐻";
    }

    @Override
    protected int getHuntingProbability(Herbivore prey) {
        if (prey instanceof Deer) return 40;
        if (prey instanceof Rabbit) return 50;
        return 30;
    }

    @Override
    protected Predator createOffspring() {
        return new Bear();
    }

    @Override
    public Location move(Location currentLocation, Island island) {
        Location newLocation = currentLocation;
        for (int i = 0; i < speed; i++) {
            List<Location> neighbors = island.getNeighbors(newLocation.x, newLocation.y);
            if (!neighbors.isEmpty()) {
                newLocation = neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
            }
        }
        return newLocation;
    }
}