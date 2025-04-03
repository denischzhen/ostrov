package ostrov.simulation;

import java.util.*;
import java.util.concurrent.*;
import ostrov.animals.Animal;
import ostrov.environment.Island;
import ostrov.environment.Location;

public class Simulation {
    private final Island island;
    private final long simulationTickMillis;
    private final ScheduledExecutorService scheduler;
    private final ExecutorService animalPool;

    public Simulation(Island island, long simulationTickMillis) {
        this.island = island;
        this.simulationTickMillis = simulationTickMillis;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.animalPool = Executors.newFixedThreadPool(10);
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::tick, 0, simulationTickMillis, TimeUnit.MILLISECONDS);
    }

    private void tick() {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (Location[] row : island.grid) {
            for (Location location : row) {
                List<Animal> animalsCopy = new ArrayList<>(location.animals);
                for (Animal animal : animalsCopy) {
                    tasks.add(() -> {
                        animal.eat(location);
                        animal.reproduce(location);
                        Location newLocation = animal.move(location, island);
                        if (newLocation != location) {
                            synchronized (location) {
                                location.removeAnimal(animal);
                            }
                            synchronized (newLocation) {
                                if (newLocation.animals.size() < animal.getMaxCountPerCell()) {
                                    newLocation.addAnimal(animal);
                                }
                            }
                        }
                        if (animal.getCurrentHunger() > 5) {
                            synchronized (location) {
                                location.removeAnimal(animal);
                                System.out.println(animal.getClass().getSimpleName() + " умер от голода в клетке (" + location.x + ", " + location.y + ")");
                            }
                        }
                        return null;
                    });
                }
            }
        }
        try {
            animalPool.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Симуляция прервана: " + e.getMessage());
        }

        for (Location[] row : island.grid) {
            for (Location location : row) {
                location.plants = Math.min(location.plants + 1, 200);
            }
        }

        printField();

        if (Arrays.stream(island.grid).flatMap(Arrays::stream).allMatch(loc -> loc.animals.isEmpty())) {
            System.out.println("Симуляция завершена: все животные погибли.");
            stop();
        }
    }

    private void printField() {
        for (Location[] row : island.grid) {
            for (Location cell : row) {
                if (!cell.animals.isEmpty()) {
                    System.out.print(cell.animals.get(0).getSymbol());
                } else if (cell.plants > 0) {
                    System.out.print("🌱");
                } else {
                    System.out.print("∙");
                }
            }
            System.out.println();
        }
    }

    public void stop() {
        scheduler.shutdown();
        animalPool.shutdown();
    }
}