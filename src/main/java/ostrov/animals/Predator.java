package ostrov.animals;

import java.util.ArrayList;
import java.util.List;
import ostrov.environment.Location;

public abstract class Predator extends Animal {
    public Predator(double weight, int maxCountPerCell, int speed, double satiety) {
        super(weight, maxCountPerCell, speed, satiety);
    }

    @Override
    public void eat(Location location) {
        List<Herbivore> herbivores = new ArrayList<>();
        for (Animal animal : location.animals) {
            if (animal instanceof Herbivore herbivore) {
                herbivores.add(herbivore);
            }
        }
        for (Herbivore prey : herbivores) {
            int probability = getHuntingProbability(prey);
            if (chance(probability)) {
                location.removeAnimal(prey);
                currentHunger = 0.0;
                System.out.println(this.getClass().getSimpleName() + " съел " + prey.getClass().getSimpleName() + " в клетке (" + location.x + ", " + location.y + ")");
                return;
            }
        }
        currentHunger += 1;
    }

    protected abstract int getHuntingProbability(Herbivore prey);

    @Override
    public void reproduce(Location location) {
        List<Animal> mates = new ArrayList<>();
        for (Animal animal : location.animals) {
            if (animal.getClass() == this.getClass() && animal != this) {
                mates.add(animal);
            }
        }
        if (!mates.isEmpty() && location.countAnimal(this.getClass()) < maxCountPerCell) {
            Predator offspring = createOffspring();
            location.addAnimal(offspring);
            System.out.println(this.getClass().getSimpleName() + " размножился в клетке (" + location.x + ", " + location.y + ")");
        }
    }

    protected abstract Predator createOffspring();
}