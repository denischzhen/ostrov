package ostrov.environment;

import ostrov.animals.Animal;
import java.util.ArrayList;
import java.util.List;

public class Location {
    public int x, y;
    public List<Animal> animals = new ArrayList<>();
    public int plants = 0;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public int countAnimal(Class<? extends Animal> animalClass) {
        int count = 0;
        for (Animal animal : animals) {
            if (animal.getClass() == animalClass) {
                count++;
            }
        }
        return count;
    }
}