package ostrov.animals;

import java.util.concurrent.ThreadLocalRandom;
import ostrov.environment.Island;
import ostrov.environment.Location;

public abstract class Animal {
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

    // Геттеры для доступа из других пакетов
    public int getMaxCountPerCell() {
        return this.maxCountPerCell;
    }

    public double getCurrentHunger() {
        return this.currentHunger;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public double getWeight() {
        return this.weight;
    }

    public int getSpeed() {
        return this.speed;
    }

    // Абстрактные методы
    public abstract void eat(Location location);
    public abstract void reproduce(Location location);
    public abstract Location move(Location currentLocation, Island island);

    protected boolean chance(int probability) {
        return ThreadLocalRandom.current().nextInt(100) < probability;
    }
}