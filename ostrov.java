import java.util.*;
import java.util.concurrent.*;

abstract class Animal {
    protected double weight;
    protected int maxCountPerCell;
    protected int speed;
    protected double satiety;
    protected double currentHunger = 0.0;
    protected String symbol;

    public Animal(double weight, int maxCountPerCell, int speed, double satiety) {
        this.weight = weight;
        this.maxCountPerCell = maxCountPerCell;
        this.speed = speed;
        this.satiety = satiety;
    }

    public abstract void eat(Location location);
    public abstract void reproduce(Location location);
    public abstract Location move(Location currentLocation, Island island);

    protected boolean chance(int probability) {
        return ThreadLocalRandom.current().nextInt(100) < probability;
    }
}

abstract class Predator extends Animal {
    public Predator(double weight, int maxCountPerCell, int speed, double satiety) {
        super(weight, maxCountPerCell, speed, satiety);
    }

    @Override
    public void eat(Location location) {
        List<Herbivore> herbivores = new ArrayList<>();
        for (Animal animal : location.animals) {
            if (animal instanceof Herbivore) {
                herbivores.add((Herbivore) animal);
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

abstract class Herbivore extends Animal {
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

class Location {
    int x, y;
    List<Animal> animals = new ArrayList<>();
    int plants = 0;

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

class Island {
    int width, height;
    Location[][] grid;

    public Island(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Location[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Location(x, y);
            }
        }
    }

    public Location getLocation(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height) ? grid[y][x] : null;
    }

    public List<Location> getNeighbors(int x, int y) {
        List<Location> neighbors = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            Location neighbor = getLocation(x + dir[0], y + dir[1]);
            if (neighbor != null) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }
}

// Пример классов животных (Wolf, Rabbit и т.д.)
class Wolf extends Predator {
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

class Rabbit extends Herbivore {
    public Rabbit() {
        super(2.0, 150, 2, 0.45);
        this.symbol = "🐇";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Rabbit();
    }
}

// Класс Boa (Удав)
class Boa extends Predator {
    public Boa() {
        super(15.0, 30, 1, 3.0);
        this.symbol = "🐍";
    }

    @Override
    protected int getHuntingProbability(Herbivore prey) {
        if (prey instanceof Rabbit) return 40;
        if (prey instanceof Mouse) return 70;
        return 50;
    }

    @Override
    protected Predator createOffspring() {
        return new Boa();
    }

    @Override
    public Location move(Location currentLocation, Island island) {
        List<Location> neighbors = island.getNeighbors(currentLocation.x, currentLocation.y);
        return neighbors.isEmpty() ? currentLocation : neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
    }
}

// Класс Fox (Лиса)
class Fox extends Predator {
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

// Класс Bear (Медведь)
class Bear extends Predator {
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

// Класс Eagle (Орел)
class Eagle extends Predator {
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

// Класс Mouse (Мышь)
class Mouse extends Herbivore {
    public Mouse() {
        super(0.05, 500, 1, 0.01);
        this.symbol = "🐁";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Mouse();
    }
}

// Класс Horse (Лошадь)
class Horse extends Herbivore {
    public Horse() {
        super(400.0, 20, 4, 60.0);
        this.symbol = "🐎";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Horse();
    }
}

// Класс Deer (Олень)
class Deer extends Herbivore {
    public Deer() {
        super(300.0, 20, 4, 50.0);
        this.symbol = "🦌";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Deer();
    }
}

// Класс Goat (Коза)
class Goat extends Herbivore {
    public Goat() {
        super(60.0, 140, 3, 10.0);
        this.symbol = "🐐";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Goat();
    }
}

// Класс Sheep (Овца)
class Sheep extends Herbivore {
    public Sheep() {
        super(70.0, 140, 3, 15.0);
        this.symbol = "🐑";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Sheep();
    }
}

// Класс Boar (Кабан)
class Boar extends Herbivore {
    public Boar() {
        super(400.0, 50, 2, 50.0);
        this.symbol = "🐗";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Boar();
    }
}

// Класс Buffalo (Буйвол)
class Buffalo extends Herbivore {
    public Buffalo() {
        super(700.0, 10, 3, 100.0);
        this.symbol = "🐃";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Buffalo();
    }
}

// Класс Duck (Утка)
class Duck extends Herbivore {
    public Duck() {
        super(1.0, 200, 4, 0.15);
        this.symbol = "🦆";
    }

    @Override
    public void eat(Location location) {
        List<Caterpillar> caterpillars = new ArrayList<>();
        for (Animal animal : location.animals) {
            if (animal instanceof Caterpillar) {
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

// Класс Caterpillar (Гусеница)
class Caterpillar extends Herbivore {
    public Caterpillar() {
        super(0.01, 1000, 0, 0.0);
        this.symbol = "🐛";
    }

    @Override
    protected Herbivore createOffspring() {
        return new Caterpillar();
    }

    @Override
    public Location move(Location currentLocation, Island island) {
        return currentLocation; // Гусеницы не двигаются
    }
}

class Simulation {
    Island island;
    long simulationTickMillis;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ExecutorService animalPool = Executors.newFixedThreadPool(10);
    boolean running = true;

    public Simulation(Island island, long simulationTickMillis) {
        this.island = island;
        this.simulationTickMillis = simulationTickMillis;
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
                                if (newLocation.animals.size() < animal.maxCountPerCell) {
                                    newLocation.addAnimal(animal);
                                }
                            }
                        }
                        if (animal.currentHunger > 5) {
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
            e.printStackTrace();
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
                    System.out.print(cell.animals.get(0).symbol);
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
        running = false;
        scheduler.shutdown();
        animalPool.shutdown();
    }
}

class ostrov {
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