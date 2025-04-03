package ostrov.animals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import ostrov.environment.Island;
import ostrov.environment.Location;

public abstract class Herbivore extends Animal {
    public Herbivore(double weight, int maxCountPerCell, int speed, double satiety) {
        super(weight, maxCountPerCell, speed, satiety);
    }

    @Override
    public void eat(Location location) {
        if (location.plants > 0) {
            location.plants--;
            currentHunger = 0.0;
            System.out.println(this.getClass().getSimpleName() + " съел растение в клетке (" + location.x + ", " + location.y + ")");
        } else {
            currentHunger += 1;
        }
    }

    @Override
    public void reproduce(Location location) {
        List<Animal> mates = new ArrayList<>();
        for (Animal animal : location.animals) {
            if (animal.getClass() == this.getClass() && animal != this) {
                mates.add(animal);
            }
        }
        if (!mates.isEmpty() && location.countAnimal(this.getClass()) < maxCountPerCell) {
            Herbivore offspring = createOffspring();
            location.addAnimal(offspring);
            System.out.println(this.getClass().getSimpleName() + " размножился в клетке (" + location.x + ", " + location.y + ")");
        }
    }

    protected abstract Herbivore createOffspring();

    @Override
    public Location move(Location currentLocation, Island island) {
        List<Location> neighbors = island.getNeighbors(currentLocation.x, currentLocation.y);
        return neighbors.isEmpty() ? currentLocation : neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
    }
}