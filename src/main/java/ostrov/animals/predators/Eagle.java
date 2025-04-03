package ostrov.animals.predators;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import ostrov.animals.Herbivore;
import ostrov.animals.Predator;
import ostrov.animals.herbivores.*;
import ostrov.environment.Island;
import ostrov.environment.Location;

public class Eagle extends Predator {
    public Eagle() {
        super(6.0, 20, 3, 1.0);
        this.symbol = "🦅";
    }

    @Override
    protected int getHuntingProbability(Herbivore prey) {
        if (prey instanceof Mouse) return 80;
        if (prey instanceof Rabbit) return 30;
        return 50;
    }

    @Override
    protected Predator createOffspring() {
        return new Eagle();
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