package ostrov;

import java.util.concurrent.ThreadLocalRandom;
import ostrov.animals.*;
import ostrov.animals.herbivores.*;
import ostrov.animals.predators.*;
import ostrov.environment.Island;
import ostrov.environment.Location;
import ostrov.simulation.Simulation;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Island island = new Island(50, 20);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        for (Location[] row : island.grid) {
            for (Location loc : row) {
                loc.plants = random.nextInt(0, 50);
                if (random.nextDouble() < 0.1) {
                    Animal animal = switch (random.nextInt(0, 15)) {
                        case 0 -> new Wolf();
                        case 1 -> new Boa();
                        case 2 -> new Fox();
                        case 3 -> new Bear();
                        case 4 -> new Eagle();
                        case 5 -> new Horse();
                        case 6 -> new Deer();
                        case 7 -> new Rabbit();
                        case 8 -> new Mouse();
                        case 9 -> new Goat();
                        case 10 -> new Sheep();
                        case 11 -> new Boar();
                        case 12 -> new Buffalo();
                        case 13 -> new Duck();
                        default -> new Caterpillar();
                    };
                    loc.addAnimal(animal);
                }
            }
        }
        
        Simulation simulation = new Simulation(island, 1000L);
        simulation.start();
        Thread.sleep(30000);
        simulation.stop();
    }
}