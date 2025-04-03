package ostrov.animals.predators;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import ostrov.animals.Herbivore;
import ostrov.animals.Predator;
import ostrov.animals.herbivores.*;
import ostrov.environment.Island;
import ostrov.environment.Location;

public class Wolf extends Predator {
    public Wolf() {
        super(50.0, 30, 3, 8.0);
        this.symbol = "🐺";
    }

    @Override
    protected int getHuntingProbability(Herbivore prey) {
        if (prey instanceof Rabbit) return 60;
        if (prey instanceof Mouse) return 80;
        return 50;
    }

    @Override
    protected Predator createOffspring() {
        return new Wolf();
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