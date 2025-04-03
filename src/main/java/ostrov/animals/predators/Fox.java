package ostrov.animals.predators;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import ostrov.animals.Herbivore;
import ostrov.animals.Predator;
import ostrov.animals.herbivores.*;
import ostrov.environment.Island;
import ostrov.environment.Location;

public class Fox extends Predator {
    public Fox() {
        super(8.0, 30, 2, 2.0);
        this.symbol = "🦊";
    }

    @Override
    protected int getHuntingProbability(Herbivore prey) {
        if (prey instanceof Rabbit) return 70;
        if (prey instanceof Mouse) return 90;
        return 50;
    }

    @Override
    protected Predator createOffspring() {
        return new Fox();
    }

    @Override
    public Location move(Location currentLocation, Island island) {
        List<Location> neighbors = island.getNeighbors(currentLocation.x, currentLocation.y);
        return neighbors.isEmpty() ? currentLocation : neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
    }
}