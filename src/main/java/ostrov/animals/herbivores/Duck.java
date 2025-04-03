package ostrov.animals.herbivores;

import java.util.ArrayList;
import java.util.List;
import ostrov.animals.Animal;
import ostrov.animals.Herbivore;
import ostrov.environment.Location;

public class Duck extends Herbivore {
    public Duck() {
        super(1.0, 200, 4, 0.15);
        this.symbol = "🦆";
    }

    @Override
    public void eat(Location location) {
        List<Caterpillar> caterpillars = new ArrayList<>();
        for (Animal animal : location.animals) {
            if (!(animal instanceof Caterpillar)) {
            } else {
                caterpillars.add((Caterpillar) animal);
            }
        }
        if (!caterpillars.isEmpty() && chance(70)) {
            location.removeAnimal(caterpillars.get(0));
            currentHunger = 0.0;
            System.out.println("Duck съела Caterpillar в клетке (" + location.x + ", " + location.y + ")");
        } else {
            super.eat(location);
        }
    }

    @Override
    protected Herbivore createOffspring() {
        return new Duck();
    }
}